package com.example.baking.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.data.database.entities.Recipe;
import com.example.baking.idling.SimpleIdlingResource;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeCardViewHolder> {
    private final RecipeClickListener mRecipeClickListener;
    private List<Recipe> mRecipes;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipeAdapter(RecipeClickListener recipeClickListener) {
        mRecipeClickListener = recipeClickListener;
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        } else {
            return 0;
        }
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

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void setIdlingResource(SimpleIdlingResource idlingResource) {
        mIdlingResource = idlingResource;
    }

    public class RecipeCardViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView recipeNameTextView;
        private final ImageView recipeImageView;
        private int recipeId;
        private String recipeName;

        private RecipeCardViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            recipeImageView = itemView.findViewById(R.id.iv_card_recipe_image);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            Recipe current = mRecipes.get(position);
            recipeId = current.id;
            recipeName = current.name;
            recipeNameTextView.setText(recipeName);

            String url = current.image;
            if (url != null && url.length() != 0) {
                Picasso.get()
                        .load(url)
                        .centerCrop()
                        // Placeholder image file is downloaded from http://sweetclipart.com/delicious-pie-clip-art-2028
                        .placeholder(R.drawable.pie_clip_art)
                        .error(R.drawable.pie_clip_art)
                        .into(recipeImageView);
            } else {
                recipeImageView.setImageResource(R.drawable.pie_clip_art);
            }
            // At this point the response from web api is taken and the recycler view of this
            // activity is getting populated. Since RecipesActivityTest
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        }

        @Override
        public void onClick(View v) {
            mRecipeClickListener.onRecipeClick(recipeId, recipeName);
        }
    }
}
