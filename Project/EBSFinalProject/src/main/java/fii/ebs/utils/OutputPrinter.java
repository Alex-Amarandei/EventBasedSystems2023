package fii.ebs.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintStream;

import static fii.ebs.consts.Constants.ERROR_STREAM;
import static fii.ebs.utils.Converter.decryptDictionaryFields;

@UtilityClass
public class OutputPrinter {
    public static void printTextToStream(String text, PrintStream stream) {
        stream.println(text);

        if (stream.equals(ERROR_STREAM)) {
            System.exit(1);
        }
    }

    public static String formatTask(String task) {
        return String.format("##### STARTING TASK: %s%n", task);
    }

    public static String formatInvalidField(String field) {
        return String.format("INVALID FIELD <%s>%n", field);
    }

    public static String formatEvent(String task, String value, String event) {
        String textToPrint = String.format("#### %s GOT THE FOLLOWING %s:%n%s", task, event.toUpperCase(), value);

        if (value.contains(":")) {
            textToPrint = String.format("#### %s GOT THE FOLLOWING %s:%n%s", task, event.toUpperCase(), decryptDictionaryFields(value));
        }
        return textToPrint;
    }

    public static String formatNumberOfPublications(int numberOfPublications) {
        return String.format("### NUMBER OF PUBLICATIONS: %s%n", numberOfPublications);
    }

    public static String formatAverageLatency(float averageLatency) {
        return String.format("### AVERAGE LATENCY IN MILLISECONDS: %s%n", averageLatency);
    }

    public static String formatAverageMatchRate(float averageMatchRate) {
        return String.format("### AVERAGE MATCH RATE: %s%n", averageMatchRate);
    }
}
