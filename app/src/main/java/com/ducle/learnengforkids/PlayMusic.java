package com.ducle.learnengforkids;

import android.content.Context;
import android.media.MediaPlayer;

public class PlayMusic {
    public static MediaPlayer mpClick;

//    public PlayMusic(Context context) {
//        this.mpClick = MediaPlayer.create(context, R.raw.click);
//        this.mpIntro = MediaPlayer.create(context, R.raw.intro);
//        this.mpBackground = MediaPlayer.create(context, R.raw.waltz);
//    }


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

}
