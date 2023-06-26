package fii.ebs.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintStream;

@UtilityClass
public class OutputPrinter {
    public static void printTextToStream(String text, PrintStream stream) {
        stream.println(text);
    }

    public static String formatTask(String task) {
        return String.format("##### STARTING TASK: %s%n", task);
    }

    public static String formatInvalidField(String field) {
        return String.format("INVALID FIELD <%s>%n", field);
    }

    public static String formatEvent(String task, String value, String event) {
        return String.format("#### %s GOT THE FOLLOWING %s:%n%s", task, event.toUpperCase(), value);
    }

    public static String formatNumberOfPublications(int numberOfPublications) {
        return String.format("### NUMBER OF PUBLICATIONS: %s%n", numberOfPublications);
    }

    public static String formatAverageLatency(float averageLatency) {
        return String.format("### AVERAGE LATENCY IN MILLISECONDS: %s%n", averageLatency);
    }

    public static String formatAverageMatchRate(float averageMatchRate) {
        return String.format("### AVERAGE MATCH RATE: %s%n", averageMatchRate / 100);
    }
}
