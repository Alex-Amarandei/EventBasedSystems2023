import secrets
from constants import CITIES_IN_ROMANIA, DIRECTIONS, STATION_ID, TEMPERATURE, WIND_SPEED

from boundaries import BOUNDARIES
from encryption import encrypt


def city_picker():
    return encrypt(collection_picker(CITIES_IN_ROMANIA))


def direction_picker():
    return encrypt(collection_picker(DIRECTIONS))


def station_id_picker():
    return encrypt(str(int_picker(BOUNDARIES[STATION_ID]["MIN"], BOUNDARIES[STATION_ID]["MAX"])))


def temperature_picker():
    return encrypt(str(int_picker(BOUNDARIES[TEMPERATURE]["MIN"], BOUNDARIES[TEMPERATURE]["MAX"])))


def wind_speed_picker():
    return encrypt(str(int_picker(BOUNDARIES[WIND_SPEED]["MIN"], BOUNDARIES[WIND_SPEED]["MAX"])))


def collection_picker(collection):
    return secrets.choice(collection)


def int_picker(low, high):
    return secrets.randbelow(high - low + 1) + low
