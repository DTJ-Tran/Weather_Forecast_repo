package com.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


public class JavaClient {
    
    public static final int PREDICT_TEMP = 0;
    public static final int PREDICT_WIND = 1;
    public static final int PREDICT_PRECI = 2;
    public static final int PREDICT_WEATHER = 3;



    private static Integer roundDoubleToInteger(Double predictionResultDouble) {
        // Extract the decimal part of the Double
        double decimalPart = predictionResultDouble - Math.floor(predictionResultDouble);
    
        // Determine the rounded value based on the decimal part
        if (decimalPart > 0.5) {
            return predictionResultDouble.intValue() + 1; // Round up
        } else {
            return predictionResultDouble.intValue(); // Round down
        }
    };

    public static Integer sendToServerInt(DataPoint dp, Integer predictionTask) {
        try {
            
            String predictionTaskStr;
            switch (predictionTask) {
                case PREDICT_TEMP:
                    predictionTaskStr = "predictTemp";
                    break;
                case PREDICT_WIND:
                    predictionTaskStr = "predictWind";
                    break;
                case PREDICT_PRECI:
                    predictionTaskStr = "predictPrecipitation";
                    break;
                case PREDICT_WEATHER:
                    predictionTaskStr = "predictWeather";
                    break;
                default:

                    throw new AssertionError("Invalid task to predict - please recheck with the predictionTask index");
            };

            // URL of the Flask API
            URL url = new URL("http://127.0.0.1:5001/" + predictionTaskStr);
   
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON data to send to the server

            // Print each DataPoint to verify parsing worked
            
            String jsonInputString = dp.toString(); // Example input  [precipitation, wind, mean_temp]
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // Read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // System.out.println("Response from Python API: " + response.toString());
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                Double predictionResultDouble = jsonObject.get("prediction").getAsDouble();
                Integer predictionResult = 0;
                predictionResult = roundDoubleToInteger(predictionResultDouble);
                //Double decimalPart = predictionResultDouble - Math.floor(predictionResultDouble.doubleValue());
                // if (decimalPart.compareTo(0.5) > 0) {
                //         predictionResult = (Integer) predictionResultDouble.intValue() + 1;
                // } else {
                //     predictionResult = (Integer) predictionResultDouble.intValue() ;
                // }
                return predictionResult;
                
            }
        } catch (IOException e) {
            System.out.println("Something Wrong");
            e.printStackTrace();
            return -1;
        } 
        
    };

    public static Double sendToServerDoub(DataPoint dp, Integer predictionTask) {
        try {
            
            String predictionTaskStr;
            switch (predictionTask) {
                case PREDICT_TEMP:
                    predictionTaskStr = "predictTemp";
                    break;
                case PREDICT_WIND:
                    predictionTaskStr = "predictWind";
                    break;
                case PREDICT_PRECI:
                    predictionTaskStr = "predictPrecipitation";
                    break;
                case PREDICT_WEATHER:
                    predictionTaskStr = "predictWeather";
                    break;
                default:

                    throw new AssertionError("Invalid task to predict - please recheck with the predictionTask index");
            };

            // URL of the Flask API
            URL url = new URL("http://127.0.0.1:5001/" + predictionTaskStr);
   
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON data to send to the server

            // Print each DataPoint to verify parsing worked
            
            String jsonInputString = dp.toString(); // Example input  [precipitation, wind, mean_temp]
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // Read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // System.out.println("Response from Python API: " + response.toString());
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                Double predictionResultDouble = jsonObject.get("prediction").getAsDouble();
        
                // Round the value to 2 decimal places
                BigDecimal roundedValue = new BigDecimal(predictionResultDouble).setScale(2, RoundingMode.HALF_UP);
                // Convert back to Double if needed
                predictionResultDouble = roundedValue.doubleValue();
                return predictionResultDouble;
                
            }
        } catch (IOException e) {
            System.out.println("Something Wrong");
            e.printStackTrace();
            return -1.0;
        } 
        
    };

    public static <Type> Type sendToServer(DataPoint dp, Integer predictionTask, Class<Type> returnType, boolean isInt) {
        try {
            String predictionTaskStr;
            switch (predictionTask) {
                case PREDICT_TEMP:
                    predictionTaskStr = "predictTemp";
                    break;
                case PREDICT_WIND:
                    predictionTaskStr = "predictWind";
                    break;
                case PREDICT_PRECI:
                    predictionTaskStr = "predictPrecipitation";
                    break;
                case PREDICT_WEATHER:
                    predictionTaskStr = "predictWeather";
                    break;
                default:
                    throw new AssertionError("Invalid task to predict - please recheck with the predictionTask index");
            }

            // URL of the Flask API
            URL url = new URL("http://127.0.0.1:5001/" + predictionTaskStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON data to send to the server
            String jsonInputString = dp.toString(); // Example input [precipitation, wind, mean_temp]
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse the response using Gson
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonElement predictionElement = jsonObject.get("prediction");

                // // Handle rounding for Double return types
                if (returnType.equals(Double.class)) {
                    Double predictionResultDouble = predictionElement.getAsDouble();
                    BigDecimal roundedValue = new BigDecimal(predictionResultDouble).setScale(2, RoundingMode.HALF_UP);
                    return returnType.cast(roundedValue.doubleValue());
                }

                if(isInt) {
                    if (predictionElement.isJsonPrimitive() && predictionElement.getAsJsonPrimitive().isNumber()) {
                        Double value = predictionElement.getAsDouble();
                        int result = roundDoubleToInteger(value);
                        JsonElement finalRes = new JsonPrimitive(result);
                        return new Gson().fromJson(finalRes, returnType);
                    } else{
                        JsonArray result = predictionElement.getAsJsonArray();
                        for (int iter = 0; iter <result.size(); iter++) {
                            JsonElement element = result.get(iter);
                            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                                    // Get the value as Double (or Float)
                                    Double value = element.getAsDouble();
                                    int roundedValue = roundDoubleToInteger(value).intValue();
                                    // Set the new value in the array (as a JsonPrimitive)
                                    result.set(iter, new JsonPrimitive(roundedValue));
                                }
                    }
                    
                    };
                };
                    // For other types, directly return the value casted to the required type
                    return new Gson().fromJson(predictionElement, returnType);
                }

                
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
            return null; // Return null for error cases
        }
    };

    public static <Type> Type getPredAnyType(DataPoint inputDatum, Integer predictionTask, Class<Type> returnType, boolean isInt) {
        return sendToServer(inputDatum,predictionTask, returnType, isInt);
    };

    public static Integer getPrediction(DataPoint inputDatum, Integer predictionTask) {
        return sendToServerInt(inputDatum, predictionTask);
    };
    
    public static Double getPredictionDoub(DataPoint inputDatum, Integer predictionTask) {
        return sendToServerDoub(inputDatum, predictionTask);

    };
   
    public static void main(String[] args) {

        DataBase weatherData = new DataBase("7294567","root","my_database");
        JavaClient testClient = new JavaClient();
        List<Float> precipitationList = weatherData.getDataSQLFloatArr("London", "precipitation", "date");
        List<Float> tempList = new LinkedList<>();
        Map<String, Object> lastRow = weatherData.getRowWithCondition("London", "ORDER BY id DESC LIMIT 1");
        tempList.add((float) lastRow.get("max_temp"));
        tempList.add((float) lastRow.get("min_temp"));
        tempList.add((float) lastRow.get("mean_temp"));
        List<Float> windList = weatherData.getDataSQLFloatArr("London", "wind_speed", "date");
        DataPoint tempData = new DataPoint(tempList);
        DataPoint windData = new DataPoint(windList);
        DataPoint precipitationData = new DataPoint(precipitationList);
        
        List<Double> newTemp = testClient.getPredAnyType(tempData, PREDICT_TEMP, (Class<List<Double>>) (Class<?>) List.class, true);
        System.out.println(newTemp.toString());
        Double newWind = testClient.getPredictionDoub(windData, PREDICT_WIND);
        Double newPreci = testClient.getPredictionDoub(precipitationData, PREDICT_PRECI);
        
        DataPoint newWeather = new DataPoint();
        newWeather.getFeature().add(newPreci.floatValue());
        newWeather.getFeature().add(newWind.floatValue());
        newWeather.getFeature().add(newTemp.get(2).floatValue());

        String newWeatherGuess = testClient.getPredAnyType(newWeather, PREDICT_WEATHER, String.class, false);
        System.out.println("New Weather: " + newWeatherGuess);


        
        // List<Float> mean_temp = weatherData.getDataSQLFloatArr("London", "mean_temp", "date");
        // List<Float> max_temp = weatherData.getDataSQLFloatArr("London", "max_temp", "date");
        // List<Float> min_temp = weatherData.getDataSQLFloatArr("London", "min_temp", "date");
        
        // ArrayList<Float> tempList = new ArrayList<>();
        // System.out.println(mean_temp.size());
        // for (int iter = 0; iter < mean_temp.size(); iter++) {
        //     DataPoint iterData = new DataPoint();
        //     iterData.getFeature().add(max_temp.get(iter));
        //     iterData.getFeature().add(min_temp.get(iter));
        //     iterData.getFeature().add(mean_temp.get(iter));
        //     List<Double> newTemp = testClient.getPredAnyType(iterData, PREDICT_TEMP, (Class<List<Double>>) (Class<?>) List.class);
        //     for (int jter = 0; jter < newTemp.size(); jter++) {
        //         newTemp.set(jter,  roundDoubleToInteger(newTemp.get(jter)).doubleValue());
        //     };
        //     tempList.add(newTemp);
        // };

        // Map<String, Object> lastRow = weatherData.getRowWithCondition("Lao_Cai", "ORDER BY id DESC LIMIT 1");
        // tempList.add((float)lastRow.get("max_temp"));
        // tempList.add((float) lastRow.get("min_temp"));
        // tempList.add((float)lastRow.get("mean_temp"));

        // DataPoint temp = new DataPoint(tempList);
        // List<Double> newTemp = testClient.getPredAnyType(temp, PREDICT_TEMP, (Class<List<Double>>) (Class<?>) List.class, true);
        // System.out.println(newTemp.toString());

        // System.out.println(tempList.toString());
        // DataPoint inTemps = new DataPoint(tempList);
        // List<Double> newTemp = testClient.getPredAnyType(tempList, PREDICT_TEMP, (Class<List<Double>>) (Class<?>) List.class);

        // DataPoint tempData = new DataPoint(tempList);
        // DataPoint windData = new DataPoint(windList);
        // DataPoint precipitationData = new DataPoint(precipitationList);
        
        // Integer newTemp = testClient.getPrediction(tempData, PREDICT_TEMP);
        // Double newWind = testClient.getPredictionDoub(windData, PREDICT_WIND);
        // Double newPreci = testClient.getPredictionDoub(precipitationData, PREDICT_PRECI);
        // System.out.println("New Temp: " + newTemp);
        // System.out.println("New Wind: " + newWind);
        // System.out.println("New Preci: " + newPreci);

        // DataPoint newWeather = new DataPoint();
        // newWeather.getFeature().add(newPreci.floatValue()); // 0
        // newWeather.getFeature().add(newWind.floatValue()); // 1
        // newWeather.getFeature().add(newTemp.floatValue()); // 2
        
        // Integer newWeatherGuess = testClient.getPrediction(newWeather, PREDICT_WEATHER);
        // System.out.println("New weather: " + newWeatherGuess);
        // List<String> colName  = new ArrayList<String>();
        // colName.add("id");
        // colName.add("date");
        // colName.add("precipitation");
        // colName.add("wind_speed");
        // colName.add("weather");
        // colName.add("mean_temp");

        // List<Double> argsList = new ArrayList<Double>();
        // argsList.add(newPreci);
        // argsList.add(newWind);
        // argsList.add((double) newWeatherGuess);
        // argsList.add((double) newTemp);
        // weatherData.addNewRowDB("weather_forecast", argsList, colName);
        
    }
}

