package fii.ebs.utils;

import fii.ebs.generated.Subscription;
import fii.ebs.models.FieldSubscription;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Converter {
    public static List<FieldSubscription> convertFieldSubscriptions(List<Subscription.FieldSubscription> fieldSubscriptionList) {
        List<FieldSubscription> result = new ArrayList<>();

        for (Subscription.FieldSubscription sub : fieldSubscriptionList) {
            result.add(new FieldSubscription(sub.getKey(), sub.getOperator(), sub.getValue()));
        }

        return result;
    }
}
