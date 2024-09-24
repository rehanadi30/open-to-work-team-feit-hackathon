package com.hackafun.foodliner.interfaces;

import com.hackafun.foodliner.models.CalculateRequest;
import com.hackafun.foodliner.models.CalculateResponse;
import com.hackafun.foodliner.models.DefaultResponse;
import com.hackafun.foodliner.models.GenerateFoodResponse;
import com.hackafun.foodliner.models.GenerateRecipeResponse;
import com.hackafun.foodliner.models.IngredientsRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    @Multipart
    @POST("generate-food")
    Call<GenerateFoodResponse> generateFood(
            @Part MultipartBody.Part image
    );

    @POST("generate-recipe")
    Call<GenerateRecipeResponse> generateRecipe(
            @Body IngredientsRequest ingredientsRequest
    );

    @POST("calculation")
    Call<CalculateResponse> calculate(
            @Body CalculateRequest calculateRequest
    );
}