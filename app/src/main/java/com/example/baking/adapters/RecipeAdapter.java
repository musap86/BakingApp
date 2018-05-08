package com.example.baking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final IListItemClickListener mOnClickListener;
    private final ArrayList<Recipe> mRecipeList;

    public RecipeAdapter(IListItemClickListener listItemClickListener, ArrayList<Recipe> recipes) {
        mOnClickListener = listItemClickListener;
        if (recipes == null) {
            mRecipeList = new ArrayList<>();
        } else {
            mRecipeList = recipes;
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView mRecipeNameTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            mRecipeNameTextView.setText(mRecipeList.get(position).name);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition, itemView);
        }
    }
}
