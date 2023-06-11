package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Publication implements Serializable {
    private String City;
    private float Temperature;
    private float WindSpeed;
}
