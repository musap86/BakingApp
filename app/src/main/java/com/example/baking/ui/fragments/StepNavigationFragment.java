package com.example.baking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.baking.R;

public class StepNavigationFragment extends Fragment {
    public StepNavigationFragment() {
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_navigation, container, false);
        Button next = rootView.findViewById(R.id.button_next);
        Button previous = rootView.findViewById(R.id.button_previous);
        return rootView;
    }
}
