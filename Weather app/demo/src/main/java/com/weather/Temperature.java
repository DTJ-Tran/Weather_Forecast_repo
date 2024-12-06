package com.weather;
public class Temperature {
    private Double fahrenheitDeg;  // Round down Integer
    private Double celciusDeg; // Round down Integer

    public Temperature() {
        this.setCelcius(0.0);
        this.setFahrenheit(0 *9/5 + 32.0); 
    };

    public Temperature(Double inputCel) {
        this.setCelcius(inputCel);
        this.setFahrenheit(inputCel * 9/5 +32);
    };

    public void setFahrenheit(Double inputFah) {
        this.fahrenheitDeg = inputFah;
        return;
    };

    public void setCelcius(Double inputCel) {
        this.celciusDeg = inputCel;
        return;
    };

    public Double getCelTemp() {
        return this.celciusDeg;
    };

    public Double getFahTemp() {
        return this.fahrenheitDeg;
    };
    
    public static void main(String[] args) {
        Temperature testTemp = new Temperature(32 * 1.0);
        Temperature testTemp1 = new Temperature();
        System.out.println(testTemp1.getCelTemp());
        System.out.println(testTemp1.getFahTemp());
        System.out.println(testTemp.getCelTemp());
        System.out.println(testTemp.getFahTemp());
    }

}