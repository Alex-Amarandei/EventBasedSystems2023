from Cryptodome.Cipher import AES
from Cryptodome.Util.Padding import pad, unpad
import base64
import sys


def encrypt(value):
    key = b"f8b27a0d8c837edc8fb00ea85f502fb4"
    cipher = AES.new(key, AES.MODE_ECB)
    encrypted_data = cipher.encrypt(pad(value.encode(), AES.block_size))
    encrypted_value = base64.b64encode(encrypted_data).decode()
    return encrypted_value


def decrypt(value):
    key = b"f8b27a0d8c837edc8fb00ea85f502fb4"
    cipher = AES.new(key, AES.MODE_ECB)
    decrypted_data = cipher.decrypt(base64.b64decode(value))
    decrypted_value = unpad(decrypted_data, AES.block_size).decode()
    return decrypted_value


if __name__ == "__main__":
    command = sys.argv[1]
    value = sys.argv[2]

    if command == "decrypt":
        decrypted_value = decrypt(value)
        print(decrypted_value)
