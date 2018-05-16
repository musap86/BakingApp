package com.example.baking.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.data.database.entities.Ingredient;

import java.util.List;

@Dao
public interface IngredientsDao extends BakingDao {

    @Query("SELECT * from ingredients WHERE recipe_id = :recipeId ORDER BY _id")
    LiveData<List<Ingredient>> get(int recipeId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);
    
}
