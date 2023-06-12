package utils;



import models.Publication;
import models.Subscription;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

public class DataGenerator implements Serializable {
    private Random random;

    String[] CITIES_IN_ROMANIA = {
            "Bucharest",
            "Cluj-Napoca",
            "Timișoara",
            "Iași",
            "Constanța",
            "Craiova",
            "Brașov",
            "Galați",
            "Oradea",
            "Ploiești",
            "Brăila",
            "Arad",
            "Pitești",
            "Sibiu",
            "Bacău",
            "Târgu Mureș",
            "Baia Mare",
            "Buzău",
            "Satu Mare",
            "Botoșani",
            "Râmnicu Vâlcea",
            "Suceava",
            "Focșani",
            "Târgoviște",
            "Tulcea",
            "Deva",
            "Reșița",
            "Slatina",
            "Giurgiu",
            "Târgu Jiu",
            "Hunedoara",
            "Roman",
            "Zalău",
            "Sfântu Gheorghe",
            "Călărași",
            "Alba Iulia",
            "Piatra Neamț",
            "Vaslui",
            "Făgăraș",
            "Turda",
            "Mediaș",
            "Sighetu Marmației",
            "Miercurea Ciuc",
            "Gheorgheni",
            "Bistrița",
            "Blaj",
            "Caracal",
            "Cugir",
            "Lugoj",
            "Petrila",
            "Motru",
            "Sebeș",
            "Odorheiu Secuiesc",
            "Lupeni",
            "Vulcan",
            "Panciu",
            "Onești",
            "Gura Humorului",
            "Toplița",
            "Baia Sprie",
            "Beiuș",
            "Câmpia Turzii",
            "Râșnov",
            "Zărnești",
            "Mioveni",
            "Buftea",
            "Caransebeș",
            "Cisnădie",
            "Comănești",
            "Curtea de Argeș",
            "Darabani",
            "Dorohoi",
            "Dragomirești",
            "Hârlău",
            "Jibou",
            "Luduș",
            "Marghita",
            "Năsăud",
            "Ocna Mureș",
            "Pecica",
            "Săcele",
            "Săliște",
            "Sângeorgiu de Pădure",
            "Tecuci",
            "Tismana",
            "Turtucaia",
            "Vicovu de Sus",
            };

    String[] DIRECTIONS = {"NE", "SE", "SW", "NW", "S", "N", "E", "W"};
    String[] OPERATORS = {">", "<", "==", ">=", "=<"};

//    This data is from Romania - 2022;
    int HIGHEST_TEMPERATURE_IN_ROMANIA = 39;
    int LOWEST_TEMPERATURE_IN_ROMANIA = -22;

    float HIGHEST_RAINFALL_VALUE_IN_ROMANIA = 1.1f;
    float LOWEST_RAINFALL_VALUE_IN_ROMANIA = 0.3f;

    int LOWEST_WIND_SPEED_IN_ROMANIA = 1;
    int HIGHEST_WIND_SPEED_IN_ROMANIA = 80;

    public DataGenerator() {
        random = new Random();
    }

    public Publication generatePublication() {
        Publication publication = new Publication();

        publication.setCity(CITIES_IN_ROMANIA[random.nextInt(CITIES_IN_ROMANIA.length)]);
        publication.setTemperature(random.nextFloat(LOWEST_TEMPERATURE_IN_ROMANIA, HIGHEST_TEMPERATURE_IN_ROMANIA));
        publication.setWindSpeed(random.nextFloat(LOWEST_WIND_SPEED_IN_ROMANIA, HIGHEST_WIND_SPEED_IN_ROMANIA));
        publication.setRain(random.nextFloat(LOWEST_RAINFALL_VALUE_IN_ROMANIA, HIGHEST_RAINFALL_VALUE_IN_ROMANIA));
        publication.setDirection(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
//        publication.setDateTime(
//                LocalDate.ofEpochDay(LocalDate.of(1990, 1, 1).toEpochDay() + random.nextInt((int) (LocalDate.of(2024, 1, 1).toEpochDay() - LocalDate.of(1990, 1, 1).toEpochDay())))
//        );

        return publication;
    }

    public Subscription generateSubscription(boolean city, boolean temperature, boolean rain, boolean windSpeed, boolean direction){
        Subscription subscription = new Subscription();

        if(city) {
            subscription.setCity(CITIES_IN_ROMANIA[random.nextInt(CITIES_IN_ROMANIA.length)]);
            subscription.setCityOperator(OPERATORS[random.nextInt(OPERATORS.length)]);
        }
        if(temperature) {
            subscription.setTemperature(random.nextFloat(LOWEST_TEMPERATURE_IN_ROMANIA, HIGHEST_TEMPERATURE_IN_ROMANIA));
            subscription.setTemperatureOperator(OPERATORS[random.nextInt(OPERATORS.length)]);
        }
        if(rain){
            subscription.setRain(random.nextFloat(LOWEST_RAINFALL_VALUE_IN_ROMANIA, HIGHEST_RAINFALL_VALUE_IN_ROMANIA));
            subscription.setRainOperator(OPERATORS[random.nextInt(OPERATORS.length)]);
        }
        if(windSpeed){
            subscription.setWindSpeed(random.nextFloat(LOWEST_WIND_SPEED_IN_ROMANIA, HIGHEST_WIND_SPEED_IN_ROMANIA));
            subscription.setWindSpeedOperator(OPERATORS[random.nextInt(OPERATORS.length)]);
        }
        if(direction){
            subscription.setDirection(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
            subscription.setDirectionOperator(OPERATORS[random.nextInt(OPERATORS.length)]);
        }


//        subscription.setDateTime(
//                LocalDate.ofEpochDay(LocalDate.of(1990, 1, 1).toEpochDay() + random.nextInt((int) (LocalDate.of(2024, 1, 1).toEpochDay() - LocalDate.of(1990, 1, 1).toEpochDay())))
//        );
//        subscription.setDateTimeOperator(OPERATORS[random.nextInt(OPERATORS.length)]);

        return subscription;
    }


}
