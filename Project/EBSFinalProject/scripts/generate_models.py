import os


def main():
    generate_publication_model_command = (
        "protoc -I=../protobuf --java_out=../src/main/java Publication.proto"
    )
    generate_subscription_model_command = (
        "protoc -I=../protobuf --java_out=../src/main/java Subscription.proto"
    )

    commands = [generate_publication_model_command, generate_subscription_model_command]

    os.system(generate_publication_model_command)

    for command in commands:
        os.system(command)


if __name__ == "__main__":
    main()
