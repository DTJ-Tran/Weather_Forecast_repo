package com.weather;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Location {
    private String locName; // same Name for the table in the Database
    private final weatherAPI locAPIObj;
    private static final int MAX_DAY_IN_WEEK= 7;
    private static String inPassDB;
    private static String inUserName = "root";
    private static String inDBName;

    private String getInPassDB() {
        return this.inPassDB;
    };

    public void setInPassDB(String inputPass) {
        this.inPassDB = inputPass;
        return;
    };

    public void setInUserName(String inputUsr) {
        this.inUserName = inputUsr;
        return;
    };

    private String getInUserName() {
        return this.inUserName;
    };

    public void setInDBName(String inputDBName) {
        this.inDBName = inputDBName;
        return;
    };

    private String getInDBName() {
        return this.inDBName;
    };

    public String getLocName() {
        return this.locName;
    };

    public void setLocName(String inputName) {
        this.locName = inputName;
        return;
    };

    public Location() {
        this.setLocName("");
        this.locAPIObj = new weatherAPI();
        this.setInDBName("weatherForecast");
        this.setInPassDB("TheGoodPlace");
    };

    public Location(String inputLocName) {
        this.setLocName(inputLocName);
        this.locAPIObj = new weatherAPI();
        this.setInDBName("weatherForecast");
        this.setInPassDB("TheGoodPlace");
    };

    public Location(String inputLocName, String inputPass, String inputUsr, String inputDBName) {
        this.setLocName(inputLocName);
        this.locAPIObj = new weatherAPI();
        this.setInDBName(inputDBName);
        this.setInPassDB(inputPass);
        this.setInUserName(inputUsr);
        this.CreateDataLoc();
    };

    private weatherAPI callAPI() {
        return this.locAPIObj;
    }

    public void CreateDataLoc() {
       LocalDate endDate = LocalDate.now(); // Gets the current date
       LocalDate startDate = endDate.minusDays(7); // Start date for historical data
       String currentDate = startDate.toString();
       DataBase testDB = new DataBase(this.getInPassDB(),this.getInUserName(),this.getInDBName());
       if (!testDB.doesTableExist(locName)) {
        testDB.createTableDB(this.getLocName());
       };
       testDB.deleteAllRow(this.getLocName());
       weatherAPI api= this.callAPI();
       Map<String, Object> testData;
       try {
           while (!currentDate.equals(endDate.plusDays(1).toString())) {
               List<String> colName = new ArrayList<>();
               List<Double> argu = new ArrayList<>();
               JSONObject dayData = api.fetchHistoricalWeatherData(this.getLocName(), currentDate, endDate.toString());
               // printWeatherData(currentDate, dayData);
               testData = api.collectWeatherData(currentDate, dayData);
               for (Map.Entry<String, Object> entry : testData.entrySet()) {
                   colName.add(entry.getKey());
                   if (entry.getValue() instanceof Double aDouble) {
                       argu.add(aDouble);
                   }
;
               };

               String dateVal = (String) testData.get("date");
               String weatherStat = (String) testData.get("weather_status");
               testDB.addRowDate(this.getLocName(), dateVal, weatherStat ,argu, colName);
               currentDate = api.incrementDate(currentDate); // Increment to the next date
           };
       
       } catch (Exception e) {
           e.printStackTrace();
       }
    };

    public void predictWeather(List<Float> listOfTemp,List<Float> listOfPreci, List<Float> listOfWindSpeed) {
        if (listOfTemp.size() != 3 ) {
            System.out.println("Check the temp list only contain 3 Data-point to predict");
            return ;
        }
        // Feature Lists
        DataPoint meanTempFeat = new DataPoint(listOfTemp);
        DataPoint preciFeat = new DataPoint(listOfPreci); 
        DataPoint windSpeedFeat = new DataPoint(listOfWindSpeed);

        // Declare the predictor
        JavaClient predictor = new JavaClient();

        // Doing the Prediction
        List<Double> newMeanTemp = predictor.getPredAnyType(meanTempFeat, predictor.PREDICT_TEMP, (Class<List<Double>>) (Class<?>) List.class, true);
        Float newPreci = predictor.getPredAnyType(preciFeat, predictor.PREDICT_PRECI, Float.class, false);
        Float newWindSpeed = predictor.getPredAnyType(windSpeedFeat, predictor.PREDICT_WIND, Float.class, true);
        
      
        Double meanTemp = newMeanTemp.get(0);
        Double maxTemp = newMeanTemp.get(2);
        Double minTemp = newMeanTemp.get(1);
        
        // guess the new Weather Status:
        List<Double> newStatFeat = new LinkedList<>();
        newStatFeat.add(newPreci.doubleValue());
        newStatFeat.add(newWindSpeed.doubleValue());
        newStatFeat.add(meanTemp);
        DataPoint dataFeat = new DataPoint(newStatFeat);
        // Create the new Weather Object
        String newStatus = predictor.getPredAnyType(dataFeat, predictor.PREDICT_WEATHER, String.class, false);
        
        DataBase weatherDB = new DataBase(this.getInPassDB(), "root", this.getInDBName());
        Weather predWeather = new Weather(meanTemp * 1.0, minTemp * 1.0, maxTemp * 1.0, newPreci, newWindSpeed, newStatus);
        // Add the new Prediction to the DB
        List<String> colName  = new LinkedList<String>();
        colName.add("id");
        colName.add("date");
        colName.add("precipitation");
        colName.add("wind_speed");
        colName.add("weather_status");
        colName.add("mean_temp");
        colName.add("max_temp");
        colName.add("min_temp");
        
        List<String> updateDB = new LinkedList<String>();
        updateDB.add(String.format("%.2f", predWeather.getPrecipitation()));
        updateDB.add(predWeather.getWindSpeed().getSpeed().toString());
        updateDB.add(predWeather.getStatus());
        updateDB.add(predWeather.getMeanCelTemp().toString());
        updateDB.add(predWeather.getMaxCelTemp().toString());
        updateDB.add(predWeather.getMinCelTemp().toString());

        weatherDB.addNewRowDB(this.getLocName(), updateDB, colName);
        
        return;
    };

    private List<Float> buildTempFeat() {
        DataBase weatherDB = new DataBase(this.getInPassDB(), "root", this.getInDBName());
        List<Float> minTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "min_temp", "date", Float.class, "3");
        List<Float> maxTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "max_temp", "date", Float.class, "3");
        List<Float> avgTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "mean_temp", "date", Float.class, "3");

        List<Float> tempsGuess = new LinkedList<>();
        tempsGuess.add(maxTemps.get(0));
        tempsGuess.add(minTemps.get(0));
        tempsGuess.add(avgTemps.get(0));
        return tempsGuess;
    };


    public HashMap<String, Weather> getWeatherInWeek() {
        // THe last element is the predicted day & first 8 days was the old date
        if (this.getLocName().isEmpty()) {
            System.out.println("Cannot get the data with empty location");
            return null;
        }
        HashMap<String, Weather> weatherInWeek = new LinkedHashMap<>();
        DataBase weatherDB = new DataBase(this.getInPassDB(), "root", this.getInDBName());

        List<Float> tempGuess = this.buildTempFeat();
        List<Float> precFeat = weatherDB.getDataSQLFloatArr(this.getLocName(), "precipitation", "date");
        List<Float> windFeat = weatherDB.getDataSQLFloatArr(this.getLocName(), "wind_speed", "date");
        this.predictWeather(tempGuess, precFeat, windFeat);

        List<String> dateList = weatherDB.getDataSQLObjectArr(this.getLocName(), "date", "date", String.class, "9"); // first element is the latest data
        List<Float> precList = weatherDB.getDataSQLObjectArr(this.getLocName(), "precipitation", "date", Float.class, "9");
        List<String> weatherStat = weatherDB.getDataSQLObjectArr(this.getLocName(), "weather_status", "date", String.class, "9"); // first element is the latest data
        List<Float> windSpeeds = weatherDB.getDataSQLObjectArr(this.getLocName(), "wind_speed", "date", Float.class, "9");
        List<Double> minTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "min_temp", "date", Double.class,"9");
        List<Double> maxTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "max_temp", "date", Double.class,"9");
        List<Double> meanTemps = weatherDB.getDataSQLObjectArr(this.getLocName(), "mean_temp", "date", Double.class,"9");
        
        for (int iter = this.MAX_DAY_IN_WEEK + 1; iter >= 0 ; iter--) {
            Weather earliestWeather = new Weather(meanTemps.get(iter).doubleValue(), minTemps.get(iter).doubleValue(), maxTemps.get(iter).doubleValue(), precList.get(iter), windSpeeds.get(iter), weatherStat.get(iter));
            weatherInWeek.put(dateList.get(iter), earliestWeather);
        };
        return weatherInWeek;
    }

    public static void main(String[] args) {
        // Location testLocation = new Location();
        Location testLocation1 = new Location("HaLong", "TheGoodPlace", "root", "weatherForecast");   
        HashMap<String, Weather> testWeather = testLocation1.getWeatherInWeek();
        for (Map.Entry<String, Weather> entry: testWeather.entrySet()){
            String key = entry.getKey();
            Weather weather = entry.getValue();
            System.out.println("Date: " + key + " Weather: " + weather.toString());
        };
        
        // DataBase weatherDB = new DataBase(testLocation1.getInPassDB(), "root", testLocation1.getInDBName());
        // // List<String> dateList = weatherDB.getDataSQLStringArr(testLocation1.getLocName(), "date", "date"); // first element is the latest data
        // List<Float> precList = weatherDB.getDataSQLFloatArr(testLocation1.getLocName(), "precipitation", "date");
        // List<Float> windSpeeds = weatherDB.getDataSQLFloatArr(testLocation1.getLocName(), "wind_speed", "date");
        // List<Float> minTemps = weatherDB.getDataSQLObjectArr(testLocation1.getLocName(), "min_temp", "date", Float.class, "3");
        // List<Float> maxTemps = weatherDB.getDataSQLObjectArr(testLocation1.getLocName(), "max_temp", "date", Float.class, "3");
        // List<Float> avgTemps = weatherDB.getDataSQLObjectArr(testLocation1.getLocName(), "mean_temp", "date", Float.class, "3");

        // List<Float> tempsGuess = new LinkedList<>();
        // tempsGuess.add(maxTemps.get(0));
        // tempsGuess.add(minTemps.get(0));
        // tempsGuess.add(avgTemps.get(0));
        
        // Weather testPred = testLocation1.predictWeather(tempsGuess,  precList, windSpeeds);
        // System.out.println(testPred.toString());

        return;
    };


};
