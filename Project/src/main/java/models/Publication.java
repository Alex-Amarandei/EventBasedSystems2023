package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Publication implements Serializable {
    private String city;
    private float temperature;
    private float windSpeed;
    private float rain;
    private String direction;
//    private LocalDate dateTime;
}
