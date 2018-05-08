package com.example.baking.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.baking.BuildConfig;
import com.example.baking.R;
import com.example.baking.adapters.IListItemClickListener;
import com.example.baking.adapters.RecipeAdapter;
import com.example.baking.model.BakingAppJsonApi;
import com.example.baking.model.Recipe;

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
    @BindView(R.id.rv_select_recipe)
    RecyclerView mSelectRecipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Install the debug tree instance of Timber - a logger with a small, extensible API which
        // provides utility on top of Android's normal Log class.
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        RecipeAdapter adapter = new RecipeAdapter(MainActivity.this, null);
        mSelectRecipeRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mSelectRecipeRecyclerView.setLayoutManager(layoutManager);

        // Retrofit
        final String BASE_URL = "http://go.udacity.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingAppJsonApi bakingAppJsonApi = retrofit.create(BakingAppJsonApi.class);
        Call<List<Recipe>> call = bakingAppJsonApi.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                Timber.v(String.valueOf(response.code()));
                ArrayList<Recipe> recipes = (ArrayList<Recipe>) response.body();
                mSelectRecipeRecyclerView.setHasFixedSize(true);
                RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
                mSelectRecipeRecyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Timber.v(t);
                call.cancel();
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View itemView) {
        Toast.makeText(this, "Clicked index: " + clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
