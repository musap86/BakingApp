package com.example.baking.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.baking.data.database.entity.Step;

import java.util.List;

@Dao
public interface StepsDao extends BakingDao {
    
    @Query("SELECT * from steps ORDER BY stepId")
    LiveData<List<Step>> getAll();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Step step);
    
}
