package com.ducle.learnengforkids;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class ToSpeak {
    private TextToSpeech toSpeech;

    public ToSpeak( Context mContext) {
        toSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    toSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
    public void speak(String text){
        toSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }
}
