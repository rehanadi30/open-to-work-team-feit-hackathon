package com.hackafun.foodliner.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hackafun.foodliner.R;
import com.hackafun.foodliner.interfaces.Api;
import com.hackafun.foodliner.models.CalculateRequest;
import com.hackafun.foodliner.models.CalculateResponse;
import com.hackafun.foodliner.models.GenerateFoodResponse;
import com.hackafun.foodliner.models.GenerateRecipeResponse;
import com.hackafun.foodliner.models.IngredientsRequest;
import com.hackafun.foodliner.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextActivity extends AppCompatActivity {
    TextView foodResponse, ingredientResponse, stepsResponse, caloriesResponse, carbonEmissionsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // Initialize TextViews
        foodResponse = findViewById(R.id.recipeTitleTextView);
        ingredientResponse = findViewById(R.id.ingredientsContentTextView);
        stepsResponse = findViewById(R.id.stepsContentTextView);
        caloriesResponse = findViewById(R.id.caloriesTextView);
        carbonEmissionsResponse = findViewById(R.id.carbonEmissionTextView);

        // Get food items from the intent
        GenerateFoodResponse generateFoodResponse = getIntent().getParcelableExtra("generatefoodresponse");
        if (generateFoodResponse == null || generateFoodResponse.getItems() == null) {
            Toast.makeText(this, "No food items received", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare ingredients string
//        String ingredients = String.join(",", generateFoodResponse.getItems());
        String ingredients = "chicken, rice, broccoli";
        IngredientsRequest ingredientsRequest = new IngredientsRequest(ingredients);
        Log.d("IngredientsRequest", ingredientsRequest.getIngredients());

        // Call generate recipe API
        Api apiService = RetrofitClient.getInstance().getApi();
        Call<GenerateRecipeResponse> call = apiService.generateRecipe(ingredientsRequest);
        call.enqueue(new Callback<GenerateRecipeResponse>() {
            @Override
            public void onResponse(Call<GenerateRecipeResponse> call, Response<GenerateRecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GenerateRecipeResponse recipeResponse = response.body();
                    if (recipeResponse.getStructuredRecipe() != null) {
                        foodResponse.setText(recipeResponse.getStructuredRecipe().getTitle());
                        ingredientResponse.setText(formatList(recipeResponse.getStructuredRecipe().getIngredients()));
                        stepsResponse.setText(formatList(recipeResponse.getStructuredRecipe().getSteps()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Recipe data is missing.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenerateRecipeResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Prepare food items for calculation
        List<CalculateRequest.FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new CalculateRequest.FoodItem("Apple", 150));
        foodItems.add(new CalculateRequest.FoodItem("Banana", 120));
        foodItems.add(new CalculateRequest.FoodItem("Orange", 130));
        foodItems.add(new CalculateRequest.FoodItem("Potato", 200));
        foodItems.add(new CalculateRequest.FoodItem("Tomato", 100));

        // Call calculate API
        CalculateRequest calculateRequest = new CalculateRequest(foodItems);
        Log.d("CalculateRequest", calculateRequest.getFoodItems().toString());

        Call<CalculateResponse> callCalc = apiService.calculate(calculateRequest);
        callCalc.enqueue(new Callback<CalculateResponse>() {
            @Override
            public void onResponse(Call<CalculateResponse> call, Response<CalculateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    caloriesResponse.setText("Total Calories: " + response.body().getTotalCalories());
                    carbonEmissionsResponse.setText("Total Carbon Emission: " + response.body().getTotalCarbonEmission());
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalculateResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Show ingredients as a Toast for debugging
        Toast.makeText(getApplicationContext(), ingredients, Toast.LENGTH_SHORT).show();
    }

    // Helper method to format lists
    private String formatList(List<String> list) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            formatted.append(i + 1).append(". ").append(list.get(i)).append("\n");
        }
        return formatted.toString();
    }
}
