import threading

import custom_threading
from constants import CITIES_IN_ROMANIA, LOWEST_TEMPERATURE_IN_ROMANIA, HIGHEST_TEMPERATURE_IN_ROMANIA, \
    LOWEST_RAINFALL_VALUE_IN_ROMANIA, HIGHEST_RAINFALL_VALUE_IN_ROMANIA, LOWEST_WIND_SPEED_IN_ROMANIA, \
    HIGHEST_WIND_SPEED_IN_ROMANIA, DIRECTIONS


def main(publication_params, subscription_params):
    # Create two threads and start them
    thread1 = threading.Thread(target=custom_threading.thread_func_1(**publication_params))

    thread2 = threading.Thread(target=custom_threading.thread_func_2(**subscription_params))

    thread1.start()
    thread2.start()

    # Wait for both threads to finish
    thread1.join()
    thread2.join()


if __name__ == "__main__":
    number_of_iterations = 10

    publication_params_dict = {
        "number_of_iterations": number_of_iterations,
        "cities": CITIES_IN_ROMANIA,
        "lowest_temperature": LOWEST_TEMPERATURE_IN_ROMANIA,
        "highest_temperature": HIGHEST_TEMPERATURE_IN_ROMANIA,
        "lowest_rainfall": LOWEST_RAINFALL_VALUE_IN_ROMANIA,
        "highest_rainfall": HIGHEST_RAINFALL_VALUE_IN_ROMANIA,
        "lowest_wind": LOWEST_WIND_SPEED_IN_ROMANIA,
        "highest_wind": HIGHEST_WIND_SPEED_IN_ROMANIA,
        "directions": DIRECTIONS,
        "days": 365,
    }

    subscription_params_dict = {
        "number_of_subscriptions": number_of_iterations,
        "cities": CITIES_IN_ROMANIA,
        "cities_freq": 8,
        "lowest_temperature": LOWEST_TEMPERATURE_IN_ROMANIA,
        "highest_temperature": HIGHEST_TEMPERATURE_IN_ROMANIA,
        "temperature_freq": 5,
        "lowest_rainfall": LOWEST_RAINFALL_VALUE_IN_ROMANIA,
        "highest_rainfall": HIGHEST_RAINFALL_VALUE_IN_ROMANIA,
        "rainfall_freq": 0,
        "lowest_wind_speed": LOWEST_WIND_SPEED_IN_ROMANIA,
        "highest_wind_speed": HIGHEST_WIND_SPEED_IN_ROMANIA,
        "wind_speed_freq": 1,
        "directions": DIRECTIONS,
        "direction_freq": 1,
        "days_before": 365,
        "days_before_freq": 1,
        # sum of freq above this line must be greater or equal to number_of_iterations
        # sum of freq below this line must also be greater or equal to number_of_iterations
        "equals_freq": 7,
        "less_than_freq": 5,
        "more_than_freq": 1,
        "less_or_equal_to_freq": 1,
        "more_or_equal_to_freq": 1,
    }

    main(publication_params_dict, subscription_params_dict)
