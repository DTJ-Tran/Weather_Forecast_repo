package com.weather;
import java.util.NavigableMap;
import java.util.TreeMap;
public class WindSpeed {
    
    private Integer speed;      // let in km/h
    private String typeOfWind; // Base on the beufortLevel to decide
    private static final NavigableMap<Integer, String> beauFortScale = new TreeMap<>();
    
    static { // Share among all the object
        beauFortScale.put(0, "Calm");
        beauFortScale.put(2, "Light Air");
        beauFortScale.put(6, "Light Breeze");
        beauFortScale.put(12, "Gentle Breeze");
        beauFortScale.put(20, "Moderate Breeze");
        beauFortScale.put(29, "Fresh Breeze");
        beauFortScale.put(39, "Strong Breeze");
        beauFortScale.put(50, "Near Gale");
        beauFortScale.put(62, "Gale");
        beauFortScale.put(75, "Strong Gale");
        beauFortScale.put(88, "Storm");
        beauFortScale.put(103, "Violent Storm");
        beauFortScale.put(117, "Hurricane");
    };
   

   
    public void setWindSpeed(Integer inputWindSpeed) {
        this.speed = inputWindSpeed;
        return;
    };

    public Integer getSpeed() {
        return this.speed;
    };

    public void setWindType() {
        this.typeOfWind = beauFortScale.floorEntry(this.getSpeed()).getValue();
        return;
        
    };

    public String getWindType() {
        return this.typeOfWind;
    };

    public WindSpeed() {
        this.setWindSpeed(0);
        this.setWindType();
    };

    public WindSpeed(Float inputSpeed) {
        this.setWindSpeed((int) Math.floor(inputSpeed));
        this.setWindType();
    };

}
