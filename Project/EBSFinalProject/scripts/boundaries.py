from constants import STATION_ID, TEMPERATURE, WIND_SPEED

BOUNDARIES = {
    STATION_ID: {"MIN": 1, "MAX": 100},
    TEMPERATURE: {"MIN": -40, "MAX": 45},
    WIND_SPEED: {"MIN": 0, "MAX": 126},
}

# The minimum temperature was recorded in Brasov, Brasov County, in 1942
# The maximum temperature was recorded in Ion Sion, Braila County, in 1951
# The maximum wind speed was recorded in Bucharest, Ilfov County, in 1954
