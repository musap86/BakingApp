package com.example.baking.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IJsonRetriever {
    @GET("/android-baking-app-json")
    Call<List<RecipeJsonModel>> getRecipes();
}
