package fii.ebs.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicationData {
    private String stationId;
    private String city;
    private String temperature;
    private String windSpeed;
    private String direction;
}
