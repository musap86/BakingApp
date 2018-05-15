package com.example.baking.ui.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

public class MediaPlayerFragment extends Fragment {
    private SimpleExoPlayer mPlayer;
    private PlayerView mPlayerView;
    private Context mContext;

    public MediaPlayerFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        TextView textView = rootView.findViewById(R.id.tv_media_player);
        mPlayerView = rootView.findViewById(R.id.pv_recipe_video);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.exo_controls_play));

        String ingredients = Objects.requireNonNull(getArguments()).getString("ingredients");
        if (ingredients != null && ingredients.length() != 0) {
            mPlayerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(ingredients);
        } else {
            String videoUrl = Objects.requireNonNull(getArguments()).getString("video");
            String thumbnailUrl = getArguments().getString("thumbnail");
            String url = (videoUrl != null && videoUrl.length() != 0 && videoUrl.endsWith("mp4")) ? videoUrl : thumbnailUrl;

            if (url != null && url.length() != 0 && url.endsWith("mp4")) {
                mPlayerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                Uri videoUri = Uri.parse(url);
                initializePlayer(videoUri);
            } else {
                mPlayerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.no_video));
            }
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        startPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer(Uri mp4VideoUri) {
        if (mPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, getString(R.string.app_name)), bandwidthMeter);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
            mPlayer.prepare(videoSource);
            mPlayer.setPlayWhenReady(false);
        }
    }

    private void pausePlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    private void startPlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
