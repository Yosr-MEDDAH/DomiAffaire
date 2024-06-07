from flask import Flask, jsonify, request
import pandas as pd
import numpy as np
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.neighbors import NearestNeighbors
from scipy.sparse import csr_matrix
from datetime import datetime, timedelta
from flask_cors import CORS, cross_origin

app = Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

# Function to load and preprocess data
def load_and_preprocess_data():
    reservations = pd.read_csv('data/reservations.csv')
    rooms = pd.read_csv('data/rooms.csv')

    # Strip double quotes from equipment names
    rooms['equipments'] = rooms['equipments'].str.replace('"', '')

    mlb = MultiLabelBinarizer()
    equipments_onehot = pd.DataFrame(mlb.fit_transform(rooms['equipments'].str.split(',')), columns=mlb.classes_)
    rooms = pd.concat([rooms, equipments_onehot], axis=1)
    rooms.drop(columns=['equipments'], inplace=True)
    encoded_features = rooms.drop(columns=['room_id'])
    rooms_matrix = csr_matrix(encoded_features)

    # Train the model
    model = NearestNeighbors(algorithm='brute')
    model.fit(rooms_matrix)

    return reservations, rooms, mlb, encoded_features, rooms_matrix, model

# Conflict checking functions
def is_date_conflict(requested_start, requested_end, reserved_start, reserved_end):
    return not (requested_end < reserved_start or requested_start > reserved_end)

def is_time_conflict(requested_start_time, requested_end_time, reserved_start_time, reserved_end_time):
    return not (requested_end_time <= reserved_start_time or requested_start_time >= reserved_end_time)

@cross_origin()
@app.route('/recommend', methods=['POST'])
def recommend_room():
    data = request.get_json()

    nbrPlaces = data.get('nbrPlaces')
    required_equipments = data.get('required_equipments')
    duration = data.get('duration')
    start_date = data.get('start_date')
    num_days = data.get('num_days')
    hourBeginingSuggested = data.get('hourBeginingSuggested')
    hourEndingSuggested = data.get('hourEndingSuggested')
    nature = data.get('nature')
    title = data.get('title')

    # Load and preprocess data
    reservations, rooms, mlb, encoded_features, rooms_matrix, model = load_and_preprocess_data()

    # Ensure the recommended room has the required equipment
    if required_equipments:
        required_equipments = [e.strip() for e in required_equipments.split(',')]
        if set(required_equipments).issubset(mlb.classes_):
            required_equipments_onehot = mlb.transform([required_equipments])
            required_room = pd.DataFrame(required_equipments_onehot, columns=mlb.classes_)
            required_room['nbrPlaces'] = nbrPlaces
            required_room = required_room[encoded_features.columns]
            required_room_matrix = csr_matrix(required_room)

            distances, suggestions = model.kneighbors(required_room_matrix, n_neighbors=4)

            recommended_rooms = []
            for room_id in rooms['room_id'].iloc[suggestions[0]]:
                room_capacity = rooms[rooms['room_id'] == room_id]['nbrPlaces'].values[0]
                if room_capacity < nbrPlaces:
                    continue

                room_recommendations = []
                for single_date in (datetime.strptime(start_date, "%Y-%m-%d") + timedelta(n) for n in range(num_days)):
                    for hour in range(hourBeginingSuggested, hourEndingSuggested - duration + 2):
                        slot_start_time = datetime.strptime(f"{hour}:00:00", "%H:%M:%S").time()
                        slot_end_time = (datetime.combine(datetime.today(), slot_start_time) + timedelta(hours=duration)).time()

                        slot_start_datetime = datetime.combine(single_date, slot_start_time)
                        slot_end_datetime = datetime.combine(single_date, slot_end_time)

                        conflict = False
                        for _, reservation in reservations[reservations['room_id'] == room_id].iterrows():
                            reserved_start_datetime = datetime.strptime(reservation['dateBegining'].strip(), "%Y-%m-%dT%H:%M:%S.%f")
                            reserved_end_datetime = datetime.strptime(reservation['dateEnding'].strip(), "%Y-%m-%dT%H:%M:%S.%f")
                            
                            if is_date_conflict(slot_start_datetime, slot_end_datetime, reserved_start_datetime, reserved_end_datetime) and \
                               is_time_conflict(slot_start_time, slot_end_time, reserved_start_datetime.time(), reserved_end_datetime.time()):
                                conflict = True
                                break

                        if not conflict:
                            room_recommendations.append({
                                "date": single_date.strftime("%Y-%m-%d"),
                                "start_time": slot_start_datetime.strftime("%H:%M"),
                                "end_time": slot_end_datetime.strftime("%H:%M")
                            })
                            break

                    if len(room_recommendations) >= num_days:
                        break

                recommended_rooms.append({
                    "room_id": room_id,
                    "slots": room_recommendations
                })

            return jsonify({'recommended_rooms': recommended_rooms})
        else:
            return jsonify({'error': 'One or more required equipments are invalid.'}), 400
    else:
        return jsonify({'error': 'No required equipments specified.'}), 400

if __name__ == '__main__':
    # app.run(debug=True)
    app.run(host='0.0.0.0',port=5000)