package com.example.baking.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.baking.BuildConfig;
import com.example.baking.R;
import com.example.baking.data.RecipesViewModel;
import com.example.baking.data.database.entity.Recipe;
import com.example.baking.data.web.WebUtils;
import com.example.baking.ui.adapters.IListItemClickListener;
import com.example.baking.ui.adapters.RecipeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

// Select a recipe.
public class MainActivity extends AppCompatActivity implements IListItemClickListener {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_recipe_select)
    RecyclerView mRecipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        // Set up Butter Knife.
        ButterKnife.bind(this);
    
        // Install the debug tree instance of Timber.
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    
        // Set up the recycler view.
        final RecipeAdapter adapter = new RecipeAdapter(this);
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
                Timber.v("Receiving database update from LiveData");
                adapter.setRecipes(recipes);
            }
        });
    
        if (WebUtils.isOnline(this)) {
            viewModel.refreshRecipes();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View itemView) {
        Toast.makeText(this, "Clicked index: " + clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
