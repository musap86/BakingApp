package com.example.baking.data.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@SuppressWarnings({"NullableProblems", "CanBeFinal", "WeakerAccess"})
@Entity(tableName = "recipes")
public class Recipe extends BakingEntity {
    @PrimaryKey
    @NonNull
    public int id;
    public String name;
    public String servings;
    public String image;
    
    public Recipe(@NonNull int id, String name, String servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }
    
}