package bolt;

import models.Publication;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class WindSpeedBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        // Procesați publicația primită
        Publication publication = (Publication) input.getValueByField("publication");
        if(publication.getWindSpeed() < 15.0) {
            System.out.println("WindSpeed publication: " + publication);

            // Emiteți publicația procesată către bolt-ul de livrare
            collector.emit(new Values(publication));
        }
        // Confirmarea procesării tuplului
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("publication"));
    }
}
