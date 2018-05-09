package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.baking.data.entities.Recipe;

import java.util.List;

public class RecipesRepository {
    private RecipesDao mRecipesDao;
    private LiveData<List<Recipe>> mAllRecipes;

    RecipesRepository(Application application) {
        RecipesRoomDatabase db = RecipesRoomDatabase.getDatabase(application);
        mRecipesDao = db.recipesDao();
        mAllRecipes = mRecipesDao.getAllRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    public void insert(Recipe recipe) {
        new InsertAsyncTask(mRecipesDao).execute(recipe);
    }

    private static class InsertAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipesDao mAsyncTaskDao;

        InsertAsyncTask(RecipesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
