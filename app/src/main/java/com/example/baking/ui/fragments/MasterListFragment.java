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
import com.example.baking.ui.adapters.IListItemClickListener;
import com.example.baking.ui.adapters.MasterListAdapter;

public class MasterListFragment extends Fragment {
    IListItemClickListener mCallBack;
    
    public MasterListFragment() {
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (IListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement IListItemClickListener");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        int id;
        try {
            id = getActivity().getIntent().getIntExtra("id", 1);
        } catch (NullPointerException e) {
            throw new NullPointerException(getActivity().toString() + ": doesn't have an intent");
        }
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_recipe_steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MasterListAdapter adapter = new MasterListAdapter(mCallBack, id);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}