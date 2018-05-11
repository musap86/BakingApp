package com.example.baking.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.baking.R;
import com.example.baking.data.RecipesViewModel;
import com.example.baking.data.database.entity.Recipe;
import com.example.baking.data.web.WebUtils;
import com.example.baking.ui.adapters.RecipeCardsAdapter;
import com.example.baking.ui.adapters.RecipeClickListener;

import java.util.List;

// Select a recipe.
public class MainActivity extends AppCompatActivity implements RecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up a recycler view for recipe cards.
        final RecipeCardsAdapter adapter = new RecipeCardsAdapter(this);
        RecyclerView mRecipesRecyclerView = findViewById(R.id.rv_recipe_select);
        mRecipesRecyclerView.setAdapter(adapter);
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipesRecyclerView.setHasFixedSize(true);

        // Use ViewModelProviders to associate ViewModel with UI controller.
        // * When app first starts, the ViewModelProviders will create the ViewModel.
        // * When the activity is destroyed, through a configuration change, the ViewModel persists.
        // * When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

        viewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        if (WebUtils.isOnline(this)) {
            viewModel.refreshRecipes();
        }
    }

    @Override
    public void onRecipeDataItemClick(int id) {
        Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
