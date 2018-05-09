package com.example.baking.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.baking.data.entities.Ingredients;
import com.example.baking.data.entities.Recipe;
import com.example.baking.data.entities.Steps;

@Database(entities = {Recipe.class, Ingredients.class, Steps.class}, version = 1)
public abstract class RecipesRoomDatabase extends RoomDatabase {
    private static RecipesRoomDatabase INSTANCE;
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    static RecipesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesRoomDatabase.class, "recipes_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RecipesDao recipesDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final RecipesDao mDao;

        PopulateDbAsync(RecipesRoomDatabase db) {
            mDao = db.recipesDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }
    }
}
