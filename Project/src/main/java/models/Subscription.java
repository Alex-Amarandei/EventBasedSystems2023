package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Subscription implements Serializable {
    private String city;
    private String cityOperator;
    private float temperature;
    private String temperatureOperator;
    private float windSpeed;
    private String windSpeedOperator;
    private float rain;
    private String rainOperator;
    private String direction;
    private String directionOperator;
//    private LocalDate dateTime;
//    private String dateTimeOperator;
}
