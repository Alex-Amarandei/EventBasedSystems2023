package fii.ebs.utils;

import fii.ebs.generated.Subscription;
import fii.ebs.models.FieldSubscription;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fii.ebs.filters.EncryptionFilter.filterDecryptValue;

@UtilityClass
public class Converter {
    private static final String REGEX_PATTERN = "\"([^\"]*)\"";

    public static List<FieldSubscription> convertFieldSubscriptions(List<Subscription.FieldSubscription> fieldSubscriptionList) {
        List<FieldSubscription> result = new ArrayList<>();

        for (Subscription.FieldSubscription sub : fieldSubscriptionList) {
            result.add(new FieldSubscription(sub.getKey(), sub.getOperator(), sub.getValue()));
        }

        return result;
    }

    public static String decryptDictionaryFields(String input) {
        StringBuffer output = new StringBuffer();
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String encryptedText = matcher.group(1);
            String decryptedText = filterDecryptValue(encryptedText);
            matcher.appendReplacement(output, Matcher.quoteReplacement("\"" + decryptedText + "\""));
        }
        matcher.appendTail(output);

        return output.toString();
    }
}
