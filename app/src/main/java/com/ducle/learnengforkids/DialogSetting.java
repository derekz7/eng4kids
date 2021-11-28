package com.ducle.learnengforkids;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.ducle.learnengforkids.Activities.ChoiGameActivity;
import com.ducle.learnengforkids.Activities.DangNhapActivity;
import com.ducle.learnengforkids.Activities.MainMenuActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DialogSetting {
    SharedPreferences sharedPreferences;
    private Context context;
    private Dialog dialog;
    AudioManager audioManager;

    public DialogSetting(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sharedPreferences = context.getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
    }

    public void show(MediaPlayer mediaPlayer) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
        final ImageButton igbHome, igbMusic, igbFB, igbExit;
        final Button igbLogout;
        final SeekBar skMusic;
        igbExit = dialog.findViewById(R.id.igbExit);
        igbHome = dialog.findViewById(R.id.igbHome);
        igbMusic = dialog.findViewById(R.id.igbMusic);
        igbFB = dialog.findViewById(R.id.igbFB);
        skMusic = dialog.findViewById(R.id.skbarVolume);
        igbLogout = dialog.findViewById(R.id.igbLogOut);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.windowAnimations = R.style.dialog_setting_anim;
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        igbExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Exit(Gravity.TOP,v.getContext());
            }
        });
        igbFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbFB.startAnimation(MainMenuActivity.getAnimClick(context));
                PlayMusic.playClick(v.getContext());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/eelcud"));
                context.startActivity(intent);
            }
        });
        igbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbHome.startAnimation(MainMenuActivity.getAnimClick(context));
                PlayMusic.playClick(v.getContext());
                if (context.getClass() != MainMenuActivity.class) {
                    context.startActivity(new Intent(context,MainMenuActivity.class));
                }
                dialog.cancel();

            }
        });
        if (mediaPlayer.isPlaying()){
            igbMusic.setImageResource(R.drawable.speaker);
            skMusic.setEnabled(true);
            skMusic.setAlpha(1);
        }
        else
        {
            igbMusic.setImageResource(R.drawable.mute);
            skMusic.setEnabled(false);
            skMusic.setAlpha(0.5F);
        }
        igbMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbMusic.startAnimation(MainMenuActivity.getAnimClick(context));
                PlayMusic.playClick(v.getContext());
                if (mediaPlayer.isPlaying()) {
                    igbMusic.setImageResource(R.drawable.mute);
                    mediaPlayer.pause();
                    skMusic.setEnabled(false);
                    skMusic.setAlpha((float) 0.5);
                } else {
                    mediaPlayer.start();
                    igbMusic.setImageResource(R.drawable.speaker);
                    skMusic.setEnabled(true);
                    skMusic.setAlpha((float) 1);
                }

            }
        });
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        skMusic.setMax(max);
        skMusic.setProgress(currentVol);
        skMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        igbLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbMusic.startAnimation(MainMenuActivity.getAnimClick(context));
                PlayMusic.playClick(v.getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context.getApplicationContext(), DangNhapActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void cancel(){
        dialog.cancel();
    }

    public static void dialog_Exit(int gravity, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        final ImageButton checkExit, cancel;
        checkExit = dialog.findViewById(R.id.btncheckexit);
        cancel = dialog.findViewById(R.id.btncancelexit);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        windowAttributes.windowAnimations = R.style.Dialog_Animations;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == gravity || Gravity.CENTER == gravity || Gravity.TOP == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        checkExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
