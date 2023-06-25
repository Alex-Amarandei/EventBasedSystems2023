from constants import TYPE, PUB, INDEX, SUB, RESTRICTIONS
from generators import (
    generate_publication,
    generate_subscription,
    update_subscription,
)


def worker_1(input_queue, output_queue, _, allowed, fields):
    while not input_queue.empty():
        item = input_queue.get()
        if item[TYPE] == PUB:
            output_queue.put((item[INDEX], generate_publication(fields)))
        else:
            output_queue.put(
                (
                    item[INDEX],
                    generate_subscription(item[RESTRICTIONS], allowed, fields),
                )
            )
        input_queue.task_done()


def worker_2(input_queue, output_queue, results, allowed, _):
    while not input_queue.empty():
        item = input_queue.get()
        if item[TYPE] == SUB:
            output_queue.put(
                (
                    item[INDEX],
                    update_subscription(
                        results[item[INDEX]], item[RESTRICTIONS], allowed
                    ),
                )
            )
        input_queue.task_done()
