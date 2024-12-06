# api.py

from flask import Flask, request, jsonify
import joblib
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import pandas as pd

app = Flask(__name__) # The application instance

# Load the trained model
temp_model = joblib.load('predictors/temperature_predictor.joblib')
precipitation_model = joblib.load('predictors/precipitation_predictor.joblib')
wind_model = joblib.load('predictors/wind_predictor.joblib')
weather_model = joblib.load('predictors/weatherPredictor.joblib')

scaler = MinMaxScaler(feature_range=(0, 1))
# Define the using dataset 
path = 'seattle-weather.csv'
weather_data = pd.read_csv(path)
weather_data['date'] = pd.to_datetime(weather_data['date'])
weather_data['wind_speed'] = weather_data['wind']
weather_data['weather_status'] = weather_data['weather']
weather_data['max_temp'] = weather_data['temp_max']
weather_data['min_temp'] = weather_data['temp_min']
weather_data['mean_temp'] = (weather_data['temp_max'] * 0.45 + weather_data['temp_min'] * 0.55)
scaled_data_wind = scaler.fit_transform(weather_data['wind_speed'].values.reshape(-1, 1))
weather_data= weather_data.drop(['temp_max', 'temp_min', 'wind', 'weather'], axis = 1)
usable_data = weather_data


from sklearn.preprocessing import StandardScaler # Normal Scaling for Mean
from sklearn.preprocessing import RobustScaler # Robus Scaler for Max
from sklearn.preprocessing import MinMaxScaler #Min-max Scaler for Min
# Initialize the scaler
scaler_obj = StandardScaler()
robust_scale = RobustScaler()
max_min_scale = MinMaxScaler()

scalers = {
    'mean_temp': scaler_obj,
    'min_temp': max_min_scale,
    'max_temp': robust_scale
}
column_names = ['mean_temp', 'min_temp', 'max_temp']
# Fit the scalers to their respective columns
scaler_obj.fit(usable_data[['mean_temp']])       # Fit StandardScaler for mean_temp
max_min_scale.fit(usable_data[['min_temp']])     # Fit MinMaxScaler for min_temp
robust_scale.fit(usable_data[['max_temp']])      # Fit RobustScaler for max_temp


# print(usable_data.head())

def inverse_transform_sample(sample, scalers, column_names): # From (0,1) -> (raw-data)
    """
    Perform inverse transformation on a sample array with 3 features.

    Parameters:
    - sample (numpy.array): An array with shape (3,) representing a single sample.
    - scalers (dict): A dictionary of scalers for each feature, e.g., {'mean_temp': scaler_obj, 'min_temp': max_min_scale, 'max_temp': robust_scale}.
    - column_names (list): List of feature names matching the order of the sample.

    Returns:
    - pd.Series: A Series containing the inverse transformed values for the sample.
    """
    inverse_transformed_values = []


    for i, col in enumerate(column_names):
        scaler = scalers[col]
        feature_value = sample[i].reshape(1, -1)  # Reshape to 2D for scaler
        inverse_transformed_value = scaler.inverse_transform(feature_value)
        inverse_transformed_values.append(inverse_transformed_value[0, 0])  # Extract scalar value

    # return pd.Series(data=inverse_transformed_values, index=column_names)
    return np.array(inverse_transformed_values)

@app.route('/predictTemp', methods=['POST']) # Feed an array of Data [max, min, mean]
def predict_temp():
    # read the data on temperature from POST request
    try:
        # Get data from request
        data = request.get_json(force=True)

        # Ensure the input is a NumPy array
        features = np.array(data['features']).reshape(1, 3)  # reshape to 1D array
        print("Features: ", features)

        feat_scaled = np.array([
            scaler_obj.transform(features[0][0].reshape(1, -1)),  # Reshape to 2D and transform
            max_min_scale.transform(features[0][1].reshape(1, -1)),  # Same for each feature
            robust_scale.transform(features[0][2].reshape(1, -1))
        ]).reshape(-1)
        print("Scaled Features: ", feat_scaled)
        # Convert to the appropriate format for the model
        print()
        prediction = temp_model.predict(feat_scaled.reshape(1, -1, 3))
        print("Result: ", prediction[0])
        result_fin = inverse_transform_sample(prediction[0], scalers, column_names)
        print("Scaled Result: ", result_fin)
        response = jsonify({'prediction': result_fin.tolist()})  # Create JSON responsd
        return response

    except Exception as e:
        # Return the error message
        print("Error Message Being Provoked")
        print(e)
        return jsonify({'error': str(e)}), 500


@app.route('/predictWind', methods=['POST'])
def predict_wind():
    # read the data on wind speed from POST request
    try:
        # Get data from request
        data = request.get_json(force=True)

        # Ensure the input is a NumPy array
        features = np.array(data['features']).reshape(1, -1)  # reshape to 1D array
        print("Features: ", features)
        # Convert to the appropriate format for the model
        print()
        prediction = wind_model.predict(features)
        print("Result: ", prediction[0])
        response = jsonify({'prediction': float(prediction[0])})  # Create JSON responsd
        return response

    except Exception as e:
        # Return the error message
        print("Error Message Being Provoked")
        print(e)
        return jsonify({'error': str(e)}), 500



@app.route('/predictPrecipitation', methods=['POST'])
def predict_precipitation():
    # read the data on precipitation from POST request
    try:
        # Get data from request
        data = request.get_json(force=True)

        # Ensure the input is a NumPy array
        features = np.array(data['features']).reshape(1, -1)  # reshape to 1D array
        print("Features: ", features)
        # Convert to the appropriate format for the model
        print()
        prediction = precipitation_model.predict(features)
        print("Result: ", prediction[0])
        response = jsonify({'prediction': float(prediction[0])})  # Create JSON responsd
        return response

    except Exception as e:
        # Return the error message
        print("Error Message Being Provoked")
        print(e)
        return jsonify({'error': str(e)}), 500


@app.route('/predictWeather', methods=['POST'])
def predict_weather():
    try:
        # Get data from request (force=True to ensure it's parsed as JSON)
        data = request.get_json(force=True)
        print("Received data: ", data) # Format [Precipitation, Wind-Speed, Mean-Temp]

        # Extract features from the data (assuming features are in the 'features' field)
        inputData = np.array(data['features']).reshape(1, -1)  # Reshaping to 2D array
        print("Input data shape: ", inputData.shape)

        # Get prediction from the model
        prediction = weather_model.predict(inputData)
        print("Prediction: ", prediction)

        # Map prediction to the corresponding weather type (0 to 4)
        weather_types = {0: 'Sun', 1: 'Rain', 2: 'Fog', 3: 'Drizzle', 4: 'Snow'}
        predicted_weather = weather_types.get(prediction[0], 'Unknown')  # Default to 'Unknown' if not in the map

        # Send the result as a JSON response
        response = jsonify({'prediction': predicted_weather})
        print("prediction weather: " + predicted_weather)
        print("Response: ", response)
        return response  # Return the JSON response

    except Exception as e:
        # Handle any errors gracefully
        print("Error occurred: ", e)
        return jsonify({'error': str(e)}), 500  # Return error message as JSON with status code 500

    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)