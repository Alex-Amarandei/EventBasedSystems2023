package fii.ebs.bolts;

import com.google.protobuf.InvalidProtocolBufferException;
import fii.ebs.generated.Publication;
import fii.ebs.generated.Subscription;
import fii.ebs.models.FieldSubscription;
import fii.ebs.statistics.StatCounter;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fii.ebs.consts.Constants.CITY;
import static fii.ebs.consts.Constants.DIRECTION;
import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.consts.Constants.NOTIFICATION_DATA;
import static fii.ebs.consts.Constants.NOTIFIER;
import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.PUBLICATION;
import static fii.ebs.consts.Constants.PUBLICATION_DATA;
import static fii.ebs.consts.Constants.STATION_ID;
import static fii.ebs.consts.Constants.SUBSCRIPTION;
import static fii.ebs.consts.Constants.TEMPERATURE;
import static fii.ebs.consts.Constants.WINDOW_SIZE;
import static fii.ebs.consts.Constants.WIND_SPEED;
import static fii.ebs.utils.Converter.convertFieldSubscriptions;
import static fii.ebs.utils.OutputPrinter.formatEvent;
import static fii.ebs.utils.OutputPrinter.formatInvalidField;
import static fii.ebs.utils.OutputPrinter.formatTask;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

public class BrokerBolt extends BaseRichBolt {
    private static final long serialVersionUID = 2;
    private final List<Float> temperatures = new ArrayList<>();
    private final HashMap<String, List<List<FieldSubscription>>> subscriptions = new HashMap<>();
    private OutputCollector collector;

    private String task;

    @Override
    public void prepare(Map<String, Object> topologyConfig, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.task = context.getThisComponentId();
        printTextToStream(formatTask(this.task), OUTPUT_STREAM);
    }

    String getPublicationField(Publication publication, String field) {
        switch (field) {
            case STATION_ID:
                return publication.getStationId();
            case TEMPERATURE:
                return publication.getTemperature();
            case CITY:
                return publication.getCity();
            case WIND_SPEED:
                return publication.getWindSpeed();
            case DIRECTION:
                return publication.getDirection();
            default:
                printTextToStream(formatInvalidField(field), ERROR_STREAM);
                System.exit(1);
        }
        return null;
    }

    @Override
    public void execute(Tuple input) {
        String sourceName = input.getSourceComponent();
        String sourceId = sourceName.substring(sourceName.length() - 1);

        Subscription subscription_data;

        if (sourceName.contains(SUBSCRIPTION)) {
            printTextToStream(formatEvent(this.task, sourceName, SUBSCRIPTION), OUTPUT_STREAM);

            try {
                subscription_data = Subscription.parseFrom(input.getBinaryByField(SUBSCRIPTION));
                List<FieldSubscription> subscription = convertFieldSubscriptions(subscription_data.getFieldSubscriptionsList());

                if (!subscriptions.containsKey(sourceId)) {
                    List<List<FieldSubscription>> newList = new ArrayList<>();
                    subscriptions.put(sourceId, newList);
                }

                List<List<FieldSubscription>> existingList = subscriptions.get(sourceId);
                existingList.add(subscription);
                subscriptions.put(sourceId, existingList);

            } catch (InvalidProtocolBufferException e) {
                printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
            }

        } else {
            printTextToStream(formatEvent(this.task, sourceName, PUBLICATION), OUTPUT_STREAM);

            long timestamp = Instant.now().toEpochMilli();
            long latency = timestamp - input.getLong(0);

            StatCounter.latency += latency;
            StatCounter.publicationsNumber++;

            if (this.temperatures.size() == WINDOW_SIZE)
                this.temperatures.clear();

            try {
                Publication publication_data = Publication.parseFrom(input.getBinaryByField(PUBLICATION_DATA));
                this.temperatures.add(Float.parseFloat(publication_data.getTemperature()));

                for (String boltId : subscriptions.keySet()) {
                    List<List<FieldSubscription>> listOfFieldSubscriptionsList = subscriptions.get(boltId);

                    for (List<FieldSubscription> fieldSubscriptionList : listOfFieldSubscriptionsList) {
                        boolean valid = true;

                        outerLoop:
                        for (FieldSubscription fieldSubscription : fieldSubscriptionList) {
                            switch (fieldSubscription.getOperator()) {
                                case "==": {
                                    String publication = getPublicationField(publication_data,
                                            fieldSubscription.getField());
                                    if (!publication.equals(fieldSubscription.getValue())) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case "!=": {
                                    String publication = getPublicationField(publication_data,
                                            fieldSubscription.getField());
                                    if (publication.equals(fieldSubscription.getValue())) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case "<": {
                                    float publication =
                                            Float.parseFloat(getPublicationField(publication_data,
                                                    fieldSubscription.getField()));
                                    float subscriptionValue =
                                            Float.parseFloat(fieldSubscription.getValue().toString());
                                    if (!(publication < subscriptionValue)) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case ">": {
                                    float publication =
                                            Float.parseFloat(getPublicationField(publication_data,
                                                    fieldSubscription.getField()));
                                    float subscriptionValue =
                                            Float.parseFloat(fieldSubscription.getValue().toString());
                                    if (!(publication > subscriptionValue)) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case "<=": {
                                    float publication =
                                            Float.parseFloat(getPublicationField(publication_data,
                                                    fieldSubscription.getField()));
                                    float subscriptionValue =
                                            Float.parseFloat(fieldSubscription.getValue().toString());
                                    if (!(publication <= subscriptionValue)) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case ">=": {
                                    float publication =
                                            Float.parseFloat(getPublicationField(publication_data,
                                                    fieldSubscription.getField()));
                                    float subscriptionValue =
                                            Float.parseFloat(fieldSubscription.getValue().toString());
                                    if (!(publication >= subscriptionValue)) {
                                        valid = false;
                                        break outerLoop;
                                    }
                                    break;
                                }

                                case "~":
                                    if (this.temperatures.size() < WINDOW_SIZE) {
                                        valid = false;
                                        break outerLoop;
                                    } else {
                                        float publication = 0;
                                        for (float temperature : this.temperatures) {
                                            publication += temperature;
                                        }
                                        publication /= temperatures.size();
                                        float subscriptionValue = Float
                                                .parseFloat(fieldSubscription.getValue().toString());
                                        if (!(publication == subscriptionValue)) {
                                            valid = false;
                                            break outerLoop;
                                        }
                                    }
                                    break;
                            }
                        }

                        if (valid) {
                            StatCounter.matchNumber++;

                            collector.emit(NOTIFIER + boltId, new Values(publication_data.toByteArray()));
                        }
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(NOTIFIER + "1", new Fields(NOTIFICATION_DATA));
        declarer.declareStream(NOTIFIER + "2", new Fields(NOTIFICATION_DATA));
        declarer.declareStream(NOTIFIER + "3", new Fields(NOTIFICATION_DATA));
    }
}
