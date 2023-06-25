package fii.ebs.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InputData {
    private List<PublicationData> publications;
    private List<Map<String, SubscriptionData>> subscriptions;
}
