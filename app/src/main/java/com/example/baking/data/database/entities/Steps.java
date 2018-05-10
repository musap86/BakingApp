package com.example.baking.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("NullableProblems")
@Entity(tableName = "steps")
public class Steps {
    @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE)
    @ColumnInfo(name = "recipe_id")
    public int recipeId;
    @PrimaryKey
    @NonNull
    private String stepId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    
    @NonNull
    public String getStepId() {
        return stepId;
    }
    
    public void setStepId(@NonNull String stepId) {
        this.stepId = stepId;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVideoURL() {
        return videoURL;
    }
    
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    
    public String getThumbnailURL() {
        return thumbnailURL;
    }
    
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
    
    public int getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
