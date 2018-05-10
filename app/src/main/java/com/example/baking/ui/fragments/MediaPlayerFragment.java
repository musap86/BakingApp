package com.example.baking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baking.R;

public class MediaPlayerFragment extends Fragment {
    public MediaPlayerFragment() {
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        ImageView imageView = rootView.findViewById(R.id.iv_media_player);
        imageView.setImageResource(R.drawable.exo_controls_play);
        return rootView;
    }
}
