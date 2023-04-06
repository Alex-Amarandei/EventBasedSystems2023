import random
from datetime import datetime, timedelta

from pickers import pick_random_value, pick_random_from_range, pick_random_float_from_range


class Publication:
    station_id: int
    city: str
    temp: int
    rain: float
    wind: int
    direction: str
    date: datetime

    def __init__(self, station_id, city, temp, rain, wind, direction, date):
        self.station_id = station_id
        self.city = city
        self.temp = temp
        self.rain = rain
        self.wind = wind
        self.direction = direction
        self.date = date

    def print_values(self):
        with open("publications.txt", "a", encoding="utf-8") as f:
            f.write(f"Station ID: {self.station_id}\n")
            f.write(f"City: {self.city}\n")
            f.write(f"Temperature: {self.temp} C\n")
            f.write(f"Rainfall: {self.rain} mm\n")
            f.write(f"Wind speed: {self.wind} km/h\n")
            f.write(f"Wind direction: {self.direction}\n")
            f.write(f"Publication date: {self.date}\n")
            f.write("\n")

    @staticmethod
    def create_random_publications(
            station_id,
            cities,
            lowest_temperature,
            highest_temperature,
            lowest_rainfall,
            highest_rainfall,
            lowest_wind_speed,
            highest_wind_speed,
            directions,
            days_before,
    ):
        # We generate a random date
        start_date = datetime.now() - timedelta(days=days_before)
        end_date = datetime.now()
        random_date = start_date + timedelta(
            days=random.randrange((end_date - start_date).days)
        )
        # format to only include day, month, and year
        formatted_date = random_date.strftime("%d.%m.%Y")

        return Publication(
            station_id,
            pick_random_value(cities),
            pick_random_from_range(lowest_temperature, highest_temperature),
            pick_random_float_from_range(lowest_rainfall, highest_rainfall),
            pick_random_from_range(lowest_wind_speed, highest_wind_speed),
            pick_random_value(directions),
            formatted_date,
        )
