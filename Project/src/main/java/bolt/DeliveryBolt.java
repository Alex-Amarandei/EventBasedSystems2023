package bolt;

import models.Publication;
import models.Subscription;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DeliveryBolt extends BaseRichBolt {
    private OutputCollector collector;
    AtomicReference<HashMap<List<Publication>, List<Subscription>>> map;
    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("test"))
            map = (AtomicReference<HashMap<List<Publication>, List<Subscription>>>) input.getValueByField("test");
        for (Map.Entry<List<Publication>, List<Subscription>> entry : map.get().entrySet()) {
            List<Publication> publicationList = entry.getKey();
            List<Subscription> subscriptionList = entry.getValue();
            // Compararea publicațiilor cu subscriptiile
            for (Subscription subscription : subscriptionList){
                if(!subscription.isNull())
                    for (Publication publication : publicationList) {
                        // Comparare publicație cu subscriptie și luarea măsurilor necesare
                        if (
                        compareStrings(subscription.getCity(), publication.getCity()) &&
                        compareFloats(subscription.getTemperature(), subscription.getTemperatureOperator(), publication.getTemperature()) &&
                        compareFloats(subscription.getWindSpeed(), subscription.getWindSpeedOperator(), publication.getWindSpeed()) &&
                        compareFloats(subscription.getRain(), subscription.getRainOperator(), publication.getRain()) &&
                        compareStrings(subscription.getDirection(), publication.getDirection())
                        ) {
                            System.out.println("Match found between: ");
                            System.out.println(subscription);
                            System.out.println(publication);
                        }
                    }
            }
        }

            collector.ack(input);
    }

    private static boolean compareFloats(Float value, String operator, Float target) {
        if(value == null)
            return true;
        return switch (operator) {
            case "=" -> value.equals(target);
            case ">" -> value < target;
            case "<" -> value > target;
            case ">=" -> value >= target;
            case "<=" -> value <= target;
            default -> false;
        };
    }

    private static boolean compareStrings(String value, String target) {
        if(value == null)
            return true;
        return value.equals(target);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }


}