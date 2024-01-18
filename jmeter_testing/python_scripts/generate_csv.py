import csv
import os
from typing import Dict


def generate_user_data(user_count: int, json_body: Dict,
                       csv_name: str, csv_folder: str) -> None:
    """Generate csv file"""
    fieldnames = list(json_body.keys())
    if not os.path.exists(csv_folder):
        os.makedirs(csv_folder)

    csv_path = os.path.join(csv_folder, csv_name)
    with open(csv_path, 'w', newline='') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter='\t')
        writer.writeheader()

        for _ in range(user_count):
            user_data = json_body.copy()
            user_data['login'] = f'user_{_}'

            writer.writerow(user_data)
    print('Csv ready!')
