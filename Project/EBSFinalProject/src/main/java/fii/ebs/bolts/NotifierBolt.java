package fii.ebs.bolts;

import com.google.protobuf.InvalidProtocolBufferException;
import fii.ebs.generated.Publication;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.consts.Constants.NOTIFICATION_DATA;
import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.PUBLICATION;
import static fii.ebs.utils.OutputPrinter.formatEvent;
import static fii.ebs.utils.OutputPrinter.formatTask;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

public class NotifierBolt extends BaseRichBolt {
    private static final long serialVersionUID = 3;
    private String task;

    @Override
    public void prepare(Map<String, Object> topologyConfig, TopologyContext context, OutputCollector collector) {
        this.task = context.getThisComponentId();
        printTextToStream(formatTask(this.task), OUTPUT_STREAM);
    }

    @Override
    public void execute(Tuple input) {
        try {
            Publication pub = Publication.parseFrom(input.getBinaryByField(NOTIFICATION_DATA));
            printTextToStream(formatEvent(this.task, pub.toString(), PUBLICATION), OUTPUT_STREAM);
        } catch (InvalidProtocolBufferException e) {
            printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
