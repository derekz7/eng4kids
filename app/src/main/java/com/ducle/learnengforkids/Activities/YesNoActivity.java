package com.ducle.learnengforkids.Activities;

import static com.ducle.learnengforkids.Activities.ChoiGameActivity.cauDoList;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ducle.learnengforkids.Module.CauDo;
import com.ducle.learnengforkids.R;
import com.ducle.learnengforkids.ToSpeak;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class YesNoActivity extends AppCompatActivity {
    private List<CauDo> listCauDo;
    private ImageView imgWordView;
    private ImageButton btnBackYN, btnSettingYN, btnYes, btnNo;
    private TextView txScore, txCauHoi;
    private ProgressBar progressTime;
    private CountDownTimer count;
    private int timer;
    private int currentQuestion = 0, score = 0;
    private ToSpeak speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesnogame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getData();
        initUI();
        ViewQuestions();
        onClick();
    }

    private void onClick() {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAns(true);
                speak.speak("Yes");
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAns(false);
                speak.speak("No");
            }
        });
    }

    private void ViewQuestions() {
        if (currentQuestion >= listCauDo.size()) {
            //End
            Toast.makeText(YesNoActivity.this, "End", Toast.LENGTH_SHORT).show();
        } else {
            CauDo cauDo = listCauDo.get(currentQuestion);
            String[] ans = new String[]{cauDo.getWrongAns(), cauDo.getTrueAns()};
            int rd = new Random().nextInt(ans.length);
            speak.speak(ans[rd]);
            txCauHoi.setTextColor(Color.BLACK);
            txCauHoi.setText(ans[rd]);
            Picasso.with(this).load(cauDo.getImgUrl()).into(imgWordView);
        }
    }

    private void setCountDownTimer() {
        progressTime.setProgress(timer / 1000);
        progressTime.setMax(timer / 1000);
        count = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int count = progressTime.getProgress() - 1;
                progressTime.setProgress(count);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    private void showAns(boolean check) {
        btnNo.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);
        progressTime.setVisibility(View.GONE);
//        count.cancel();
        CauDo cauDo = listCauDo.get(currentQuestion);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cauDo.getTrueAns().equals(txCauHoi.getText().toString())) {
                    if (check) {
                        playSound(R.raw.success);
                    } else {
                        playSound(R.raw.losing);
                    }
                    txCauHoi.startAnimation(animBlink());
                    txCauHoi.setTextColor(Color.BLUE);
                } else {
                    if (check) {
                        playSound(R.raw.losing);
                    } else {
                        playSound(R.raw.success);
                    }
                    txCauHoi.startAnimation(animBlink());
                    txCauHoi.setTextColor(Color.RED);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txCauHoi.setText(cauDo.getTrueAns());
                            txCauHoi.startAnimation(animBlink());
                            txCauHoi.setTextColor(Color.BLUE);
                        }
                    },1500);
                }
            }
        }, 1500);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                currentQuestion++;
//                ViewQuestions();
//            }
//        }, 5000);
    }


    private void initUI() {
        btnYes = findViewById(R.id.igbYes);
        btnNo = findViewById(R.id.igbNo);
        imgWordView = findViewById(R.id.imgWordView);
        btnBackYN = findViewById(R.id.btnBackYN);
        btnSettingYN = findViewById(R.id.btnSettingYN);
        txScore = findViewById(R.id.tvDiem);
        progressTime = findViewById(R.id.progressBarCount);
        txCauHoi = findViewById(R.id.tvNoiDungCauHoiYN);
    }

    private void getData() {
        timer = 20;
        listCauDo = cauDoList;
        Collections.shuffle(listCauDo);
        speak = new ToSpeak(this);
    }

    private Animation animBlink() {
        return AnimationUtils.loadAnimation(this, R.anim.anim_blink);
    }

    //sound effect
    private void playSound(int rawRes) {
        MediaPlayer sound = MediaPlayer.create(this, rawRes);
        sound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                sound.setVolume(0.1f, 0.1f);
                sound.start();
            }
        });
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                sound.release();
            }
        });
    }

}