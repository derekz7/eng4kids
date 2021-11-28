package com.ducle.learnengforkids.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView yt_player;
    String id = "";
    int REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        MainMenuActivity.mpbackground.pause();
        Intent intent = getIntent();
        id = intent.getStringExtra("ID_VIDEO");
        yt_player.initialize(MusicActicity.API_KEY,this);

    }

    private void init() {
        yt_player = findViewById(R.id.view_youtube);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(id);
        youTubePlayer.setFullscreen(true);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this,REQUEST_CODE);
        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE){
            yt_player.initialize(MusicActicity.API_KEY,this);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}