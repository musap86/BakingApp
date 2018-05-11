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

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeStepsViewHolder> {
    private final RecipeClickListener mOnClickListener;
    private List<Step> mSteps;

    public MasterListAdapter(RecipeClickListener recipeClickListener) {
        mOnClickListener = recipeClickListener;
    }
    
    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_description_item, parent, false);
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
        private int stepId;

        private RecipeStepsViewHolder(View itemView) {
            super(itemView);
            stepDescriptionTextView = itemView.findViewById(R.id.tv_step_description);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            Step current = mSteps.get(position);
            stepId = current.stepId;
            stepDescriptionTextView.setText(current.shortDescription);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onRecipeDataItemClick(stepId);
        }

    }
}
