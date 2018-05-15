package com.example.baking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking.R;
import com.example.baking.data.database.entity.Step;
import com.example.baking.ui.adapters.RecipeDetailAdapter;
import com.example.baking.ui.adapters.RecipeStepClickListener;

import java.util.List;
import java.util.Objects;

public class RecipeDetailFragment extends Fragment {

    private RecipeStepClickListener mCallBack;

    public RecipeDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (RecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_recipe_steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final RecipeDetailAdapter adapter = new RecipeDetailAdapter(mCallBack);
        recyclerView.setAdapter(adapter);

        List<Step> steps = Objects.requireNonNull(getArguments()).getParcelableArrayList("steps");

        adapter.setRecipes(steps);

        return rootView;
    }
}
