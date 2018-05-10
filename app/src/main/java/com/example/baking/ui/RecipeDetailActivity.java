package com.example.baking.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.baking.R;
import com.example.baking.ui.adapters.IListItemClickListener;
import com.example.baking.ui.fragments.MasterListFragment;

import timber.log.Timber;

public class RecipeDetailActivity extends AppCompatActivity implements IListItemClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        MasterListFragment masterListFragment = new MasterListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.recipe_steps_container, masterListFragment).commit();
    }
    
    @Override
    public void onListItemClick(int clickedItemIndex, View itemView) {
        Timber.v("clicked : RecipeDetailActivity");
    }
}
