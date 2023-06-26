package fii.ebs.filters;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static fii.ebs.consts.Constants.OUTPUT_STREAM;
import static fii.ebs.consts.Constants.PYTHON_SCRIPT_PATH;
import static fii.ebs.utils.OutputPrinter.printTextToStream;

@UtilityClass
public class EncryptionFilter {
    public static String filterDecryptValue(String value) {
        try {
            String command = "python3 " + PYTHON_SCRIPT_PATH + " decrypt " + value;

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String decryptedValue = reader.readLine();
            reader.close();

            return decryptedValue;
        } catch (IOException e) {
            printTextToStream(Arrays.toString(e.getStackTrace()), OUTPUT_STREAM);
        }
        return "";
    }
}
