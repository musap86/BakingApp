package com.example.baking.data.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@SuppressWarnings("NullableProblems")
@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    @NonNull
    private int id;
    private String name;
    private String servings;
    private String image;
    
    public Recipe(@NonNull int id, String name, String servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }
    
    @NonNull
    public int getId() {
        return id;
    }
    
    public void setId(@NonNull int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getServings() {
        return servings;
    }
    
    public void setServings(String servings) {
        this.servings = servings;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
}