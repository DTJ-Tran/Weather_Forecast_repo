import joblib
from matplotlib import pyplot as plt
import pandas as pd
import numpy as np

# FOR TESTING PURPOSES

path = 'seattle-weather.csv'

# Preprocessing
weather_data = pd.read_csv(path)
weather_data['mean_temp'] = (weather_data['temp_max'] + weather_data['temp_min']) / 2
weather_data['date'] = (pd.to_datetime(weather_data['date'])).dt.date
weather_data['weather'] = weather_data['weather'].map({'sun': 0, 'rain': 1,'fog': 2 , 'drizzle': 3, 'snow': 4})
weather_data['date_index'] = weather_data['date'].index

usable_data = weather_data.drop(['temp_max', 'temp_min'], axis=1)


# Each instance of the dataset was has the format [precipitation, wind, mean_temp]

# Create a linear regression model for Precipitation


from sklearn.preprocessing import MinMaxScaler
X_hum = usable_data['date_index'].values.reshape(-1, 1) 
y_hum = usable_data['precipitation']

# Using the model predict humidity

# model_preci = joblib.load('weather/src/main/resources/backEnd/predictors/precipitation_predictor.joblib')

# # Choose the number of day want to predict & learn
# sequence_length_preci = 7
# scaler = MinMaxScaler (feature_range=(0, 1))
# # Get the last sequence of days - predict the next day
# last_sequence_preci = weather_data['precipitation'].iloc[-sequence_length_preci:].values
# print("Original last sequence of precipitation: ", last_sequence_preci)
# last_sequence = last_sequence_preci.reshape((1, sequence_length_preci, 1)) # Shape for LSTM input 
# last_sequence = last_sequence.reshape(-1, 1)
# print(last_sequence)
# scaled_next_day_preci = scaler.fit_transform(last_sequence)
# scaled_next_day_preci = scaled_next_day_preci.reshape((1, sequence_length_preci, 1))
# print(scaled_next_day_preci.shape)
# scaled_next_day_preci= model_preci.predict(scaled_next_day_preci)
# next_7_days_preci = scaler.inverse_transform(scaled_next_day_preci.reshape(-1, 1))
# print("Predicted humidity for the next 7 day:", next_7_days_preci.flatten())


# # Predict for 7 days:
# days = [0,1,2,3,4,5,6]

# plt.figure(figsize=(10, 5))
# # plt.plot(weather_data.index, weather_data['precipitation'], label='Actual Precipitation', color='blue')
# plt.scatter(days, next_7_days_preci, label='Predicted Precipitation', color='red')
# plt.title('Time Series Prediction')
# plt.xlabel('Date')
# plt.ylabel('Precipitation')
# plt.legend()
# plt.show()










# Use the LSTM model to predict the temperature

# model_temp = joblib.load('src/main/resources/backEnd/predictors/temperature_predictor.joblib')
# scaler_temp = MinMaxScaler(feature_range=(0, 1))
# sequence_length_temp = 5 # Use the data of the last 2 days to predict the next day

# # Get the last sequence of days - predict the next day
# days = [0, 1, 2, 3, 4, 5, 6]
# last_sequence_temp = weather_data['mean_temp'].iloc[-sequence_length_temp:].values
# print("Original last sequence:", last_sequence_temp)
# last_sequence_temp = last_sequence_temp.reshape(-1, 1)
# scaled_temp = scaler_temp.fit_transform(last_sequence_temp)
# scaled_temp = scaled_temp.reshape((1, sequence_length_temp, 1))
# next_7_day = model_temp.predict(scaled_temp)
# next_7_day = scaler_temp.inverse_transform(next_7_day.reshape(-1, 1))
# print("Predicted temperature for the next 7 days:", next_7_day.flatten())

# # Print the predicted values for the next 7 days

# plt.figure(figsize=(10, 5))
# # plt.plot(weather_data.index, weather_data['precipitation'], label='Actual Precipitation', color='blue')
# plt.scatter(days, next_7_day, label='Predicted Temperature', color='red')
# plt.title('Time Series Prediction')
# plt.xlabel('Date')
# plt.ylabel('Temperature')
# plt.legend()
# plt.show()











# Use the model to predict the wind

# model_wind = joblib.load('src/main/resources/backEnd/predictors/wind_predictor.joblib')

# sequence_length_wind = 5
# scaler_wind = MinMaxScaler(feature_range=(0, 1))
# days = [0, 1, 2, 3, 4, 5, 6]

# # Get the last sequence of days - predict the next day
# last_sequence_wind = weather_data['wind'].iloc[-sequence_length_wind:].values
# print("Original last sequence of wind-speed: ", last_sequence_wind)
# last_sequence_wind = last_sequence_wind.reshape(-1, 1)
# scaled_sequence_wind = scaler_wind.fit_transform(last_sequence_wind)
# scaled_sequence_wind = scaled_sequence_wind.reshape((1, sequence_length_wind, 1))
# next_7_day_wind = model_wind.predict(scaled_sequence_wind)
# next_7_day_wind = scaler_wind.inverse_transform(next_7_day_wind.reshape(-1, 1))
# # Print the predicted values for the next 7 days
# print("Predicted wind-speed for the next day:", next_7_day_wind.flatten())


# plt.figure(figsize=(10, 5))
# # plt.plot(weather_data.index, weather_data['precipitation'], label='Actual Precipitation', color='blue')
# plt.scatter(days, next_7_day_wind, label='Predicted Wind', color='red')
# plt.title('Time Series Prediction')
# plt.xlabel('Date')
# plt.ylabel('Wind Speed')
# plt.legend()
# plt.show()
