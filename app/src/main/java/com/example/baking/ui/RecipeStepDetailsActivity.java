package com.example.baking.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.example.baking.R;
import com.example.baking.ui.fragments.MediaPlayerFragment;
import com.example.baking.ui.fragments.StepInstructionFragment;
import com.example.baking.ui.fragments.StepNavigationFragment;

public class RecipeStepDetailsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        
        MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.media_player_container, mediaPlayerFragment).commit();
        
        StepInstructionFragment stepInstructionFragment = new StepInstructionFragment();
        fragmentManager.beginTransaction().replace(R.id.step_instruction_container, stepInstructionFragment).commit();
        
        StepNavigationFragment stepNavigationFragment = new StepNavigationFragment();
        fragmentManager.beginTransaction().replace(R.id.step_navigation_container, stepNavigationFragment).commit();
    }
}
