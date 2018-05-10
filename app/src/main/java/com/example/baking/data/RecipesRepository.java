package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.baking.data.database.AppDatabase;
import com.example.baking.data.database.RecipesDao;
import com.example.baking.data.database.entities.Recipe;
import com.example.baking.data.web.RecipeJsonModel;
import com.example.baking.data.web.Webservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RecipesRepository {
    private final String BASE_URL = "http://go.udacity.com/";
    private RecipesDao mRecipesDao;
    private LiveData<List<Recipe>> mAllRecipes;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private Webservice mWebservice = retrofit.create(Webservice.class);
    
    RecipesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRecipesDao = db.recipesDao();
        mAllRecipes = mRecipesDao.getAllRecipes();
    }
    
    // Room executes all queries on a separate thread. Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }
    
    public void refreshRecipes() {
        Call<List<RecipeJsonModel>> call = mWebservice.getRecipes();
        call.enqueue(new Callback<List<RecipeJsonModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeJsonModel>> call, @NonNull Response<List<RecipeJsonModel>> response) {
                Timber.v(String.valueOf(response.code()));
                ArrayList<RecipeJsonModel> recipes = (ArrayList<RecipeJsonModel>) response.body();
                if (recipes != null) {
                    for (RecipeJsonModel r : recipes) {
                        Recipe recipe = new Recipe(r.id, r.name, r.servings, r.image);
                        insert(recipe);
                    }
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<List<RecipeJsonModel>> call, @NonNull Throwable t) {
                Timber.v(t);
                call.cancel();
            }
        });
    }
    
    private void insert(Recipe recipe) {
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
