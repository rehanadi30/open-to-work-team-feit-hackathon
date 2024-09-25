package com.hackafun.foodliner.interfaces;

import com.hackafun.foodliner.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("top-headlines")
    Call<DefaultResponse> getAllNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}