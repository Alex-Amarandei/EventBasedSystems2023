package bolt;

import models.Publication;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EmitterBolt extends BaseRichBolt {
    private OutputCollector collector;
    private List<Publication> publications;

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.publications = new ArrayList<>();
    }

    @Override
    public void execute(Tuple input) {
        // Procesați publicația primită
        Publication publication = (Publication) input.getValueByField("publication");
        System.out.println("Received publication: " + publication);
        publications.add((Publication) input.getValueByField("publication"));
        // Puteți face mai multe operații cu publicația aici

        // Confirmarea procesării tuplului
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {}

}