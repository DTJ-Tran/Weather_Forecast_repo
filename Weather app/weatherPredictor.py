import kagglehub

# # Download latest version
# path = kagglehub.dataset_download("ananthr1/weather-prediction")

# input format: [precipitation  temp_max  temp_min  wind  weather ]

# print("Path to dataset files:", path)

# Load the dataset
import joblib
import pandas as pd
import numpy as np
path = 'weather/src/main/resources/backEnd/seattle-weather.csv'
weather_data = pd.read_csv(path)
weather_data['mean_temp'] = ((weather_data['temp_max'] + weather_data['temp_min']) / 2).round(2)


# Preprocessing
# weather_data['weather'] = weather_data['weather'].map({'sun': 0, 'rain': 1,'fog': 2 , 'drizzle': 3, 'snow': 4})
# X = weather_data.drop(['weather','date', 'temp_max', 'temp_min'], axis=1)
# y = weather_data['weather']
# # print(y.head())

print(weather_data.head())

weather_data.to_csv('/Users/davesjamest/Desktop/weatherForecast/weather/src/main/resources/dataBase/formated_weather_data.csv', index= False)

# Each instance of the dataset was has the format [precipitation, wind, mean_temp]

# With temperature measure in Celcius
# Windspeed measure in km/h
# Precipitation || humidity measure in mm

# # Splitting the dataset
# from sklearn.model_selection import train_test_split
# X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# # Modelng the predictor
# from sklearn.ensemble import RandomForestClassifier
# from sklearn.metrics import accuracy_score, confusion_matrix, precision_score, recall_score, ConfusionMatrixDisplay

# rf = RandomForestClassifier()
# rf.fit(X_train, y_train)
# y_pred = rf.predict(X_test)



# # Hyperparameter tuning

# from sklearn.model_selection import RandomizedSearchCV
# from scipy.stats import randint

# param_dist = {'n_estimators': randint(50,500),
#               'max_depth': randint(1,20)}

# # Create a random forest classifier
# rf = RandomForestClassifier()

# # Use random search to find the best hyperparameters
# rand_search = RandomizedSearchCV(rf, 
#                                  param_distributions = param_dist, 
#                                  n_iter=5, 
#                                  cv=5)

# # Fit the random search object to the data
# rand_search.fit(X_train, y_train)

# # Create a variable for the best model
# best_rf = rand_search.best_estimator_

# # # Print the best hyperparameters
# # print('Best hyperparameters:',  rand_search.best_params_)



# # Generate predictions with the best model
# y_pred = best_rf.predict(X_test)

# accuracy = accuracy_score(y_test, y_pred)
# precision = precision_score(y_test, y_pred, average ='macro')
# recall = recall_score(y_test, y_pred, average ='macro')

# print("Accuracy:", accuracy)
# print("Precision:", precision)
# print("Recall:", recall)



# # Doing a test prediction with one instance

# X_new = np.array([[10.5, 15.2,4.3], [0, 25.5, 12.5], [0.5, 20.5, 10.5], [30.1, 10.5, 2.3]]) # [precipitation, wind, mean_temp]
# y_pred = best_rf.predict(X_new)
# print("Predicted weather:", y_pred[0], y_pred[1], y_pred[2],y_pred[3])

# joblib.dump(best_rf, 'src/backEnd/weatherPredictor.joblib', 5)