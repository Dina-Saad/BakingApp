package com.example.dinasaad.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dinasaad.bakingapp.Objects.RecipeObject;
import com.example.dinasaad.bakingapp.Objects.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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


public class StepsFragment extends Fragment  implements  ExoPlayer.EventListener {
    private static final String TAG = StepsDetailsActivity.class.getSimpleName();
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    Steps RecipeStep = new Steps();
    RecipeObject Recipe=new RecipeObject();
    private SimpleExoPlayerView mPlayerView;
    TextView description;
    String step_index;
    static long savedinstance = -1;
    public StepsFragment() {

    }
    public static StepsFragment newInstance(int index,RecipeObject Recipe) {
        StepsFragment f = new StepsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("index", String.valueOf(index));
        args.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("SavedVideoIndex", mExoPlayer.getCurrentPosition());
        savedinstance = mExoPlayer.getCurrentPosition();
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView;
        if (Utilities.Istablet == true) {
            Bundle bundle = this.getArguments();
        if (bundle != null) {
            Recipe= bundle.getParcelable("clickedRecipeObject" );
            step_index =  bundle.getString("index");
            RecipeStep=Recipe.getStepsList().get(Integer.parseInt(step_index));

        }
       }
        else {
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();
        if (extras == null) {
        }
        else if (extras != null) {
            Recipe =  extras.getParcelable("clickedRecipeObject");
            step_index =  extras.getString("index");
            RecipeStep=Recipe.getStepsList().get(Integer.parseInt(step_index));

        }}
        if(RecipeStep.getVideoURL().isEmpty() != true) {

             rootView = inflater.inflate(R.layout.steps_details, container, false);

            // Initialize the player view.
            mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
            // Initialize the Media Session.
            initializeMediaSession();
            // Initialize the player.
            initializePlayer(Uri.parse(RecipeStep.getVideoURL()));

        }
        else
        {
             rootView = inflater.inflate(R.layout.steps_details_without_video, container, false);

        }
        if(!RecipeStep.getThumbnailURL().isEmpty()) {
            ImageView imageview_detail = (ImageView) rootView.findViewById(R.id.thumbnailimage);
            Picasso.with(getContext()).load(RecipeStep.getThumbnailURL()).into(imageview_detail);
        }
        description = (TextView) rootView.findViewById(R.id.Description);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(RecipeStep.getDescription());

        Button previousbutton = (Button) rootView.findViewById(R.id.button);
        previousbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreviousStep(v);
            }
        });
        Button nextbutton = (Button) rootView.findViewById(R.id.button2);
         nextbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    NextStep(v);
                }
         });
        if(Integer.parseInt(step_index) == 0)
            previousbutton.setEnabled(false);
        if(Integer.parseInt(step_index) == Recipe.getStepsList().size()-1)
            nextbutton.setEnabled(false);

        if (savedinstance != -1 ) {
            // Seek to the last position of the player.
            mExoPlayer.seekTo(savedinstance);
            savedinstance=-1;
        }
        return rootView;

    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null ) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            if (savedinstance != -1 ) {
                // Seek to the last position of the player.
                mExoPlayer.seekTo(savedinstance);
                savedinstance=-1;
            }
        }

    }



    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(RecipeStep.getVideoURL().isEmpty() != true){
            releasePlayer();
            mMediaSession.setActive(false);}
    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();


    }
    @Override
    public void onResume() {
        super.onResume();
        if (RecipeStep.getVideoURL().isEmpty() != true) {
            if( savedinstance != -1)
            {mExoPlayer = null;}
            initializePlayer(Uri.parse(RecipeStep.getVideoURL()));
        }

    }

    // ExoPlayer Event Listeners

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
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }
    @Override
    public void onPositionDiscontinuity() {
    }
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    public void NextStep(View view) {
        Intent intent = getActivity().getIntent();
        Bundle extras = new Bundle();
        int newindex = Integer.parseInt(step_index)+1;
        if(newindex < Recipe.getStepsList().size())
        {
            extras.putString("index", String.valueOf(newindex));
            if(Utilities.Istablet == true) {
               extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                intent.putExtras(extras);
                Fragment newFragment = new StepsFragment();
                newFragment.setArguments(extras);
                // Replace the old head fragment with a new one
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.head_container, newFragment);
                t.commit();
            }
           else {
                getActivity().finish();
                intent.putExtras(extras);
                startActivity(intent);}
        }
        else{
            view.setEnabled(false);
        }
    }


    public void PreviousStep(View view) {
        Intent intent = getActivity().getIntent();
        Bundle extras = new Bundle();
        int newindex = Integer.parseInt(step_index)-1;
        if(newindex >= 0)
        {   extras.putString("index", String.valueOf(newindex));
            intent.putExtras(extras);
            if(Utilities.Istablet == true) {
                extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                intent.putExtras(extras);
                Fragment newFragment = new StepsFragment();
                newFragment.setArguments(extras);
                // Replace the old head fragment with a new one
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.head_container, newFragment);
                t.commit();
            }else {
                getActivity().finish();
                intent.putExtras(extras);
                startActivity(intent);}
        }
        else{
            view.setEnabled(false);
        }

    }
}
