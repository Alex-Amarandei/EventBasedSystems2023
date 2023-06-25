import json
import math
import multiprocessing
import random
import time

from constants import (
    CONFIG_FILE_PATH,
    FIELD,
    FREQUENCY,
    INDEX,
    OPERATOR,
    PERCENT,
    PUB,
    PUBLICATIONS,
    RESTRICTIONS,
    RESULTS_FILE_PATH,
    SUB,
    SUBSCRIPTIONS,
    TYPE,
    ALL_OPERATORS,
    EQUALITY_OPERATORS
)
from generators import generate
from pickers import (
    city_picker,
    direction_picker,
    int_picker,
    station_id_picker,
    temperature_picker,
    wind_speed_picker,
)
from workers import worker_1, worker_2

FIELDS = {
    "stationId": station_id_picker,
    "city": city_picker,
    "temperature": temperature_picker,
    "windSpeed": wind_speed_picker,
    "direction": direction_picker,
}

OPERATORS = {
    "stationId": ALL_OPERATORS,
    "city": EQUALITY_OPERATORS,
    "temperature": ALL_OPERATORS,
    "windSpeed": ALL_OPERATORS,
    "direction": EQUALITY_OPERATORS,
}


def main():
    start_time = time.time()

    with open(CONFIG_FILE_PATH) as config_file:
        config = json.load(config_file)

    to_generate = [{} for _ in range(config[PUBLICATIONS] + config[SUBSCRIPTIONS])]
    results = [{} for _ in range(config[PUBLICATIONS] + config[SUBSCRIPTIONS])]
    publication_indexes = [i for i in range(config[PUBLICATIONS])]
    subscription_indexes = [
        i
        for i in range(
            config[PUBLICATIONS], config[PUBLICATIONS] + config[SUBSCRIPTIONS]
        )
    ]

    for index in publication_indexes:
        to_generate[index][INDEX] = index
        to_generate[index][TYPE] = PUB

    for index in subscription_indexes:
        to_generate[index][INDEX] = index
        to_generate[index][TYPE] = SUB
        to_generate[index][RESTRICTIONS] = []

    fields = list(FIELDS.keys())

    for restriction in config[RESTRICTIONS]:
        if restriction[TYPE] == OPERATOR:
            continue
        percent = int(restriction[PERCENT][1:])
        if restriction[PERCENT][0] == "<":
            percent = int_picker(0, percent - 1)
        elif restriction[PERCENT][0] == ">":
            percent = int_picker(percent + 1, 100)
        indexes = random.sample(
            subscription_indexes, math.ceil(percent * len(subscription_indexes) / 100)
        )
        q_constaint = {FIELD: restriction[FIELD], OPERATOR: None}

        if restriction[FIELD] in fields:
            fields.remove(restriction[FIELD])

        for index in indexes:
            to_generate[index][RESTRICTIONS].append(q_constaint)

    input_queue = multiprocessing.JoinableQueue()
    output_queue = multiprocessing.Manager().Queue()
    for i in to_generate:
        input_queue.put(i)

    generate(input_queue, output_queue, worker_1, results, fields, FIELDS, config)

    field_indexes = {}
    operators = {}
    for field in FIELDS.keys():
        field_indexes[field] = []
        operators[field] = OPERATORS[field]

    for index in subscription_indexes:
        for field in results[index]:
            field_indexes[field].append(index)
        to_generate[index][RESTRICTIONS] = []

    for restriction in config[RESTRICTIONS]:
        if restriction[TYPE] == FREQUENCY:
            continue
        percent = int(restriction[PERCENT][1:])
        if restriction[PERCENT][0] == "<":
            percent = int_picker(0, percent - 1)
        elif restriction[PERCENT][0] == ">":
            percent = int_picker(percent + 1, 100)
        indexes = random.sample(
            field_indexes[restriction[FIELD]],
            math.ceil(percent * len(field_indexes[restriction[FIELD]]) / 100),
        )
        q_constaint = {
            FIELD: restriction[FIELD],
            OPERATOR: restriction[OPERATOR],
        }

        if restriction[OPERATOR] in operators[restriction[FIELD]]:
            operators[restriction[FIELD]].remove(restriction[OPERATOR])

        for index in indexes:
            to_generate[index][RESTRICTIONS].append(q_constaint)

    input_queue = multiprocessing.JoinableQueue()
    output_queue = multiprocessing.Manager().Queue()
    for index in subscription_indexes:
        input_queue.put(to_generate[index])

    generate(input_queue, output_queue, worker_2, results, operators, FIELDS, config)

    final_results = {
        PUBLICATIONS: results[: config[PUBLICATIONS]],
        SUBSCRIPTIONS: results[config[PUBLICATIONS]:],
    }

    end_time = time.time()

    print("Time elapsed: ", end_time - start_time)

    with open(RESULTS_FILE_PATH, "w") as results_file:
        json.dump(final_results, results_file, indent=4)


if __name__ == "__main__":
    main()
