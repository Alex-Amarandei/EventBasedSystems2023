import multiprocessing
import random

from constants import FIELD, OPERATOR, THREADS, VALUE
from pickers import collection_picker, int_picker


def generate_publication(fields):
    return {field: fields[field]() for field in fields}


def generate_subscription(restrictions, allowed_fields, fields):
    sample = random.sample(allowed_fields, int_picker(1, len(allowed_fields)))
    subscription = {field: {OPERATOR: None, VALUE: fields[field]()} for field in sample}
    subscription.update(
        {
            restriction[FIELD]: {
                OPERATOR: restriction[OPERATOR],
                VALUE: fields[restriction[FIELD]](),
            }
            for restriction in restrictions
        }
    )
    return subscription


def update_subscription(subscription, restrictions, operators):
    subscription.update(
        {
            restriction[FIELD]: {OPERATOR: restriction[OPERATOR]}
            for restriction in restrictions
        }
    )
    for field in subscription:
        if subscription[field][OPERATOR] is None:
            subscription[field][OPERATOR] = collection_picker(operators[field])
    return subscription


def generate(input_queue, output_queue, worker, results, allowed, fields, config):
    threads = []
    for _ in range(config[THREADS]):
        x = multiprocessing.Process(
            target=worker, args=(input_queue, output_queue, results, allowed, fields)
        )
        threads.append(x)
        x.start()

    input_queue.join()

    while not output_queue.empty():
        item = output_queue.get()
        results[item[0]] = item[1]
