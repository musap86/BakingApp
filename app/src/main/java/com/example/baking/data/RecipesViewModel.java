package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.baking.data.entities.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipesRepository mRepository;

    private LiveData<List<Recipe>> mAllRecipes;

    public RecipesViewModel(Application application) {
        super(application);
        mRepository = new RecipesRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    public void insert(Recipe recipe) {
        mRepository.insert(recipe);
    }
}
