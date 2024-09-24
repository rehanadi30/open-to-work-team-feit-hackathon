package com.hackafun.foodliner.models;

import java.util.List;

public class GenerateRecipeResponse {

    private String raw_response;
    private StructuredRecipe structured_recipe;

    // Getters and Setters
    public String getRawResponse() {
        return raw_response;
    }

    public void setRawResponse(String raw_response) {
        this.raw_response = raw_response;
    }

    public StructuredRecipe getStructuredRecipe() {
        return structured_recipe;
    }

    public void setStructuredRecipe(StructuredRecipe structured_recipe) {
        this.structured_recipe = structured_recipe;
    }

    // Inner class for structured recipe
    public static class StructuredRecipe {
        private List<String> ingredients;
        private List<String> steps;
        private String title;

        // Getters and Setters
        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public List<String> getSteps() {
            return steps;
        }

        public void setSteps(List<String> steps) {
            this.steps = steps;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

