from . import generate_user_data


def main(user_count: int):
    csv_folder = 'tmp'
    json_body = {
        "login": "",
        "password": "testpassword",
        "confirmPassword": "testpassword",
        "email": "user@example.com",
        "firstName": "Vladimir",
        "lastName": "Putin",
        "address": "Kremlin",
        "roles": [
            "DETECTIVE",
        ],
        "telegramId": 12346,
    }

    generate_user_data(user_count, json_body,
                       csv_name='user_registration.csv',
                       csv_folder=csv_folder)
    json_body = {
        "login": "",
        "password": "testpassword"
    }
    generate_user_data(user_count, json_body,
                       csv_name='user_login.csv',
                       csv_folder=csv_folder)


if __name__ == '__main__':
    main(1000)
