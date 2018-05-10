package com.example.baking.data.web;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {
    @GET("/android-baking-app-json")
    Call<List<RecipeJsonModel>> getRecipes();
}
