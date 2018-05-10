package com.example.baking.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("NullableProblems")
@Entity(tableName = "steps")
public class Step extends BakingEntity {
    
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int stepId;
    
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;
    
    @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE)
    @ColumnInfo(name = "recipe_id")
    public int recipeId;
    
    public Step(String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }
}
