package com.hackafun.foodliner.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class GenerateRecipeResponse implements Parcelable {

    private String raw_response;
    private StructuredRecipe structured_recipe;

    protected GenerateRecipeResponse(Parcel in) {
        raw_response = in.readString();
    }

    public static final Creator<GenerateRecipeResponse> CREATOR = new Creator<GenerateRecipeResponse>() {
        @Override
        public GenerateRecipeResponse createFromParcel(Parcel in) {
            return new GenerateRecipeResponse(in);
        }

        @Override
        public GenerateRecipeResponse[] newArray(int size) {
            return new GenerateRecipeResponse[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(raw_response);
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

