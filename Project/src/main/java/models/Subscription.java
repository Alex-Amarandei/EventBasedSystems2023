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
    private Float temperature;
    private String temperatureOperator;
    private Float windSpeed;
    private String windSpeedOperator;
    private Float rain;
    private String rainOperator;
    private String direction;
    private String directionOperator;
//    private LocalDate dateTime;
//    private String dateTimeOperator;

    public boolean isNull(){
        return  city == null &&
                temperature == null &&
                windSpeed == null &&
                rain == null &&
                direction == null;
    }
}
