package com.example.baking.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.baking.BuildConfig;
import com.example.baking.R;
import com.example.baking.data.RecipesViewModel;
import com.example.baking.data.entities.Recipe;
import com.example.baking.network.IJsonRetriever;
import com.example.baking.network.NetworkUtils;
import com.example.baking.network.RecipeJsonModel;
import com.example.baking.ui.adapters.IListItemClickListener;
import com.example.baking.ui.adapters.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

// Select a recipe.
public class MainActivity extends AppCompatActivity implements IListItemClickListener {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_recipe_select)
    RecyclerView mRecipesRecyclerView;
    private RecipesViewModel mRecipesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // When app first starts, the ViewModelProviders will create the ViewModel.
        // When the activity is destroyed, through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        mRecipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mRecipesViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });
        // Retrofit
        if (NetworkUtils.isOnline(this)) {
            final String BASE_URL = "http://go.udacity.com/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IJsonRetriever retriever = retrofit.create(IJsonRetriever.class);
            Call<List<RecipeJsonModel>> call = retriever.getRecipes();
            call.enqueue(new Callback<List<RecipeJsonModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<RecipeJsonModel>> call, @NonNull Response<List<RecipeJsonModel>> response) {
                    Timber.v(String.valueOf(response.code()));
                    ArrayList<RecipeJsonModel> recipes = (ArrayList<RecipeJsonModel>) response.body();
                    if (recipes != null) {
                        for (RecipeJsonModel r : recipes) {
                            Recipe recipe = new Recipe(r.id, r.name, r.servings, r.image);
                            mRecipesViewModel.insert(recipe);
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
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View itemView) {
        Toast.makeText(this, "Clicked index: " + clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
