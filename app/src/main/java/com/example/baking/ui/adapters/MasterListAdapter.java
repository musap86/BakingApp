package com.example.baking.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeStepsViewHolder> {
    private final IListItemClickListener mOnClickListener;
    private int mId;
    
    public MasterListAdapter(IListItemClickListener listItemClickListener, int id) {
        mOnClickListener = listItemClickListener;
        mId = id;
    }
    
    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new MasterListAdapter.RecipeStepsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        holder.recipeNameTextView.setText(String.valueOf(mId));
    }
    
    @Override
    public int getItemCount() {
        return 1;
    }
    
    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView recipeNameTextView;
        
        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition, itemView);
        }
    }
}
