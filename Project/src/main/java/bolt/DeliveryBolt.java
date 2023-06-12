package bolt;

import models.Publication;
import models.Subscription;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.io.FileWriter;
import java.io.IOException;
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
            System.out.println("Received publications: " + map);

        for (Map.Entry<List<Publication>, List<Subscription>> entry : map.get().entrySet()) {
            List<Publication> publicationList = entry.getKey();
            List<Subscription> subscriptionList = entry.getValue();

            // Compararea publicațiilor cu subscriptiile
            for (Subscription subscription : subscriptionList){
                for (Publication publication : publicationList) {
                    // Comparare publicație cu subscriptie și luarea măsurilor necesare
                    if (compareFloats(subscription.getWindSpeed(), subscription.getWindSpeedOperator(), publication.getWindSpeed())) {
                        System.out.println("Match found between: ");
                        System.out.println(subscription);
                        System.out.println(publication);
                    }
                }
            }
        }

            collector.ack(input);
    }

    private static boolean compareFloats(float value, String operator, float target) {
        return switch (operator) {
            case "=" -> value == target;
            case ">" -> value < target;
            case "<" -> value > target;
            case ">=" -> value >= target;
            case "<=" -> value <= target;
            default -> false;
        };
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }


}