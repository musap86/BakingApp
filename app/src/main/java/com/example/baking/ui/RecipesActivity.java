package com.example.baking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.data.RecipeViewModel;
import com.example.baking.data.web.WebUtils;
import com.example.baking.ui.adapters.RecipeAdapter;
import com.example.baking.ui.adapters.RecipeClickListener;

// Select a recipe.
public class RecipesActivity extends AppCompatActivity implements RecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscapeTablet = getResources().getBoolean(R.bool.isLandscapeTablet);
        // Set up a recycler view for recipe cards.
        final RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        RecyclerView mRecipeRecyclerView = findViewById(R.id.rv_recipe_select);
        mRecipeRecyclerView.setAdapter(recipeAdapter);
        if (isLandscapeTablet) {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else if (isTablet) {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mRecipeRecyclerView.setHasFixedSize(true);

        // Use ViewModelProviders to associate ViewModel with UI controller.
        // * When app first starts, the ViewModelProviders will create the ViewModel.
        // * When the activity is destroyed, through a configuration change, the ViewModel persists.
        // * When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        viewModel.getAllRecipes().observe(this, recipeAdapter::setRecipes);

        if (WebUtils.isOnline(this)) {
            viewModel.refreshRecipes();
        }
    }

    @Override
    public void onRecipeClick(int recipeId, String recipeName) {
        Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra("id", recipeId);
        intent.putExtra("name", recipeName);
        boolean isStartedByWidget = getIntent().getBooleanExtra("widget", false);
        intent.putExtra("widget", isStartedByWidget);
        startActivity(intent);
    }
}
