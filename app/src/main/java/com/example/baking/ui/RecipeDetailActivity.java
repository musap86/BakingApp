package com.example.baking.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.example.baking.R;
import com.example.baking.ui.adapters.RecipeClickListener;
import com.example.baking.ui.fragments.MasterListFragment;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        MasterListFragment masterListFragment = new MasterListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.recipe_steps_container, masterListFragment).commit();
    }
    
    @Override
    public void onRecipeDataItemClick(int id) {
    }
}
