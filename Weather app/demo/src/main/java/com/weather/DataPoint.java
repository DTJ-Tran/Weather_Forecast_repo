package com.weather;
import java.util.ArrayList;
import java.util.List;

/*
 * This type of DataType is only serve for the purpose to interact with the Python API
 */
public class DataPoint {
    private List<Float> features; // This will map to the "features" array in the JSON.
    
    public DataPoint() {
        // Default Constructor;
        features = new ArrayList<>();
    };

    public DataPoint(List<? extends Number> others) {
        if (others != null) {
            this.features = new ArrayList<>();
            for (Number currentItem : others) {
                if (currentItem instanceof Float) {
                    this.features.add((Float) currentItem);
                } else if (currentItem instanceof Double) {
                    this.features.add(((Double) currentItem).floatValue());
                } else {
                    this.features.add(currentItem.floatValue()); // Fallback for other Number types
                }
            }
        }
    }


    public final List<Float> getFeature(){
        return this.features;
    };

    // override toString() for easy printing of each DataPoint.
    @Override
    public String toString() {
        return String.format("{\"features\": %s}", this.features.toString());
    }
}