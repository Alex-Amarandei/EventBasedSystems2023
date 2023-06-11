package bolt;

import models.Publication;
import models.Subscription;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PrelucrationBolt extends BaseRichBolt {
    private OutputCollector collector;
    private AtomicReference<List<Publication>> publications = new AtomicReference<>(new ArrayList<>());
    private AtomicReference<List<Subscription>> subscriptions = new AtomicReference<>(new ArrayList<>());

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String sourceComponent = input.getSourceComponent();

        if (sourceComponent.equals("subscription-spout")) {
            Subscription subscription = (Subscription) input.getValueByField("subscription");
            System.out.println("intra subscriptie");
            List<Subscription> updatedSubscriptions = new ArrayList<>(subscriptions.get());
            updatedSubscriptions.add(subscription);
            subscriptions.set(updatedSubscriptions);
        } else if (sourceComponent.equals("publisher-spout")) {
            Publication publication = (Publication) input.getValueByField("publication");
            System.out.println("intra publisher");
            List<Publication> updatedPublications = new ArrayList<>(publications.get());
            updatedPublications.add(publication);
            publications.set(updatedPublications);
        }
        collector.emit("subscriptions",  new Values(subscriptions));
        collector.emit("publications",  new Values(publications));

        // Confirmarea procesării tuplului
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        declarer.declareStream("subscriptions", new Fields("subscriptions"));
        declarer.declareStream("publications", new Fields("publications"));
    }
}
