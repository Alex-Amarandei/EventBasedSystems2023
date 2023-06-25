package fii.ebs.spouts;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import fii.ebs.data.InputData;
import fii.ebs.data.SubscriptionData;
import fii.ebs.generated.Subscription;
import fii.ebs.models.FieldSubscription;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import static fii.ebs.consts.Constants.BROKER;
import static fii.ebs.consts.Constants.DATA_FILE_PATH;
import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.SUBSCRIPTION;
import static fii.ebs.utils.OutputPrinter.formatTask;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

public class SubscriptionSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1;
    private final ArrayList<ArrayList<FieldSubscription>> subscriptionList = new ArrayList<>();
    private SpoutOutputCollector collector;
    private int i = 0;

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        String task = context.getThisComponentId();
        int currentId = Integer.parseInt(task.substring(task.length() - 1));
        Gson gson = new Gson();

        try {
            JsonReader reader = new JsonReader(new FileReader(DATA_FILE_PATH));
            InputData data = gson.fromJson(reader, InputData.class);
            for (int i = 0; i < data.getSubscriptions().size(); i++) {
                if (i % 3 != currentId - 1)
                    continue;

                Map<String, SubscriptionData> subscriptionDataMap = data.getSubscriptions().get(i);
                ArrayList<FieldSubscription> subscription = new ArrayList<>();
                for (String key : subscriptionDataMap.keySet()) {
                    subscription.add(new FieldSubscription(
                            key,
                            subscriptionDataMap.get(key).getOperator(),
                            subscriptionDataMap.get(key).getValue()));
                }
                subscriptionList.add(subscription);
            }
        } catch (FileNotFoundException e) {
            printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
        }

        printTextToStream(formatTask(task), OUTPUT_STREAM);
    }

    @Override
    public void nextTuple() {
        if (i == this.subscriptionList.size())
            return;

        Subscription.Builder subscriptionBuilder = Subscription.newBuilder();
        for (FieldSubscription fieldSubscription : subscriptionList.get(i)) {
            if (fieldSubscription.getValue() == null)
                continue;

            Subscription.FieldSubscription.Builder fieldBuilder = Subscription.FieldSubscription.newBuilder();

            fieldBuilder.setKey(fieldSubscription.getField());
            fieldBuilder.setOperator(fieldSubscription.getOperator());
            fieldBuilder.setValue(fieldSubscription.getValue().toString());

            subscriptionBuilder.addFieldSubscriptions(fieldBuilder.build());
        }

        Subscription sub = subscriptionBuilder.build();
        this.collector.emit(BROKER + (i % 3 + 1), new Values(sub.toByteArray()));
        i++;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(BROKER + "1", new Fields(SUBSCRIPTION));
        declarer.declareStream(BROKER + "2", new Fields(SUBSCRIPTION));
        declarer.declareStream(BROKER + "3", new Fields(SUBSCRIPTION));
    }
}
