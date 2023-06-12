package spout;

import models.Publication;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import utils.DataGenerator;

import java.util.Map;

public class PublisherSpout extends BaseRichSpout {
    private final DataGenerator dataGenerator;
    private SpoutOutputCollector collector;
    private int currentPublication;
    private int totalNumberOfPublication;

    public PublisherSpout(int totalNumberOfPublication, DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
        this.totalNumberOfPublication = totalNumberOfPublication;
    }

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.currentPublication = 0;
    }

    @Override
    public void nextTuple() {
        if (this.currentPublication < this.totalNumberOfPublication) {
            // Generați o publicație nouă
            Publication publication = dataGenerator.generatePublication();

            // Emiteți publicația în fluxul "publications"
            collector.emit(new Values(publication));
            currentPublication++;
        }else{
            // Așteptați o perioadă de timp între emiteri
            Utils.sleep(100);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("publication"));
    }
}