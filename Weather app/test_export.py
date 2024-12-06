# Load the dataset
import joblib
import pandas as pd
import numpy as np
import json
path = 'seattle-weather.csv'
weather_data = pd.read_csv(path)

weather_data['mean_temp'] = ((weather_data['temp_max']*0.45 + weather_data['temp_min']*0.55)).round(2)
weather_data['date']=pd.to_datetime(weather_data['date'])
weather_data['wind_speed']=weather_data['wind']
weather_data['weather_status']=weather_data['weather']
weather_data['max_temp']=weather_data['temp_max']
weather_data['min_temp']=weather_data['temp_min']

weather_data=weather_data.drop(['temp_max','temp_min','wind','weather'],axis=1)
print(weather_data.head())
weather_data.to_csv('weather-data.csv',index=False)


# # Preprocessing
# print(weather_data.head(5))

# X = weather_data.drop(['weather','date', 'temp_max', 'temp_min'], axis=1)
# y = weather_data['weather']
# print(y.head())

# print(X.head())

# features_list = []
# # Loop through each row in the DataFrame and create a features dictionary
# for index, row in X.iterrows():
#     features_list.append({
#         "features": [row['precipitation'], row['wind'], row['mean_temp']]
#     })


# with open('src/backEnd/test_data.json', 'w') as new_file:
#     json.dump(features_list, new_file, indent=4)

# # X.to_json('src/backEnd/formated_weather_data.json',orient= 'records', indent=4)
# # # prevent the index from being saved as a column in the csv file