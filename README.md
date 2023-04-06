# EventBasedSystems2023

## Table of Contents

1. [Overview](#overview)
2. [Classes](#classes)
    1. [Publication](#publication)
    2. [Subscription](#subscription)
    3. [Values](#values)
3. [Utilities](#utilities)
    1. [Constants](#constants)
    2. [Pickers](#pickers)
4. [Threading](#threading)
5. [Results](#results)

### Overview

This project's purpose is to create a program that generates balanced sets of subscriptions and publications with the
option to set the total number of messages, frequency weights on subscription fields, and equality operator weights on
at least one field.

Publications have fixed fields, and parallelization is included for efficiency with an evaluation of
times obtained.

### Classes

#### Publication

```py
class Publication:
    station_id: int
    city: str
    temp: int
    rain: float
    wind: int
    direction: str
    date: datetime
```

This class defines a Publication object that contains information related to weather data.

It has attributes for the station ID, city, temperature, rainfall, wind speed, wind direction, and publication date. It
also has an `__init__` method to initialize the object with the specified attributes, and a `print_values` method to
print the values of the object to a file.

Additionally, the class has a static method called `create_random_publications()`, which generates a random Publication
object with randomly generated values for each attribute. The method takes in various parameters that define the ranges
and options for each attribute, such as the range of temperatures, range of rainfall, range of wind speeds, and a list
of possible directions. The method randomly generates a date within a given range and returns a Publication object with
the randomly generated attributes.

#### Subscription

```py
class Subscription:
    city: Values(str, str)
    temp: Values(str, str)
    rainfall: Values(str, str)
    wind: Values(str, str)
    direction: Values(str, str)
    date: Values(str, str)
```

The Subscription class provides functionality for creating and managing subscriptions for weather-related data.

The class has several attributes, including city, temp, rainfall, wind, direction, and date, which are all instances of
the Values class. The Values class is defined in a separate file named `values.py`.

The Subscription class has methods for setting operators and printing the subscription data.

The `set_operators` method takes an argument operator and randomly selects an attribute to add the operator to, if it
has not already been set.

The `print` method writes the subscription data to a file named subscriptions.txt.

The class also has a static method named `create_random_subscriptions` that generates a specified number of random
subscriptions based on the given input parameters. The parameters include the frequency of various weather-related
variables such as temperature, rainfall, wind speed, and directions, and operators such as equals_freq, less_than_freq,
more_than_freq, less_or_equal_to_freq, more_or_equal_to_freq.

#### Values

```py
class Values:
    field: str
    operator: str
```

The Values class has two properties: field and operator. The class constructor initializes these properties with values
passed as parameters.

The field property represents a field name, while the operator property represents an operator to be used with that
field.

The class can be used to create objects that store information about a particular field and its associated operator.

### Utilities

#### Constants

```py
CITIES_IN_ROMANIA,
DIRECTIONS,
OPERATORS,
HIGHEST_TEMPERATURE_IN_ROMANIA,
LOWEST_TEMPERATURE_IN_ROMANIA,
HIGHEST_RAINFALL_VALUE_IN_ROMANIA,
LOWEST_RAINFALL_VALUE_IN_ROMANIA,
LOWEST_WIND_SPEED_IN_ROMANIA,
HIGHEST_WIND_SPEED_IN_ROMANIA
```

There are several constants defined, namely:

- a list of cities in Romania
- a list of compass directions
- a list of comparison operators
- various climate-related values such as the highest and lowest temperatures, the highest and lowest rainfall values,
  and the lowest and highest wind speeds in Romania.

These constants are used for data analysis and processing purposes, and can be accessed throughout the program without
being modified.

#### Pickers

```py
def pick_random_from_range(lowest, highest)


def pick_random_float_from_range(lowest, highest)


def pick_random_value(array)
```

The function `pick_random_from_range` generates a random integer between the lowest and highest parameters, inclusive.

The function `pick_random_float_from_range` generates a random float between the lowest and highest parameters,
inclusive.

The function `pick_random_value` returns a random element from the array parameter.

### Threading

The code defines two functions that are meant to be run concurrently in two separate threads.

The first function, `thread_func_1`, takes in several arguments including the number of iterations, cities, and various
weather parameters such as lowest and highest temperature, rainfall, and wind speed.

It then creates a `Publication`
object using the `create_random_publication` method from the module called `publication`, passing in the weather
parameters and other arguments such as a unique ID for the publication.

Finally, the function calls the `print_values`
method on the Publication object, which prints out the weather data.

The second function, `thread_func_2`, takes in a similar set of arguments, and creates `Subscription` objects using
the `create_random_subscriptions` method from the module called `subscription`.

The method takes in similar weather parameters and other arguments to define the criteria for the subscription.

Both functions are meant to be run concurrently in separate threads.

### Results

#### No threads, frequencies add up

| Number of Iterations | Start Time      | End Time        | Time Difference |
|----------------------|-----------------|-----------------|-----------------|
| 1000                 | 16:31:44.340609 | 16:31:44.685609 | 0.345           |
| 10000                | 16:23:46.205249 | 16:23:49.340247 | 3.135           |
| 100000               | 16:35:03.686321 | 16:35:34.405120 | 30.718          |
| 500000               | 16:37:42.556727 | 16:40:16.633558 | 2:34.077        |

#### No threads, frequencies add above

| Number of Iterations | Start Time      | End Time        | Time Difference |
|----------------------|-----------------|-----------------|-----------------|
| 1000                 | 16:33:13.430340 | 16:33:13.765340 | 0.335           |
| 10000                | 16:28:54.528719 | 16:28:57.760719 | 3.232           |
| 100000               | 16:36:03.338579 | 16:36:33.743578 | 30.405          |
| 500000               | 16:51:49.701384 | 16:54:35.328612 | 2:45.627        |

#### Threads for Subscriptions and Publications, frequencies add up

| Number of Iterations | Start Time      | End Time        | Time Difference |
|----------------------|-----------------|-----------------|-----------------|
| 1000                 | 17:05:40.729659 | 17:05:41.087659 | 0.358           |
| 10000                | 17:08:39.933388 | 17:08:41.544387 | 1.611           |
| 100000               | 17:09:44.049725 | 17:09:58.777726 | 14.728          |
| 500000               | 17:11:24.382778 | 17:12:37.079778 | 1:12.697        |

#### Threads for Subscriptions and Publications, frequencies add above

| Number of Iterations | Start Time      | End Time        | Time Difference |
|----------------------|-----------------|-----------------|-----------------|
| 1000                 | 17:06:58.537665 | 17:06:58.875662 | 0.337           |
| 10000                | 17:09:04.682981 | 17:09:06.319979 | 1.636           |
| 100000               | 17:10:21.430723 | 17:10:36.589722 | 15.158          |
| 500000               | 17:15:07.163614 | 17:16:21.365614 | 1:14.202        |

