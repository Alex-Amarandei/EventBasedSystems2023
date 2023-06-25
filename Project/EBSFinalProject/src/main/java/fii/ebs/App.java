package fii.ebs;

import fii.ebs.bolts.BrokerBolt;
import fii.ebs.bolts.NotifierBolt;
import fii.ebs.spouts.PublisherSpout;
import fii.ebs.spouts.SubscriptionSpout;
import fii.ebs.statistics.StatCounter;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static fii.ebs.consts.Constants.BROKER;
import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.consts.Constants.NOTIFIER;
import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.PUBLISHER_SPOUT;
import static fii.ebs.consts.Constants.SUBSCRIPTION;
import static fii.ebs.consts.Constants.THREE_MINUTES_IN_SECONDS;
import static fii.ebs.consts.Constants.TOPOLOGY_NAME;
import static fii.ebs.utils.OutputPrinter.formatAverageLatency;
import static fii.ebs.utils.OutputPrinter.formatAverageMatchRate;
import static fii.ebs.utils.OutputPrinter.formatNumberOfPublications;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

public class App {
    public static void main(String[] args) {
        try {
            TopologyBuilder builder = new TopologyBuilder();
            PublisherSpout publisher = new PublisherSpout();

            builder.setSpout(PUBLISHER_SPOUT, publisher);

            for (int i = 1; i <= 3; ++i) {
                builder.setSpout(SUBSCRIPTION + i, new SubscriptionSpout());
            }
            for (int i = 1; i <= 3; ++i) {
                builder.setBolt(BROKER + i, new BrokerBolt())
                        .allGrouping(PUBLISHER_SPOUT)
                        .shuffleGrouping(SUBSCRIPTION + "1", BROKER + i)
                        .shuffleGrouping(SUBSCRIPTION + "2", BROKER + i)
                        .shuffleGrouping(SUBSCRIPTION + "3", BROKER + i);
            }
            for (int i = 1; i <= 3; i++) {
                builder.setBolt(NOTIFIER + i, new NotifierBolt())
                        .shuffleGrouping(BROKER + "1", NOTIFIER + i)
                        .shuffleGrouping(BROKER + "2", NOTIFIER + i)
                        .shuffleGrouping(BROKER + "3", NOTIFIER + i);
            }


            Config config = new Config();

            LocalCluster cluster = new LocalCluster();
            StormTopology topology = builder.createTopology();

            config.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 1024);
            config.put(Config.TOPOLOGY_TRANSFER_BATCH_SIZE, 1);

            cluster.submitTopology(TOPOLOGY_NAME, config, topology);

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(THREE_MINUTES_IN_SECONDS));
            } catch (InterruptedException e) {
                printTextToStream(Arrays.toString(e.getStackTrace()), ERROR_STREAM);
            }

            cluster.killTopology(TOPOLOGY_NAME);
            cluster.shutdown();

            cluster.close();

            StatCounter.latency /= StatCounter.publicationsNumber;
            StatCounter.matchNumber /= StatCounter.publicationsNumber;

            printTextToStream(formatNumberOfPublications((int) StatCounter.publicationsNumber), OUTPUT_STREAM);
            printTextToStream(formatAverageLatency(StatCounter.latency), OUTPUT_STREAM);
            printTextToStream(formatAverageMatchRate(StatCounter.matchNumber), OUTPUT_STREAM);
        } catch (Exception ex) {
            printTextToStream(ex.getLocalizedMessage(), ERROR_STREAM);
        }
    }
}
