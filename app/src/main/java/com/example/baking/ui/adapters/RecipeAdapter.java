package com.example.baking.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.data.database.entity.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeCardViewHolder> {
    private final RecipeClickListener mRecipeClickListener;
    private List<Recipe> mRecipes;

    public RecipeAdapter(RecipeClickListener recipeClickListener) {
        mRecipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RecipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_card_item, parent, false);
        return new RecipeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardViewHolder holder, int position) {
        if (mRecipes != null) {
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        } else {
            return 0;
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeCardViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView recipeNameTextView;
        private int recipeId;
        private String recipeName;

        private RecipeCardViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            Recipe current = mRecipes.get(position);
            recipeId = current.id;
            recipeName = current.name;
            recipeNameTextView.setText(recipeName);
        }

        @Override
        public void onClick(View v) {
            mRecipeClickListener.onRecipeClick(recipeId, recipeName);
        }
    }
}
