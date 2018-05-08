package com.example.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final IListItemClickListener mOnClickListener;
    private final ArrayList<String> mItemList;

    public RecipeAdapter(IListItemClickListener listItemClickListener, ArrayList<String> itemList) {
        mOnClickListener = listItemClickListener;
        mItemList = itemList;
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
        return mItemList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView mRecipeNameTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            mRecipeNameTextView.setText(mItemList.get(position));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition, itemView);
        }
    }
}
