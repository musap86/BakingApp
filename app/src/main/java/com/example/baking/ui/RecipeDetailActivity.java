package com.example.baking.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.baking.R;
import com.example.baking.data.RecipesViewModel;
import com.example.baking.data.database.entity.Ingredient;
import com.example.baking.data.database.entity.Step;
import com.example.baking.ui.adapters.RecipeStepClickListener;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.RecipeDetailFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepClickListener {
    private int mStepCount;
    private String mRecipeName;
    private boolean mTwoPane;
    private RecipesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mTwoPane = findViewById(R.id.linear_layout_step_details) != null;

        String recipeName = getIntent().getStringExtra("name");
        if (recipeName != null) {
            mRecipeName = recipeName;
            getSupportActionBar().setTitle(recipeName);
        }

        final RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int id = getIntent().getIntExtra("id", 1);
        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        final RecipesViewModel viewModel = mViewModel;
        viewModel.getSteps(id).observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable final List<Step> steps) {
                mStepCount = steps.size();
                viewModel.getIngredients(id).observe(RecipeDetailActivity.this, new Observer<List<Ingredient>>() {
                    @Override
                    public void onChanged(@Nullable List<Ingredient> ingredients) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("steps", (ArrayList<Step>) steps);
                        bundle.putParcelableArrayList("ingredients", (ArrayList<Ingredient>) ingredients);
                        recipeDetailFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.recipe_steps_container, recipeDetailFragment).commit();

                        if (mTwoPane) {
                            int defaultStepId = id * 100 + 1;
                            populateStepDetails(defaultStepId);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onRecipeStepClick(int databaseGeneratedStepId) {
        if (mTwoPane) {
            populateStepDetails(databaseGeneratedStepId);
        } else {
            Intent intent = new Intent(RecipeDetailActivity.this, RecipeStepDetailsActivity.class);
            intent.putExtra("id", databaseGeneratedStepId);
            intent.putExtra("name", mRecipeName);
            intent.putExtra("stepCount", mStepCount);
            startActivity(intent);
        }
    }

    private void populateStepDetails(final int id) {
        final MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        final StepInstructionFragment stepInstructionFragment = new StepInstructionFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int stepCount = mStepCount;
        mViewModel.getStep(id).observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                Bundle bundle = new Bundle();
                bundle.putString("video", step.videoURL);
                bundle.putString("thumbnail", step.thumbnailURL);
                bundle.putString("description", step.description);
                bundle.putInt("stepCount", stepCount);
                bundle.putInt("id", id);
                mediaPlayerFragment.setArguments(bundle);
                stepInstructionFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.media_player_container, mediaPlayerFragment).commit();
                fragmentManager.beginTransaction().replace(R.id.step_instruction_container, stepInstructionFragment).commit();
            }
        });
    }
}
