package com.example.baking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;

public class MediaPlayerFragment extends Fragment {

    public MediaPlayerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        TextView view = rootView.findViewById(R.id.tv_media_player);

        String videoUrl = getArguments().getString("video");
        String thumbnailUrl = getArguments().getString("thumbnail");
        String url = (videoUrl != null && videoUrl.length() != 0 && videoUrl.endsWith("mp4")) ? videoUrl : thumbnailUrl;
        url = (url.length() != 0 && videoUrl.endsWith("mp4")) ? url : "No video link";

        view.setText(url);

        return rootView;
    }
}
