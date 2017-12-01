package com.example.suzanne.recipesapp;

import android.content.Context;
import android.content.res.Configuration;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by suzanne on 26/11/2017.
 */

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String RECIPE_STEP_DETAIL_KEY = "recipeStepDetail";
    private static final String MEDIA_SESSION_TAG = "recipeMediaSession";
    private static final String IS_LAST_STEP_KEY = "isLastStep";
    private static final String IS_FIRST_STEP_KEY = "isFirstStep";
    private static final String FLAG_NEXT = "next";
    private static final String FLAG_PREVIOUS = "previous";

    private int mCurrentOrientation;

    private RecipeStep mRecipeStep;
    private ExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private OnStepArrowClickListener mStepArrowClickListener;

    public interface OnStepArrowClickListener{
        void onStepArrowClicked(String flag);
    }

    @Nullable @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    @Nullable @BindView(R.id.iv_next_arrow)
    ImageView mNextArrow;

    @Nullable @BindView(R.id.iv_back_arrow) ImageView mBackArrow;

    @Nullable @BindView(R.id.tv_next_label) TextView mNextLabel;
    @Nullable @BindView(R.id.tv_previous_label) TextView mPreviousLabel;
    @BindView(R.id.iv_video_alt_image) ImageView mVideoPlaceholderImage;
    @BindView(R.id.layout_no_network)
    LinearLayout mNoNetworkLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        mCurrentOrientation = getResources().getConfiguration().orientation;
        initializeMediaSession();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            mRecipeStep = bundle.getParcelable(RECIPE_STEP_DETAIL_KEY);

            if(mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT){
                mStepDescription.setText(mRecipeStep.getDescription());
                updateArrows(bundle);
            }

            if(NetworkUtils.isOnlineOrConnecting(getContext())){
                mNoNetworkLayout.setVisibility(View.INVISIBLE);
                initializePlayer();
            } else {
                mNoNetworkLayout.setVisibility(View.VISIBLE);
                mVideoPlaceholderImage.setVisibility(View.INVISIBLE);
                mPlayerView.setVisibility(View.INVISIBLE);
            }

        }

        return rootView;
    }

    private void updateArrows(Bundle bundle){
        mNextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepArrowClickListener.onStepArrowClicked(FLAG_NEXT);
            }
        });
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepArrowClickListener.onStepArrowClicked(FLAG_PREVIOUS);
            }
        });

        if(bundle.getBoolean(IS_LAST_STEP_KEY)){
            mNextArrow.setVisibility(View.INVISIBLE);
            mNextLabel.setVisibility(View.INVISIBLE);
        }

        if(bundle.getBoolean(IS_FIRST_STEP_KEY)){
            mBackArrow.setVisibility(View.INVISIBLE);
            mPreviousLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStepArrowClickListener = (OnStepArrowClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnNextStepClickListener");
        }
    }

    private void initializePlayer(){
//        Check if there is a video URL to load, show thumbnail or placeholder if not
        if(mRecipeStep.getVideoUrl().isEmpty() || mRecipeStep.getVideoUrl() == null){
            mPlayerView.setVisibility(View.INVISIBLE);
            mVideoPlaceholderImage.setVisibility(View.VISIBLE);
//            Check if there is a thumbnail to display
            if(!mRecipeStep.getThumbnailUrl().isEmpty() && mRecipeStep.getThumbnailUrl() != null){
                Picasso.with(getActivity())
                        .load(mRecipeStep.getThumbnailUrl())
                        .placeholder(R.drawable.video_coming_soon)
                        .error(R.drawable.video_coming_soon)
                        .into(mVideoPlaceholderImage);
            } else {
                mVideoPlaceholderImage.setImageDrawable(getActivity().getDrawable(R.drawable.video_coming_soon));
            }

        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            mVideoPlaceholderImage.setVisibility(View.INVISIBLE);
            if (mExoPlayer == null){
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                mPlayerView.setPlayer((SimpleExoPlayer) mExoPlayer);

                String userAgent = Util.getUserAgent(getActivity(), "recipe app");
                Uri uri = Uri.parse(mRecipeStep.getVideoUrl());
                MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                        getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
                mExoPlayer.addListener(this);
            }
        }

    }


    private void releasePlayer(){
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initializeMediaSession(){
        mMediaSession = new MediaSessionCompat(getActivity(), MEDIA_SESSION_TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void onNextClick(View view){

    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(),
                    1f);
        } else if (playbackState == Player.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(),
                    1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
