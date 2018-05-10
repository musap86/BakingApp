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
    public int ingredientId;
    
    public String quantity;
    public String measure;
    public String ingredient;
    
    @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE)
    @ColumnInfo(name = "recipe_id")
    public int recipeId;
    
    @NonNull
    public int getIngredientId() {
        return ingredientId;
    }
    
    
}
