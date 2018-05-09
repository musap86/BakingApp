package com.example.baking.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.data.entities.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Query("SELECT * from recipes")
    LiveData<List<Recipe>> getAllRecipes();
}
