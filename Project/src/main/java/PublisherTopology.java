import bolt.EmitterBolt;
import bolt.WindSpeedBolt;
import models.Publication;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import spout.PublisherSpout;
import utils.DataGenerator;

public class PublisherTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        // Definiți generatorul de date
        DataGenerator dataGenerator = new DataGenerator();

        // Definiți un emitator de publicații
        builder.setSpout("publisher-spout", new PublisherSpout(dataGenerator));

        // Emitați publicațiile în fluxul "publications"
//        builder.setBolt("emitter-bolt", new EmitterBolt())
//                .allGrouping("publisher-spout");

        builder.setBolt("wind-speed-bolt", new WindSpeedBolt())
                .shuffleGrouping("publisher-spout");

        Config config = new Config();

        config.registerSerialization(Publication.class);

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
