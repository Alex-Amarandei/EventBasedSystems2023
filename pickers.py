import random


def pick_random_from_range(lowest, highest):
    return random.randint(lowest, highest)


def pick_random_float_from_range(lowest, highest):
    return random.uniform(lowest, highest)


def pick_random_value(array):
    return random.choice(array)
