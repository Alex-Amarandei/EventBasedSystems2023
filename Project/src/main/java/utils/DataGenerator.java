package utils;



import models.Publication;

import java.io.Serializable;
import java.util.Random;

public class DataGenerator implements Serializable {
    private Random random;

    public DataGenerator() {
        random = new Random();
    }

    public Publication generatePublication() {
        Publication publication = new Publication();
        publication.setCity("Bucharest");
        publication.setTemperature(random.nextFloat() * 30);
        publication.setWindSpeed(random.nextFloat() * 20);
        // Alte câmpuri relevante pentru publicație

        return publication;
    }
}
