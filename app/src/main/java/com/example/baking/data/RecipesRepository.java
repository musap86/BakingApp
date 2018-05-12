package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.baking.data.database.AppDatabase;
import com.example.baking.data.database.dao.BakingDao;
import com.example.baking.data.database.dao.IngredientsDao;
import com.example.baking.data.database.dao.RecipesDao;
import com.example.baking.data.database.dao.StepsDao;
import com.example.baking.data.database.entity.BakingEntity;
import com.example.baking.data.database.entity.Ingredient;
import com.example.baking.data.database.entity.Recipe;
import com.example.baking.data.database.entity.Step;
import com.example.baking.data.web.RecipeJsonModel;
import com.example.baking.data.web.Webservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesRepository {
    private final String BASE_URL = "http://go.udacity.com/";
    private RecipesDao mRecipesDao;
    private IngredientsDao mIngredientsDao;
    private StepsDao mStepsDao;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private Webservice mWebservice = retrofit.create(Webservice.class);

    RecipesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRecipesDao = db.recipesDao();
        mIngredientsDao = db.ingredientsDao();
        mStepsDao = db.stepsDao();
    }

    // Room executes all queries on a separate thread. Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Recipe>> getRecipes() {
        return mRecipesDao.getAll();
    }

    LiveData<List<Step>> getSteps(int recipeId) {
        return mStepsDao.getSteps(recipeId);
    }

    LiveData<Step> getStep(int id) {
        return mStepsDao.getStep(id);
    }

    LiveData<List<Ingredient>> getIngredients(int recipeId) {
        return mIngredientsDao.get(recipeId);
    }

    public void refreshRecipes() {
        Call<List<RecipeJsonModel>> call = mWebservice.getRecipes();
        call.enqueue(new Callback<List<RecipeJsonModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeJsonModel>> call, @NonNull Response<List<RecipeJsonModel>> response) {
                ArrayList<RecipeJsonModel> recipes = (ArrayList<RecipeJsonModel>) response.body();
                if (recipes != null) {
                    for (RecipeJsonModel r : recipes) {
                        Recipe recipe = new Recipe(r.id, r.name, r.servings, r.image);
                        insert(recipe);
                        int primaryKey = r.id * 100;
                        for (RecipeJsonModel.IngredientJsonModel i : r.ingredients) {
                            primaryKey++;
                            Ingredient ingredient = new Ingredient(primaryKey, i.quantity, i.measure, i.ingredient, r.id);
                            insert(ingredient);
                        }
                        primaryKey = r.id * 100;
                        for (RecipeJsonModel.StepJsonModel s : r.steps) {
                            primaryKey++;
                            Step step = new Step(primaryKey, s.id, s.shortDescription, s.description, s.videoURL, s.thumbnailURL, r.id);
                            insert(step);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeJsonModel>> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void insert(Recipe recipe) {
        new InsertAsyncTask(mRecipesDao).execute(recipe);
    }

    private void insert(Ingredient ingredient) {
        new InsertAsyncTask(mIngredientsDao).execute(ingredient);
    }

    private void insert(Step step) {
        new InsertAsyncTask(mStepsDao).execute(step);
    }

    private static class InsertAsyncTask extends AsyncTask<BakingEntity, Void, Void> {
        private BakingDao mAsyncTaskDao;

        InsertAsyncTask(BakingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BakingEntity... params) {
            if (params[0] instanceof Recipe) {
                RecipesDao dao = (RecipesDao) mAsyncTaskDao;
                dao.insert((Recipe) params[0]);
            } else if (params[0] instanceof Ingredient) {
                IngredientsDao dao = (IngredientsDao) mAsyncTaskDao;
                dao.insert((Ingredient) params[0]);
            } else if (params[0] instanceof Step) {
                StepsDao dao = (StepsDao) mAsyncTaskDao;
                dao.insert((Step) params[0]);
            }
            return null;
        }
    }
}
