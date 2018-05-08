package com.example.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.bakingapp.BuildConfig;
import com.example.bakingapp.R;
import com.example.bakingapp.adapters.IListItemClickListener;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.utilities.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

// Select a recipe.
public class MainActivity extends AppCompatActivity implements IListItemClickListener {
    @BindView(R.id.rv_select_recipe)
    RecyclerView mSelectRecipeRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecipeAdapter mAdapter;

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

        mLayoutManager = new LinearLayoutManager(this);
        mSelectRecipeRecyclerView.setLayoutManager(mLayoutManager);
        mSelectRecipeRecyclerView.setHasFixedSize(true);
        ArrayList<String> arrayList = JsonUtils.getRecipeNames(JsonUtils.BAKE_JSON_STRING);
        mAdapter = new RecipeAdapter(this, arrayList);
        mSelectRecipeRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View itemView) {
        Toast.makeText(this, "Clicked index: " + clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
