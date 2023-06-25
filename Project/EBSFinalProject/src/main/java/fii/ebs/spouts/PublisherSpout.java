package fii.ebs.spouts;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import fii.ebs.data.InputData;
import fii.ebs.data.PublicationData;
import fii.ebs.generated.Publication;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fii.ebs.consts.Constants.DATA_FILE_PATH;
import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.PUBLICATION_DATA;
import static fii.ebs.consts.Constants.TIMESTAMP;
import static fii.ebs.utils.OutputPrinter.formatTask;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

public class PublisherSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1;
    private final List<Object> valueList = new ArrayList<>();
    private SpoutOutputCollector collector;
    private int i = 0;

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        Gson gson = new Gson();

        try {
            JsonReader reader = new JsonReader(new FileReader(DATA_FILE_PATH));
            InputData data = gson.fromJson(reader, InputData.class);
            for (PublicationData pub : data.getPublications()) {
                this.valueList.add(Publication.newBuilder()
                        .setStationId(pub.getStationId())
                        .setCity(pub.getCity())
                        .setTemperature(pub.getTemperature())
                        .setWindSpeed(pub.getWindSpeed())
                        .setDirection(pub.getDirection())
                        .build()
                        .toByteArray());
            }

        } catch (FileNotFoundException e) {
            printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
        }

        String task = context.getThisComponentId();
        printTextToStream(formatTask(task), OUTPUT_STREAM);
    }

    @Override
    public void nextTuple() {
        if (this.i == this.valueList.size()) {
            i = 0;
        }
        long timestamp = Instant.now().toEpochMilli();
        this.collector.emit(new Values(timestamp, this.valueList.get(i++)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(TIMESTAMP, PUBLICATION_DATA));
    }
}
