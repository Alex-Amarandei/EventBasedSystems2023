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
    AtomicReference<List<Publication>> receivedPublications;
    AtomicReference<List<Subscription>> receivedSubscriptions;
    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        if (input.getSourceStreamId().equals("publications")) {
            receivedPublications = (AtomicReference<List<Publication>>) input.getValueByField("publications");
            System.out.println("Received publications: " + receivedPublications);
        } else if (input.getSourceStreamId().equals("subscriptions")) {
            receivedSubscriptions = (AtomicReference<List<Subscription>>) input.getValueByField("subscriptions");
            System.out.println("Received subscriptions: " + receivedSubscriptions);
        }
        // Confirmarea procesării tuplului
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }


}