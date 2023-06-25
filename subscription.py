import random
from datetime import datetime, timedelta

from constants import OPERATORS
from pickers import (
    pick_random_value,
    pick_random_from_range,
    pick_random_float_from_range,
)
from values import Values


class Subscription:
    city: Values(str, str)
    temp: Values(str, str)
    rainfall: Values(str, str)
    wind: Values(str, str)
    direction: Values(str, str)
    date: Values(str, str)

    def __init__(self):
        self.city = Values(None, None)
        self.temp = Values(None, None)
        self.rainfall = Values(None, None)
        self.wind = Values(None, None)
        self.direction = Values(None, None)
        self.date = Values(None, None)

    def print(self):
        with open("subscriptions.txt", "a", encoding="utf-8") as f:
            if self.city.field is not None:
                f.write(
                    f"City: {self.city.field}"
                    + f" with operator {self.city.operator}\n"
                )
            if self.temp.field is not None:
                f.write(
                    f"Temperature: {self.temp.field} C"
                    + f" with operator {self.temp.operator}\n"
                )
            if self.rainfall.field is not None:
                f.write(
                    f"Rainfall: {self.rainfall.field} mm"
                    + f" with operator {self.rainfall.operator}\n"
                )
            if self.wind.field is not None:
                f.write(
                    f"Wind speed: {self.wind.field} km/h"
                    + f" with operator {self.wind.operator}\n"
                )
            if self.direction.field is not None:
                f.write(
                    f"Wind direction: {self.direction.field}"
                    + f" with operator {self.direction.operator}\n"
                )
            if self.date.field is not None:
                f.write(
                    f"Publication date: {self.date.field}"
                    + f" with operator {self.date.operator}\n"
                )
            f.write("\n")

    def can_add_operator(self):
        fields = ["city", "temp", "rainfall", "wind", "direction", "date"]
        if self.city.field is None or self.city.operator is not None:
            fields.remove("city")
        if self.temp.field is None or self.temp.operator is not None:
            fields.remove("temp")
        if self.rainfall.field is None or self.rainfall.operator is not None:
            fields.remove("rainfall")
        if self.wind.field is None or self.wind.operator is not None:
            fields.remove("wind")
        if self.direction.field is None or self.direction.operator is not None:
            fields.remove("direction")
        if self.date.field is None or self.date.operator is not None:
            fields.remove("date")
        return fields

    def set_operators(self, operator: str):
        # Create a list of field names
        fields = ["city", "temp", "rainfall", "wind", "direction", "date"]

        # Pick a random field name from the list
        while True:
            field = random.choice(fields)
            # Do something with the field
            if field == "city":
                if self.city.field is not None and self.city.operator is None:
                    self.city.operator = operator
                    break
                fields.remove("city")
            elif field == "temp":
                if self.temp.field is not None and self.temp.operator is None:
                    self.temp.operator = operator
                    break
                fields.remove("temp")
            elif field == "rainfall":
                if self.rainfall.field is not None and self.rainfall.operator is None:
                    self.rainfall.operator = operator
                    break
                fields.remove("rainfall")
            elif field == "wind":
                if self.wind.field is not None and self.wind.operator is None:
                    self.wind.operator = operator
                    break
                fields.remove("wind")
            elif field == "direction":
                if self.direction.field is not None and self.direction.operator is None:
                    self.direction.operator = operator
                    break
                fields.remove("direction")
            elif field == "date":
                if self.date.field is not None and self.date.operator is None:
                    self.date.operator = operator
                    break
                fields.remove("date")

    @staticmethod
    def create_random_subscriptions(
        number_of_subscriptions,
        cities,
        cities_freq,
        lowest_temperature,
        highest_temperature,
        temperature_freq,
        lowest_rainfall,
        highest_rainfall,
        rainfall_freq,
        lowest_wind_speed,
        highest_wind_speed,
        wind_speed_freq,
        directions,
        direction_freq,
        days_before,
        days_before_freq,
        equals_freq,
        less_than_freq,
        more_than_freq,
        less_or_equal_to_freq,
        more_or_equal_to_freq,
    ):
        list_of_subscriptions = []

        for i in range(number_of_subscriptions):
            subscription = Subscription()

            if cities_freq > 0:
                subscription.city.field = pick_random_value(cities)
                cities_freq = cities_freq - 1

            elif temperature_freq > 0:
                subscription.temp.field = pick_random_from_range(
                    lowest_temperature, highest_temperature
                )
                temperature_freq = temperature_freq - 1

            elif rainfall_freq > 0:
                subscription.rainfall.field = pick_random_float_from_range(
                    lowest_rainfall, highest_rainfall
                )
                rainfall_freq = rainfall_freq - 1

            elif wind_speed_freq > 0:
                subscription.wind.field = pick_random_from_range(
                    lowest_wind_speed, highest_wind_speed
                )
                wind_speed_freq = wind_speed_freq - 1

            elif direction_freq > 0:
                subscription.direction.field = pick_random_value(directions)
                direction_freq = direction_freq - 1

            elif days_before_freq > 0:
                start_date = datetime.now() - timedelta(days=days_before)
                end_date = datetime.now()
                random_date = start_date + timedelta(
                    days=random.randint(range(0, (end_date - start_date).days))
                )

                # format to only include day, month, and year
                formatted_date = random_date.strftime("%d.%m.%Y")
                subscription.date.field = formatted_date
                days_before_freq = days_before_freq - 1
            list_of_subscriptions.append(subscription)

        index = 0

        while (
            cities_freq != 0
            or temperature_freq != 0
            or rainfall_freq != 0
            or wind_speed_freq != 0
            or direction_freq != 0
            or days_before_freq != 0
        ):
            index = index % number_of_subscriptions

            if cities_freq > 0:
                list_of_subscriptions[index].city.field = pick_random_value(cities)
                cities_freq = cities_freq - 1

            elif temperature_freq > 0:
                list_of_subscriptions[index].temp.field = pick_random_from_range(
                    lowest_temperature, highest_temperature
                )
                temperature_freq = temperature_freq - 1

            elif rainfall_freq > 0:
                list_of_subscriptions[
                    index
                ].rainfall.field = pick_random_float_from_range(
                    lowest_rainfall, highest_rainfall
                )
                rainfall_freq = rainfall_freq - 1

            elif wind_speed_freq > 0:
                list_of_subscriptions[index].wind.field = pick_random_from_range(
                    lowest_wind_speed, highest_wind_speed
                )
                wind_speed_freq = wind_speed_freq - 1

            elif direction_freq > 0:
                list_of_subscriptions[index].direction.field = pick_random_value(
                    directions
                )
                direction_freq = direction_freq - 1

            elif days_before_freq > 0:
                start_date = datetime.now() - timedelta(days=days_before)
                end_date = datetime.now()
                random_date = start_date + timedelta(
                    days=random.randint(range((end_date - start_date).days))
                )
                # format to only include day, month, and year
                formatted_date = random_date.strftime("%d.%m.%Y")
                list_of_subscriptions[index].date.field = formatted_date
                days_before_freq = days_before_freq - 1

            index = index + 1

        index = 0

        while (
            equals_freq != 0
            or less_than_freq != 0
            or less_or_equal_to_freq != 0
            or more_than_freq != 0
            or more_or_equal_to_freq != 0
        ):
            index = index % number_of_subscriptions

            if (
                equals_freq > 0
                and len(list_of_subscriptions[index].can_add_operator()) > 0
            ):
                list_of_subscriptions[index].set_operators(OPERATORS[0])
                equals_freq = equals_freq - 1

            elif (
                less_than_freq > 0
                and len(list_of_subscriptions[index].can_add_operator()) > 0
            ):
                list_of_subscriptions[index].set_operators(OPERATORS[1])
                less_than_freq = less_than_freq - 1

            elif (
                less_or_equal_to_freq > 0
                and len(list_of_subscriptions[index].can_add_operator()) > 0
            ):
                list_of_subscriptions[index].set_operators(OPERATORS[2])
                less_or_equal_to_freq = less_or_equal_to_freq - 1

            elif (
                more_than_freq > 0
                and len(list_of_subscriptions[index].can_add_operator()) > 0
            ):
                list_of_subscriptions[index].set_operators(OPERATORS[3])
                more_than_freq = more_than_freq - 1

            elif (
                more_or_equal_to_freq > 0
                and len(list_of_subscriptions[index].can_add_operator()) > 0
            ):
                list_of_subscriptions[index].set_operators(OPERATORS[4])
                more_or_equal_to_freq = more_or_equal_to_freq - 1

            index = index + 1

        for i in range(number_of_subscriptions):
            list_of_subscriptions[i].print()
