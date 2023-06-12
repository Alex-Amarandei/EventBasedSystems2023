import bolt.DeliveryBolt;
import bolt.EmitterBolt;
import bolt.PrelucrationBolt;
import bolt.WindSpeedBolt;
import models.Publication;
import models.Subscription;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import spout.PublisherSpout;
import spout.SubscriptionSpout;
import utils.DataGenerator;

import java.util.HashMap;

public class PublisherTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        DataGenerator dataGenerator = new DataGenerator();

        builder.setSpout("subscription-spout", new SubscriptionSpout(1000, new HashMap<>() {{
            put("city", 0.93f);
            put("temperature", 0.23f);
            put("rain", 0.49f);
            put("wind", 0.16f);
            put("direction", 0.4426f);
        }}, dataGenerator));
        builder.setSpout("publisher-spout", new PublisherSpout(1000, dataGenerator));
        builder.setBolt("prelucration-bolt", new PrelucrationBolt())
                .allGrouping("subscription-spout")
                .allGrouping("publisher-spout");
        builder.setBolt("delivery-bolt", new DeliveryBolt())
                .shuffleGrouping("prelucration-bolt", "test");
        Config config = new Config();

        config.registerSerialization(Publication.class);
        config.registerSerialization(Subscription.class);
        config.setDebug(true);

        config.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE,1024);
        config.put(Config.TOPOLOGY_TRANSFER_BATCH_SIZE,1);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("publisher-topology", config, builder.createTopology());

        Utils.sleep(10000);

        cluster.killTopology("publisher-topology");
        cluster.shutdown();
    }
}
