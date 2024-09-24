package com.hackafun.foodliner.models;

import java.io.Serializable;

public class CalculateResponse implements Serializable {

    private double totalCalories;
    private double totalCarbonEmission;

    // Getters and Setters
    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalCarbonEmission() {
        return totalCarbonEmission;
    }

    public void setTotalCarbonEmission(double totalCarbonEmission) {
        this.totalCarbonEmission = totalCarbonEmission;
    }
}

