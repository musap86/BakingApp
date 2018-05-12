package com.example.baking.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.data.database.entity.Step;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeStepsViewHolder> {
    private final RecipeStepClickListener mOnClickListener;
    private List<Step> mSteps;

    public RecipeDetailAdapter(RecipeStepClickListener recipeStepClickListener) {
        mOnClickListener = recipeStepClickListener;
    }
    
    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        if (mSteps != null) {
            holder.bind(position);
        }
    }
    
    @Override
    public int getItemCount() {
        if (mSteps != null) {
            return mSteps.size();
        } else {
            return 0;
        }
    }

    public void setRecipes(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView stepDescriptionTextView;
        private int id;

        private RecipeStepsViewHolder(View itemView) {
            super(itemView);
            stepDescriptionTextView = itemView.findViewById(R.id.tv_step_description);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            Step current = mSteps.get(position);
            id = current.id;
            stepDescriptionTextView.setText(current.shortDescription);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onRecipeStepClick(id);
        }

    }
}
