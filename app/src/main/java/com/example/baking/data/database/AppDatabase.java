package com.example.baking.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.baking.data.database.entities.Ingredients;
import com.example.baking.data.database.entities.Recipe;
import com.example.baking.data.database.entities.Steps;

@Database(entities = {Recipe.class, Ingredients.class, Steps.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "recipes_database";
    private static AppDatabase sInstance;
    
    // When the database is opened ...
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(sInstance).execute();
        }
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
    
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final RecipesDao mDao;
        
        PopulateDbAsync(AppDatabase db) {
            mDao = db.recipesDao();
        }
        
        @Override
        protected Void doInBackground(final Void... params) {
            // ... do something.
            return null;
        }
    }
}
