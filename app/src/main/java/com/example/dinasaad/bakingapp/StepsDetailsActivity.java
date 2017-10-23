package com.example.dinasaad.bakingapp;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dinasaad.bakingapp.Objects.RecipeObject;
import com.example.dinasaad.bakingapp.Objects.Steps;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

/**
 * Created by DinaSaad on 23/08/2017.
 */

public class StepsDetailsActivity extends AppCompatActivity  {
    private static final String TAG = StepsDetailsActivity.class.getSimpleName();
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private SimpleExoPlayer mExoPlayer;
    Steps RecipeStep = new Steps();
    RecipeObject Recipe=new RecipeObject();
    private SimpleExoPlayerView mPlayerView;
    TextView description;
    String step_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_step);
    }
}