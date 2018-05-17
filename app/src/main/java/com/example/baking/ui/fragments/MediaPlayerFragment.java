package com.example.baking.ui.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.ui.activities.RecipeDetailActivity;
import com.example.baking.ui.activities.RecipeStepDetailsActivity;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;

public class MediaPlayerFragment extends Fragment {
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();
    private int mStepId;
    private int mStartWindow;
    private long mStartPosition;
    private boolean mStartAutoPlay;

    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private PlayerView mPlayerView;
    private MediaSource mMediaSource;
    private Uri mVideoUri;

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
        ImageView imageview = rootView.findViewById(R.id.iv_recipe_image);
        mPlayerView = rootView.findViewById(R.id.pv_recipe_video);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.exo_controls_play));

        Bundle arguments = getArguments();
        if (arguments != null) {
            savedInstanceState = arguments.getBundle("savedInstanceState");
            if (savedInstanceState != null) {
                mStartWindow = savedInstanceState.getInt("window");
                mStartPosition = savedInstanceState.getLong("position");
                mStartAutoPlay = savedInstanceState.getBoolean("auto_play");
            } else {
                clearStartPosition();
            }
            String ingredients = arguments.getString("ingredients");
            if (ingredients != null && ingredients.length() != 0) {
                // Media player container is hosting for ingredients list.
                mPlayerView.setVisibility(View.GONE);
                imageview.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(ingredients);
            } else {
                // Identifies this recipe step to send it to parent activity to consider if it's the same step (device rotation) or
                // a selection of another step.
                mStepId = arguments.getInt("id");
                // Video for this recipe step.
                String videoUrl = arguments.getString("video");
                String thumbnailUrl = arguments.getString("thumbnail");
                // Video url is either in video or thumbnail field in the web api response.
                String url = (videoUrl != null && videoUrl.length() != 0 && videoUrl.endsWith("mp4")) ? videoUrl : thumbnailUrl;

                if (url != null && url.length() != 0) {
                    if (url.endsWith("mp4")) {
                        mPlayerView.setVisibility(View.VISIBLE);
                        imageview.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        mVideoUri = Uri.parse(url);
                        initializePlayer();
                    } else {
                        // Url does not address to mp4 video file, replace with a thumbnail image.
                        mPlayerView.setVisibility(View.GONE);
                        imageview.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                        Picasso.get()
                                .load(url)
                                .centerCrop()
                                // Placeholder image file is downloaded from http://sweetclipart.com/delicious-pie-clip-art-2028
                                .placeholder(R.drawable.pie_clip_art)
                                .error(R.drawable.pie_clip_art)
                                .into(imageview);
                    }
                } else {
                    mPlayerView.setVisibility(View.GONE);
                    imageview.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageview.setImageResource(R.drawable.pie_clip_art);
                }
            }
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
        startPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        updateStartPosition();
        outState.putInt("stepId", mStepId);
        outState.putInt("window", mStartWindow);
        outState.putLong("position", mStartPosition);
        outState.putBoolean("auto_play", mStartAutoPlay);
        RecipeStepDetailsActivity stepDetailsActivity = null;
        RecipeDetailActivity detailActivity = null;
        try {
            detailActivity = (RecipeDetailActivity) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            stepDetailsActivity = (RecipeStepDetailsActivity) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
        if (stepDetailsActivity != null) {
            stepDetailsActivity.fromMediaFragment(outState);
        } else if (detailActivity != null) {
            detailActivity.fromMediaFragment(outState);
        }
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, getString(R.string.app_name)), bandwidthMeter);
            mMediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mVideoUri);
            mPlayer.setPlayWhenReady(mStartAutoPlay);
            boolean haveStartPosition = mStartWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mPlayer.seekTo(mStartWindow, mStartPosition);
            }
            mPlayer.prepare(mMediaSource, !haveStartPosition, false);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            updateStartPosition();
            mPlayer.release();
            mPlayer = null;
            mMediaSource = null;
        }
    }

    private void startPlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    private void pausePlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    private void updateStartPosition() {
        if (mPlayer != null) {
            mStartWindow = mPlayer.getCurrentWindowIndex();
            mStartPosition = Math.max(0, mPlayer.getContentPosition());
            mStartAutoPlay = mPlayer.getPlayWhenReady();
        }
    }

    private void clearStartPosition() {
        mStartAutoPlay = true;
        mStartWindow = C.INDEX_UNSET;
        mStartPosition = C.TIME_UNSET;
    }
}
