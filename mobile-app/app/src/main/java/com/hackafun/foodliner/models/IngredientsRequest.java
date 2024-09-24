package com.hackafun.foodliner.models;

public class IngredientsRequest {

    private String ingredients;

    public IngredientsRequest(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
