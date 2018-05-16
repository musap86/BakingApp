package com.example.baking.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings({"NullableProblems", "WeakerAccess", "CanBeFinal"})
@Entity(tableName = "ingredients")
public class Ingredient extends BakingEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    public int id;
    public String quantity;
    public String measure;
    public String ingredient;
    @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE)
    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    public Ingredient(@NonNull int id, String quantity, String measure, String ingredient, int recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }
}
