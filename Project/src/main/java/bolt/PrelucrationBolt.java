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

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PrelucrationBolt extends BaseRichBolt {
    private OutputCollector collector;
    private AtomicReference<List<Publication>> publications = new AtomicReference<>(new ArrayList<>());
    private AtomicReference<List<Subscription>> subscriptions = new AtomicReference<>(new ArrayList<>());
    private AtomicReference<HashMap<List<Publication>, List<Subscription>>> test = new AtomicReference<>(new HashMap<>());

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

        HashMap<List<Publication>, List<Subscription>> test2 = new HashMap<>();
        for (int i = 0; i < publications.get().size(); i++) {
            if(publications.get().size() <= subscriptions.get().size())
                test2.put(Collections.singletonList(publications.get().get(i)), Collections.singletonList(subscriptions.get().get(i)));
        }
        test.set(test2);
        collector.emit("test",  new Values(test));

        // Confirmarea procesării tuplului
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        declarer.declareStream("test", new Fields("test"));
    }
}
