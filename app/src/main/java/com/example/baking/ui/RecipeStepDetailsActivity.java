package com.example.baking.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.baking.R;
import com.example.baking.data.RecipesViewModel;
import com.example.baking.data.database.entity.Step;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;
import com.example.baking.ui.fragments.StepNavigationFragment;
import com.example.baking.ui.fragments.StepNavigator;

public class RecipeStepDetailsActivity extends AppCompatActivity implements StepNavigator {

    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);

        String recipeName = getIntent().getStringExtra("name");
        if (recipeName != null) {
            getSupportActionBar().setTitle(recipeName);
        }

        mId = getIntent().getIntExtra("id", 101);
        populateStepDetails(mId);
    }

    private void populateStepDetails(final int id) {
        final MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        final StepInstructionFragment stepInstructionFragment = new StepInstructionFragment();
        final StepNavigationFragment stepNavigationFragment = new StepNavigationFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int stepCount = getIntent().getIntExtra("stepCount", 1);
        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        viewModel.getStep(id).observe(this, new Observer<Step>() {
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
                stepNavigationFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.media_player_container, mediaPlayerFragment).commit();
                fragmentManager.beginTransaction().replace(R.id.step_instruction_container, stepInstructionFragment).commit();
                fragmentManager.beginTransaction().replace(R.id.step_navigation_container, stepNavigationFragment).commit();
            }
        });
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
