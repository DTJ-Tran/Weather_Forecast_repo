package com.weather;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Weather {
    private String status;  // Status from prediction
    private Temperature meanTemp;  // In Celsius & Fahrenheit
    private Float precipitation;  // Precipitation in mm (milimeter)
    private WindSpeed windSpeed;  // Wind speed in kph
    private Temperature minTemp;
    private Temperature maxTemp;
    private static final int MAX_HOUR_IN_DAY = 24; 

    public void setStatus(String inputStatus) {
        this.status = inputStatus;
    };

    public String getStatus() {
        return this.status;
    };

    public void setMeanTemp(Double inputTemp) {
        this.meanTemp = new Temperature(inputTemp);
    };

    public Double getMeanCelTemp() {
        return (double) this.meanTemp.getCelTemp();
    };

    public void setMinTemp(Double inputTemp) {
        this.minTemp = new Temperature(inputTemp);
    };

    public Double getMinCelTemp() {
        return this.minTemp != null ? (double) this.minTemp.getCelTemp() : 0.0;
    };

    public void setMaxTemp(Double inputTemp) {
        this.maxTemp = new Temperature(inputTemp);
    };

    public Double getMaxCelTemp() {
        return this.maxTemp != null ? (double) this.maxTemp.getCelTemp() : 0.0;
    };

    public Float getPrecipitation() {
        return this.precipitation != null ? (float) this.precipitation : 0.0f;
    };

    public void setWindSpeed(Float inputWind) {
        this.windSpeed = new WindSpeed(inputWind);
    };

    public WindSpeed getWindSpeed() {
        return this.windSpeed != null ?  this.windSpeed : null;
    };

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Weather(Double inputTemp, Float inputPreci, float inputWindSpeed,String status) {
        this.setMeanTemp(inputTemp);
        this.setPrecipitation(inputPreci); 
        this.setWindSpeed(inputWindSpeed);
        this.setStatus(status);
    };

    public void setPrecipitation(Float inputPreci) {
        this.precipitation = inputPreci;
        return;
    };

    public List<Double> generateMeanTemperatures() {
        if (this.getMaxCelTemp() > this.getMaxCelTemp()) {
            throw new IllegalArgumentException("Minimum temperature cannot be greater than the maximum temperature.");
        }
        
        Random random = new Random();
        List<Double> temperatures = new LinkedList<>();
        // Generate temperatures over 24 hours
        for (int i = 0; i < this.MAX_HOUR_IN_DAY; i++) {
            // Simulate temperature fluctuations with a sine wave + randomness
            Double fluctuation = Math.sin((Math.PI * i) / 12) * (this.getMaxCelTemp() - this.getMeanCelTemp());
            Double randomVariation = (random.nextDouble() - 0.5) * 2; // Random noise
            Double temp = this.getMeanCelTemp() + fluctuation + randomVariation;

            // Clamp temperatures between min and max
            temp = Math.max(this.getMinCelTemp(), Math.min(this.getMaxCelTemp(), temp));
            temp = Math.round(temp * 10.0) / 10.0;
            temperatures.add(temp);
        }
        
        // Ensure maxTemp and minTemp are included
        ensureExtremes(temperatures, this.getMaxCelTemp(), this.getMinCelTemp());

        return temperatures;
    };

    public List<Float> generatePrecipitation() {
        Random random = new Random();
        List<Float> precipitationList = new LinkedList<>();
        for (int i = 0; i < this.MAX_HOUR_IN_DAY; i++) {
            Float curPreci = this.getPrecipitation() + (float) random.nextGaussian();
            precipitationList.add(Math.max(0, curPreci));
        };
        return precipitationList;
    };

    public List<WindSpeed> generateWindSpeed() {
        Random random = new Random();
        List<WindSpeed> windList = new LinkedList<>();
        for (int i = 0; i < this.MAX_HOUR_IN_DAY; i++) {
            Float curSpeed = Math.max(0, this.getWindSpeed().getSpeed() + (float) random.nextGaussian());
            WindSpeed curWind = new WindSpeed(curSpeed);
            windList.add(curWind);
        };
        return windList;
    };

    public List<String> genWeatherStatus() {
        List<Double> listOfMeanTemp = this.generateMeanTemperatures();
        List<Float> listOfPreci = this.generatePrecipitation();
        List<WindSpeed> listOfWind = this.generateWindSpeed();
        List<String> weatherStatList = new LinkedList<>();
        JavaClient genWeatherClient = new JavaClient();
        for (int iter = 0; iter < listOfMeanTemp.size(); iter++) {
            List<Float> feature = new LinkedList<>();
            feature.add(listOfPreci.get(iter));
            feature.add(listOfWind.get(iter).getSpeed() * 1.0f);
            feature.add(listOfMeanTemp.get(iter).floatValue());
            DataPoint dataFeat = new DataPoint(feature);
            String predWeather = genWeatherClient.getPredAnyType(dataFeat, genWeatherClient.PREDICT_WEATHER, String.class, false);
            weatherStatList.add(predWeather);
        }
        return weatherStatList;
    };

    public HashMap<String, Weather> getWeatherInHours() {
        HashMap<String, Weather> weatherInADay = new LinkedHashMap<>();
        List<Double> listOfMeanTemp = this.generateMeanTemperatures();
        List<Float> listOfPreci = this.generatePrecipitation();
        List<WindSpeed> listOfWind = this.generateWindSpeed();
        List<String> listOfWeatherStat = this.genWeatherStatus();
        int time = LocalTime.now().getHour();
        for (int iter = 0; iter < 9; iter++) {
            int i = (time + iter) % 24; // Wrap values greater than 23 back to 0
            Weather curWeather = new Weather(listOfMeanTemp.get(i), listOfPreci.get(i), listOfWind.get(i).getSpeed().floatValue(), (String) listOfWeatherStat.get(i));
            String hour = String.format("%02d:00", i);
            weatherInADay.put(hour, curWeather);
        };
        return weatherInADay;
    };

    // Ensure the list contains the exact max and min temperatures
    private static void ensureExtremes(List<Double> temperatures, double maxTemp, double minTemp) {
        int maxIndex = 0;
        int minIndex = 0;

        // Find current max and min indices
        for (int i = 0; i < temperatures.size(); i++) {
            if (temperatures.get(i) > temperatures.get(maxIndex)) maxIndex = i;
            if (temperatures.get(i) < temperatures.get(minIndex)) minIndex = i;
        }

        // Replace one value with the exact max and one with the exact min
        temperatures.set(maxIndex, maxTemp);
        temperatures.set(minIndex, minTemp);
    };

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Weather(Double inputTemp, Double inputMinTemp,Double inputMaxTemp ,Float inputPreci, Float inputWindSpeed,String status) {
        this.setMeanTemp(inputTemp);
        this.setMinTemp(inputMinTemp);
        this.setMaxTemp(inputMaxTemp);
        this.setPrecipitation(inputPreci); 
        this.setWindSpeed(inputWindSpeed);
        this.setStatus(status);
    };

    @Override
    public String toString() {
        String result = String.format(
            "Weather: %s, MeanTemp: %s, Precipitation: %s, WindSpeed: = %s, MinTemp: = %s, MaxTemp: = %s",
            this.getStatus() != null ? this.getStatus() : "NaN",
            this.getMeanCelTemp() != null ? this.getMeanCelTemp() : "NaN",
            this.getPrecipitation() != null ? String.format("%.2f", this.getPrecipitation())  : "NaN",
            this.getWindSpeed() != null ? this.getWindSpeed().getSpeed().intValue() : "NaN",
            this.getMinCelTemp() != null ? this.getMinCelTemp() : "NaN",
            this.getMaxCelTemp() != null ? this.getMaxCelTemp() : "NaN"
        );
        return result;
    };

    public static void main(String[] args) {
    }
}
