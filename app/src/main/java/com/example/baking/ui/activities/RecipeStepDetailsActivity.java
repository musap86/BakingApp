package com.example.baking.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.baking.R;
import com.example.baking.data.RecipeViewModel;
import com.example.baking.data.database.entities.Ingredient;
import com.example.baking.ui.adapters.RecipeDetailAdapter;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;
import com.example.baking.ui.fragments.StepNavigationFragment;
import com.example.baking.ui.fragments.StepNavigator;

public class RecipeStepDetailsActivity extends AppCompatActivity implements StepNavigator {
    private int mId;
    private int mRecipeId;
    private Bundle mMediaFragmentSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);

        // Change ActionBar title as the name of the recipe.
        String recipeName = getIntent().getStringExtra("name");
        if (recipeName != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setTitle(recipeName);
            }
        }

        if (savedInstanceState != null) {
            // Be sure that step detail view shows the same step before rotation.
            mId = savedInstanceState.getInt("id");
            // ...or same ingredients.
            mRecipeId = savedInstanceState.getInt("recipeId");
            // Video state of the media player fragment.
            mMediaFragmentSavedInstanceState = savedInstanceState.getBundle("savedInstanceState");
        } else {
            // Database table id of the first step is 101.
            mId = getIntent().getIntExtra("id", 101);
            // Get database id of selected recipe.
            mRecipeId = getIntent().getIntExtra("recipeId", 1);
        }

        populateStepDetails(mId);
    }

    private void populateStepDetails(final int id) {
        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        // Video or ingredients are seen in media player container
        // depending on the clicked item of recycler view of recipe details.
        final MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        View instructionsView = findViewById(R.id.step_instruction_container);
        if (id == RecipeDetailAdapter.INGREDIENT_ID) {
            if (instructionsView != null) {
                instructionsView.setVisibility(View.GONE);
            }
            viewModel.getIngredients(mRecipeId).observe(this, ingredients -> {
                if (ingredients != null) {
                    Bundle bundle = new Bundle();
                    StringBuilder ingredientList = new StringBuilder();
                    ingredientList.append(getString(R.string.ingredients)).append("\n\n");
                    for (Ingredient ingredient : ingredients) {
                        ingredientList
                                .append("* ")
                                .append(ingredient.quantity).append(" ")
                                .append(ingredient.measure).append(" ")
                                .append(ingredient.ingredient).append("\n");
                    }
                    bundle.putString("ingredients", ingredientList.toString());
                    mediaPlayerFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.media_player_container, mediaPlayerFragment)
                            .commit();
                }
            });
        } else {
            if (instructionsView != null) {
                instructionsView.setVisibility(View.VISIBLE);
            }
            final int stepCount = getIntent().getIntExtra("stepCount", 1);
            viewModel.getStep(id).observe(this, step -> {
                if (step != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("video", step.videoURL);
                    bundle.putString("thumbnail", step.thumbnailURL);
                    bundle.putString("description", step.description);
                    bundle.putInt("stepCount", stepCount);
                    bundle.putInt("id", id);
                    if (mMediaFragmentSavedInstanceState != null) {
                        // Another step should start from the beginning.
                        int stepId = mMediaFragmentSavedInstanceState.getInt("stepId");
                        if (stepId != mId) {
                            mMediaFragmentSavedInstanceState = null;
                        }
                    }
                    bundle.putBundle("savedInstanceState", mMediaFragmentSavedInstanceState);
                    mediaPlayerFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.media_player_container, mediaPlayerFragment)
                            .commit();
                    if (findViewById(R.id.step_instruction_container) != null) {
                        StepInstructionFragment stepInstructionFragment = new StepInstructionFragment();
                        StepNavigationFragment stepNavigationFragment = new StepNavigationFragment();
                        stepInstructionFragment.setArguments(bundle);
                        stepNavigationFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_instruction_container, stepInstructionFragment)
                                .replace(R.id.step_navigation_container, stepNavigationFragment)
                                .commit();
                    }
                }
            });
        }
    }

    public void fromMediaFragment(Bundle fragmentOutState) {
        mMediaFragmentSavedInstanceState = fragmentOutState;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", mId);
        outState.putInt("recipeId", mRecipeId);
        outState.putBundle("savedInstanceState", mMediaFragmentSavedInstanceState);
    }

    @Override
    public void goToPreviousStep() {
        mId--;
        populateStepDetails(mId);
    }

    @Override
    public void goToNextStep() {
        mId++;
        populateStepDetails(mId);
    }
}
