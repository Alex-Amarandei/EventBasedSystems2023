package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Publication implements Serializable {
    private String city;
    private Float temperature;
    private Float windSpeed;
    private Float rain;
    private String direction;
//    private LocalDate dateTime;
}
