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

import java.util.Map;
import java.util.Random;

public class SubscriptionSpout extends BaseRichSpout {
    private final DataGenerator dataGenerator;
    private SpoutOutputCollector collector;
    private int currentSubscribors;
    private Random random = new Random();

    public SubscriptionSpout(DataGenerator dataGenerator){
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.currentSubscribors = 0;
    }

    @Override
    public void nextTuple() {
        if (this.currentSubscribors < 100) {
            Subscription subscription = dataGenerator.generateSubscription();

            collector.emit(new Values(Integer.toString(currentSubscribors), subscription));
            currentSubscribors++;
        }else{
            // Așteptați o perioadă de timp între emiteri
            Utils.sleep(100);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("subscriber", "subscription"));
    }

}
