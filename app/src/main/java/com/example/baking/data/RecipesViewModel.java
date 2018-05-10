package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.baking.data.database.entities.Recipe;

import java.util.List;

// The ViewModel's role is to provide data to the UI and survive configuration changes.
// A ViewModel acts as a communication center between the RecipesRepository and the UI.
public class RecipesViewModel extends AndroidViewModel {
    // In the ViewModel, use LiveData for changeable data that the UI will use or display.
    private LiveData<List<Recipe>> mAllRecipes;
    private RecipesRepository mRepository;
    
    // Warning: Never pass context into ViewModel instances.
    // Do not store Activity, Fragment, or View instances or their Context in the ViewModel.
    // If you need the application context, use AndroidViewModel.
    public RecipesViewModel(Application application) {
        super(application);
        mRepository = new RecipesRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
    }
    
    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }
    
    public void refreshRecipes() {
        mRepository.refreshRecipes();
    }
}
