package com.weather;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

// Create the the value for new Location
public class weatherAPI {
   // API credentials
   private static final String API_KEY = "8447641334244ac9a1771216242111";
   private static final String BASE_URL = "http://api.weatherapi.com/v1/history.json";

   // Method to fetch historical weather data using WeatherAPI
   @SuppressWarnings("ConvertToTryWithResources")
   public static JSONObject fetchHistoricalWeatherData(String location, String startDate, String endDate) throws Exception {
       String urlStr = BASE_URL + "?key=" + API_KEY + "&q=" + location + "&dt=" + startDate + "&end_dt" + endDate;
       URL url = new URL(urlStr);
       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
       conn.setRequestMethod("GET");

       int responseCode = conn.getResponseCode();

       // Check for HTTP errors
       if (responseCode != HttpURLConnection.HTTP_OK) { // If not 200 (OK)
           System.err.println("HTTP Error Code: " + responseCode);
           BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
           StringBuilder errorResponse = new StringBuilder();
           String line;
           while ((line = errorReader.readLine()) != null) {
               errorResponse.append(line);
           }
           errorReader.close();

           System.err.println("Error Response: " + errorResponse.toString());
           throw new IOException("HTTP Error " + responseCode + ": " + conn.getResponseMessage());
       }

       // If response is OK, read the input stream
       BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
       String inputLine;
       StringBuilder response = new StringBuilder();
       while ((inputLine = in.readLine()) != null) {
           response.append(inputLine);
       }
       in.close();

       // Parse the JSON response
       JSONObject jsonResponse = new JSONObject(response.toString());
       return jsonResponse.getJSONObject("forecast")
                       .getJSONArray("forecastday")
                       .getJSONObject(0)
                       .getJSONObject("day");
   };

   // Method to print weather data
   @SuppressWarnings("UnnecessaryReturnStatement")
   public static void printWeatherData(String date, JSONObject dayData) throws JSONException {
       double precipitation = dayData.getDouble("totalprecip_in")*100;
       double maxTemp = dayData.getDouble("maxtemp_c");
       double minTemp = dayData.getDouble("mintemp_c");
       double AvgTemp = dayData.getDouble("avgtemp_c");
       String weatherStatus = dayData.getJSONObject("condition").getString("text");
       double windSpeed = dayData.getDouble("maxwind_kph");

       System.out.println(date);
       System.out.println("precipitation: " + precipitation);
       System.out.println("Wind Speed (kph): " + windSpeed);
       System.out.println("Weather Status: " + weatherStatus);
       System.out.println("Avg Temp (°C): " + AvgTemp);
       System.out.println("Max Temp (°C): " + maxTemp);
       System.out.println("Min Temp (°C): " + minTemp);
       System.out.println("-------------------------------");
       return;
   };

   public static Map<String, Object> collectWeatherData(String date, JSONObject dayData) throws JSONException {
       Map<String, Object> result = new LinkedHashMap<>();
       double precipitation = dayData.getDouble("totalprecip_in");
       double maxTemp = dayData.getDouble("maxtemp_c");
       double minTemp = dayData.getDouble("mintemp_c");
       double AvgTemp = dayData.getDouble("avgtemp_c");
       String weatherStatus = dayData.getJSONObject("condition").getString("text");
       double windSpeed = dayData.getDouble("maxwind_kph");
       
       
       result.put("id", 0);
       result.put("date", date);
       result.put("precipitation", precipitation);
       result.put("wind_speed", windSpeed);
       result.put("weather_status", weatherStatus);
       result.put("mean_temp", AvgTemp);
       result.put("max_temp", maxTemp);
       result.put("min_temp", minTemp);

       return result;
   };
   
   // Method to increment date by one day
   public static String incrementDate(String date) throws Exception {
       java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
       java.util.Date d = sdf.parse(date);
       java.util.Calendar cal = java.util.Calendar.getInstance();
       cal.setTime(d);
       cal.add(java.util.Calendar.DATE, 1);
       return sdf.format(cal.getTime());
   }

   public weatherAPI() {

   };
   @SuppressWarnings("empty-statement")
   public static void main(String[] args) {
       String location = "Londo"; // Replace with your desired location
       LocalDate endDate = LocalDate.now(); // Gets the current date
       LocalDate startDate = endDate.minusDays(7); // Start date for historical data    
       String currentDate = startDate.toString();
       DataBase testDB = new DataBase("7294567","root","my_database");
       testDB.createTableDB(location);
       testDB.deleteAllRow(location);
       Map<String, Object> testData;
       try {
           while (!currentDate.equals(endDate.plusDays(1).toString())) {
               List<String> colName = new ArrayList<>();
               List<Double> argu = new ArrayList<>();
               JSONObject dayData = fetchHistoricalWeatherData(location, currentDate, endDate.toString());
               // printWeatherData(currentDate, dayData);
               testData = collectWeatherData(currentDate, dayData);
               for (Map.Entry<String, Object> entry : testData.entrySet()) {
                   colName.add(entry.getKey());
                   if (entry.getValue() instanceof Double aDouble) {
                       argu.add(aDouble);
                   }
;
               };

               String dateVal = (String) testData.get("date");
               String weatherStat = (String) testData.get("weather_status");
               testDB.addRowDate(location, dateVal, weatherStat ,argu, colName);
               currentDate = incrementDate(currentDate); // Increment to the next date
           };
       
       } catch (Exception e) {
           e.printStackTrace();
       }
   };
}