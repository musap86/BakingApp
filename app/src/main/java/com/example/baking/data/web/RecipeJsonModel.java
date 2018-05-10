package com.example.baking.data.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeJsonModel {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("ingredients")
    @Expose
    public List<IngredientJsonModel> ingredients = null;
    @SerializedName("steps")
    @Expose
    public List<StepJsonModel> steps = null;
    @SerializedName("servings")
    @Expose
    public String servings;
    @SerializedName("image")
    @Expose
    public String image;
    
    public class IngredientJsonModel {
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("measure")
        @Expose
        public String measure;
        @SerializedName("ingredient")
        @Expose
        public String ingredient;
    }
    
    public class StepJsonModel {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("shortDescription")
        @Expose
        public String shortDescription;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("videoURL")
        @Expose
        public String videoURL;
        @SerializedName("thumbnailURL")
        @Expose
        public String thumbnailURL;
    }
}