package com.example.baking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.baking.R;

import java.util.Objects;

public class StepNavigationFragment extends Fragment {
    private StepNavigator navigator;

    public StepNavigationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigator = (StepNavigator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement StepNavigator");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_navigation, container, false);

        Button next = rootView.findViewById(R.id.button_next);
        Button previous = rootView.findViewById(R.id.button_previous);

        int id = Objects.requireNonNull(getArguments()).getInt("id");
        id = id % 100;

        int stepCount = getArguments().getInt("stepCount");

        if (id == 1) {
            previous.setVisibility(View.GONE);
        } else if (id < stepCount) {
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.GONE);
        }

        next.setOnClickListener(v -> navigator.goToNextStep());

        previous.setOnClickListener(v -> navigator.goToPreviousStep());

        return rootView;
    }
}
