package com.example.baking.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingAppJsonApi {
    @GET("/android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
