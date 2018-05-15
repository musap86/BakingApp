package com.example.baking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;

import java.util.Objects;

public class StepInstructionFragment extends Fragment {

    public StepInstructionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_instruction, container, false);

        TextView textView = rootView.findViewById(R.id.tv_step_instruction);

        String description = Objects.requireNonNull(getArguments()).getString("description");

        textView.setText(description);

        return rootView;
    }
}
