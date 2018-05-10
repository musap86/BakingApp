package com.example.baking.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.data.database.entity.Ingredient;

import java.util.List;

@Dao
public interface IngredientsDao extends BakingDao {
    
    @Query("SELECT * from ingredients ORDER BY ingredientId")
    LiveData<List<Ingredient>> getAll();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);
    
}
