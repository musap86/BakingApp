package com.example.baking.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.baking.data.database.dao.IngredientsDao;
import com.example.baking.data.database.dao.RecipesDao;
import com.example.baking.data.database.dao.StepsDao;
import com.example.baking.data.database.entity.Ingredient;
import com.example.baking.data.database.entity.Recipe;
import com.example.baking.data.database.entity.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "recipes_database";
    private static AppDatabase sInstance;
    
    // When the database is opened ...
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
    };
    
    public static AppDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return sInstance;
    }
    
    public abstract RecipesDao recipesDao();
    
    public abstract IngredientsDao ingredientsDao();
    
    public abstract StepsDao stepsDao();
}
