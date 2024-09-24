package com.hackafun.foodliner.models;

import java.util.List;

public class CalculateRequest {

    private List<FoodItem> foodItems;

    // Constructor
    public CalculateRequest(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    // Getters and Setters
    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    // Inner class to represent each food item
    public static class FoodItem {
        private String food;
        private int mass;

        // Constructor
        public FoodItem(String food, int mass) {
            this.food = food;
            this.mass = mass;
        }

        // Getters and Setters
        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }

        public int getMass() {
            return mass;
        }

        public void setMass(int mass) {
            this.mass = mass;
        }
    }
}
