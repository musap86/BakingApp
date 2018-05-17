package com.example.baking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking.R;
import com.example.baking.data.database.entities.Step;
import com.example.baking.ui.activities.RecipeDetailActivity;
import com.example.baking.ui.adapters.RecipeDetailAdapter;
import com.example.baking.ui.adapters.RecipeStepClickListener;

import java.util.List;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();
    private static final String FIRST_ITEM = "state";
    private LinearLayoutManager mLayoutManager;
    private RecipeStepClickListener mCallBack;
    private int mFirstVisibleItemPos;

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
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        final RecipeDetailAdapter adapter = new RecipeDetailAdapter(mCallBack);
        recyclerView.setAdapter(adapter);

        Bundle arguments = getArguments();
        if (arguments != null) {
            List<Step> steps = arguments.getParcelableArrayList("steps");
            adapter.setRecipes(steps);
            savedInstanceState = arguments.getBundle("savedInstanceState");
            if (savedInstanceState != null) {
                mFirstVisibleItemPos = savedInstanceState.getInt(FIRST_ITEM);
                mLayoutManager.scrollToPosition(mFirstVisibleItemPos);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        mFirstVisibleItemPos = mLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(FIRST_ITEM, mFirstVisibleItemPos);

        RecipeDetailActivity detailActivity = null;
        try {
            detailActivity = (RecipeDetailActivity) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }

        if (detailActivity != null) {
            detailActivity.fromRecipeDetailFragment(outState);
        }
    }
}
