package com.example.baking.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.data.database.entities.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {
    
    @Query("SELECT * from recipes ORDER BY id")
    LiveData<List<Recipe>> getAllRecipes();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);
    
}
