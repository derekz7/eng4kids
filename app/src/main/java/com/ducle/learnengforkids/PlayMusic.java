package com.ducle.learnengforkids;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PlayMusic {
    public static MediaPlayer mpClick;

    public static void playClick(Context context){
        mpClick = MediaPlayer.create(context,R.raw.click);
        mpClick.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mpClick.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void startAnimationClick(View v,Context context){
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.click_item);
        v.startAnimation(animation);
    }

}
