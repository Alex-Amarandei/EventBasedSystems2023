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

public class PublisherTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        // Definiți generatorul de date
        DataGenerator dataGenerator = new DataGenerator();

        // Definiți un emitator de publicații
        builder.setSpout("subscription-spout", new SubscriptionSpout(dataGenerator));
        builder.setSpout("publisher-spout", new PublisherSpout(dataGenerator));
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

        // Așteptați un anumit interval de timp (ex. 1 minut) pentru a rula topologia
        Utils.sleep(10000);

        // Opriți topologia și închideți clusterul
        cluster.killTopology("publisher-topology");
        cluster.shutdown();
    }
}
