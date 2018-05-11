package com.example.baking.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.baking.data.database.entity.Step;

import java.util.List;

public class StepsViewModel extends AndroidViewModel {
    private RecipesRepository mRepository;

    public StepsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RecipesRepository(application);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return mRepository.getSteps(recipeId);
    }
}
