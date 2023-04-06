# Define a function to be run in the first thread
from publication import Publication
from subscription import Subscription


def thread_func_1(
        cities,
        lowest_temperature,
        highest_temperature,
        lowest_rainfall,
        highest_rainfall,
        lowest_wind,
        highest_wind,
        directions,
        days):
    for i in range(10):
        p = Publication.create_random_publications(
            i,
            cities,
            lowest_temperature,
            highest_temperature,
            lowest_rainfall,
            highest_rainfall,
            lowest_wind,
            highest_wind,
            directions,
            days
        )
        p.print_values()


# Define a function to be run in the second thread
def thread_func_2(number_of_subscriptions,
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
                  more_or_equal_to_freq, ):
    Subscription.create_random_subscriptions(
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
    )
