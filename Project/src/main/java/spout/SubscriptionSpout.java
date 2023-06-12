package spout;

import models.Publication;
import models.Subscription;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import utils.DataGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubscriptionSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    int totalSubscriptions;
    int currentSubscriptions;
    int numberOfCities;
    int numberOfTemperatures;
    int numberOfWindSpeeds;
    int numberOfRainValues;
    int numberOfDirections;
    DataGenerator dataGenerator;

    public SubscriptionSpout(int totalSubscriptions, HashMap<String, Float> freq, DataGenerator dataGenerator){
        this.totalSubscriptions = totalSubscriptions;
        this.currentSubscriptions = 0;
        this.dataGenerator = dataGenerator;
        for (Map.Entry<String, Float> entry : freq.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            switch (key) {
                case "city":
                    numberOfCities = (int) (totalSubscriptions * value);
                    break;
                case "temperature":
                    numberOfTemperatures = (int) (totalSubscriptions * value);
                    break;
                case "rain":
                    numberOfRainValues = (int) (totalSubscriptions * value);
                    break;
                case "wind":
                    numberOfWindSpeeds = (int) (totalSubscriptions * value);
                    break;
                case "direction":
                    numberOfDirections = (int) (totalSubscriptions * value);
                    break;
                default:
                    return;
            }
        }
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        if (this.currentSubscriptions < this.totalSubscriptions) {
            System.out.println(numberOfCities + " " + numberOfTemperatures + " " + numberOfRainValues);
            Subscription subscription = dataGenerator.generateSubscription(
                    numberOfCities-- > 0,
                    numberOfTemperatures-- > 0,
                    numberOfRainValues-- > 0,
                    numberOfWindSpeeds-- > 0,
                    numberOfDirections-- > 0
            );
            System.out.println(numberOfCities + " " + numberOfTemperatures + " " + numberOfRainValues);
            this.currentSubscriptions++;
            collector.emit(new Values(subscription));
        }else{
            // Așteptați o perioadă de timp între emiteri
            Utils.sleep(100);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("subscription"));
    }

}
