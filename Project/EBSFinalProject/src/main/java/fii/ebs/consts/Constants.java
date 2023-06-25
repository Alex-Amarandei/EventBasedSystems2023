package fii.ebs.consts;

import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static fii.ebs.utils.OutputPrinter.printTextToStream;

@UtilityClass
public class Constants {
    public static final String TOPOLOGY_NAME = "EBS_Topology";

    public static final int WINDOW_SIZE = 5;
    public static final String TIMESTAMP = "timestamp";

    public static final String BROKER = "broker";
    public static final String NOTIFIER = "notifier";
    public static final String SUBSCRIPTION = "subscription";
    public static final String PUBLICATION = "publication";

    public static final String PUBLICATION_DATA = "publication_data";
    public static final String NOTIFICATION_DATA = "notification_data";

    public static final String PUBLISHER_SPOUT = "publisher_spout";

    public static final String DATA_FILE_PATH = "/Users/alexwama/Files/Github/EventBasedSystems2023/project/EBSFinalProject/data/results" +
            ".json";

    public static final String STATION_ID = "stationId";
    public static final String TEMPERATURE = "temperature";
    public static final String CITY = "city";
    public static final String WIND_SPEED = "windSpeed";
    public static final String DIRECTION = "direction";

    public static final PrintStream ERROR_STREAM = System.err;
    public static final PrintStream OUTPUT_STREAM = System.out;
    public static final PrintStream FILE_STREAM;

    public static final int THREE_MINUTES_IN_SECONDS = 180;

    static {
        try {
            FILE_STREAM = new PrintStream(new FileOutputStream("execution.txt", true));
        } catch (FileNotFoundException e) {
            printTextToStream(e.getLocalizedMessage(), ERROR_STREAM);
            throw new RuntimeException(e);
        }
    }


}
