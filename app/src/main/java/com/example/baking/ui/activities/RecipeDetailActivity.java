package com.example.baking.ui.activities;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.baking.R;
import com.example.baking.data.RecipeViewModel;
import com.example.baking.data.database.entities.Ingredient;
import com.example.baking.data.database.entities.Step;
import com.example.baking.ui.adapters.RecipeDetailAdapter;
import com.example.baking.ui.adapters.RecipeStepClickListener;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.RecipeDetailFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;
import com.example.baking.ui.widget.RecipeIngredientsWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepClickListener {
    private int mId;
    private int mRecipeId;
    private int mStepCount;
    private String mRecipeName;
    private boolean mTwoPane;
    private RecipeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Track if the activity has a two-pane layout or not.
        mTwoPane = findViewById(R.id.linear_layout_step_details) != null;

        // Change ActionBar title as the name of the recipe.
        String recipeName = getIntent().getStringExtra("name");
        if (recipeName != null) {
            mRecipeName = recipeName;
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipeName);
        }

        if (savedInstanceState != null) {
            // Be sure that step detail view shows the same step before rotation.
            mId = savedInstanceState.getInt("id");
            // ...or same ingredients.
            mRecipeId = savedInstanceState.getInt("recipeId");
        } else {
            // Get database id of selected recipe.
            mRecipeId = getIntent().getIntExtra("id", 1);
            // Database table id of any first step is a result of the following simple math.
            mId = mRecipeId * 100 + 1;
        }

        mViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        boolean isStartedByWidget = getIntent().getBooleanExtra("widget", false);
        if (isStartedByWidget) {
            populateRecipeIngredientWidget();
        } else {
            mViewModel.getSteps(mRecipeId).observe(this, this::populateRecipeSteps);
        }
    }

    private void populateRecipeSteps(List<Step> steps) {
        // Step count is used to see if any step has a previous or next one.
        mStepCount = Objects.requireNonNull(steps).size();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", (ArrayList<Step>) steps);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_steps_container, recipeDetailFragment).commit();

        if (mTwoPane) {
            populateStepDetailsPane(mId);
        }
    }

    private void populateStepDetailsPane(final int id) {
        // Video or ingredients are seen in media player container
        // depending on the clicked item of recycler view of recipe details.
        final MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        View instructionsView = findViewById(R.id.step_instruction_container);
        if (id == RecipeDetailAdapter.INGREDIENT_ID) {
            instructionsView.setVisibility(View.GONE);
            mViewModel.getIngredients(mRecipeId).observe(this, ingredients -> {
                Bundle bundle = new Bundle();
                StringBuilder ingredientList = new StringBuilder();
                ingredientList.append(mRecipeName).append(" ")
                        .append(getString(R.string.ingredients)).append("\n\n");
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
            instructionsView.setVisibility(View.VISIBLE);
            final int stepCount = mStepCount;
            mViewModel.getStep(id).observe(this, step -> {
                Bundle bundle = new Bundle();
                bundle.putString("video", Objects.requireNonNull(step).videoURL);
                bundle.putString("thumbnail", step.thumbnailURL);
                bundle.putString("description", step.description);
                bundle.putInt("stepCount", stepCount);
                bundle.putInt("id", id);
                StepInstructionFragment stepInstructionFragment = new StepInstructionFragment();
                stepInstructionFragment.setArguments(bundle);
                mediaPlayerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.media_player_container, mediaPlayerFragment)
                        .replace(R.id.step_instruction_container, stepInstructionFragment)
                        .commit();
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
    public void onRecipeStepClick(int generatedStepId) {
        if (mTwoPane) {
            mId = generatedStepId;
            populateStepDetailsPane(generatedStepId);
        } else {
            Intent intent = new Intent(this, RecipeStepDetailsActivity.class);
            intent.putExtra("id", generatedStepId);
            intent.putExtra("name", mRecipeName);
            intent.putExtra("stepCount", mStepCount);
            intent.putExtra("recipeId", mRecipeId);
            startActivity(intent);
        }
    }

    private void populateRecipeIngredientWidget() {
        mViewModel.getIngredients(mRecipeId).observe(this, ingredients -> {
            StringBuilder ingredientList = new StringBuilder();
            ingredientList.append(mRecipeName).append(" ")
                    .append(getString(R.string.ingredients)).append("\n\n");
            for (Ingredient ingredient : Objects.requireNonNull(ingredients)) {
                ingredientList
                        .append("* ")
                        .append(ingredient.quantity).append(" ")
                        .append(ingredient.measure).append(" ")
                        .append(ingredient.ingredient).append("\n");
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeDetailActivity.this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(RecipeDetailActivity.this, RecipeIngredientsWidget.class));
            RecipeIngredientsWidget.updateRecipeIngredientsWidget(RecipeDetailActivity.this, appWidgetManager,
                    ingredientList.toString(), appWidgetIds);

            finish();
            moveTaskToBack(true);
        });
    }
}
