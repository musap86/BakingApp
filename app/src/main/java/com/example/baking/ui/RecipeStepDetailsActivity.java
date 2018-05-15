package com.example.baking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.baking.R;
import com.example.baking.data.RecipeViewModel;
import com.example.baking.data.database.entity.Ingredient;
import com.example.baking.ui.adapters.RecipeDetailAdapter;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;
import com.example.baking.ui.fragments.StepNavigationFragment;
import com.example.baking.ui.fragments.StepNavigator;

import java.util.Objects;

public class RecipeStepDetailsActivity extends AppCompatActivity implements StepNavigator {
    private int mId;
    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);

        // Change ActionBar title as the name of the recipe.
        String recipeName = getIntent().getStringExtra("name");
        if (recipeName != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipeName);
        }

        if (savedInstanceState != null) {
            // Be sure that step detail view shows the same step before rotation.
            mId = savedInstanceState.getInt("id");
            // ...or same ingredients.
            mRecipeId = savedInstanceState.getInt("recipeId");
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
        if (id == RecipeDetailAdapter.INGREDIENT_ID) {
            viewModel.getIngredients(mRecipeId).observe(this, ingredients -> {
                Bundle bundle = new Bundle();
                StringBuilder ingredientList = new StringBuilder();
                ingredientList.append(getString(R.string.ingredients)).append("\n\n");
                for (Ingredient ingredient : Objects.requireNonNull(ingredients)) {
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
            });
        } else {
            final int stepCount = getIntent().getIntExtra("stepCount", 1);
            viewModel.getStep(id).observe(this, step -> {
                Bundle bundle = new Bundle();
                bundle.putString("video", Objects.requireNonNull(step).videoURL);
                bundle.putString("thumbnail", step.thumbnailURL);
                bundle.putString("description", step.description);
                bundle.putInt("stepCount", stepCount);
                bundle.putInt("id", id);
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
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", mId);
        outState.putInt("recipeId", mRecipeId);
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
