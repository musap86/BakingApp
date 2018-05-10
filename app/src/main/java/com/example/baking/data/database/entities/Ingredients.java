package com.example.baking.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("NullableProblems")
@Entity(tableName = "ingredients")
public class Ingredients {
    @PrimaryKey
    @NonNull
    private int ingredientId;
    
    private String quantity;
    private String measure;
    private String ingredient;
    
    @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE)
    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    
    @NonNull
    public int getIngredientId() {
        return ingredientId;
    }
    
    public void setIngredientId(@NonNull int ingredientId) {
        this.ingredientId = ingredientId;
    }
    
    public String getQuantity() {
        return quantity;
    }
    
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    public String getMeasure() {
        return measure;
    }
    
    public void setMeasure(String measure) {
        this.measure = measure;
    }
    
    public String getIngredient() {
        return ingredient;
    }
    
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
    
    public int getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
