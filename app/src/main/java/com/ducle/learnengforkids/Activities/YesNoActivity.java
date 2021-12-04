package com.ducle.learnengforkids.Activities;

import static com.ducle.learnengforkids.Activities.ChoiGameActivity.cauDoList;
import static com.ducle.learnengforkids.Activities.ChoiGameActivity.mpGame;
import static com.ducle.learnengforkids.Activities.MainMenuActivity.setAnim_button_click;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.CauDo;
import com.ducle.learnengforkids.PlayMusic;
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
    private SharedPreferences sharedPreferences;
    private String username = "";
    private UserDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesnogame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getData();
        initUI();
        setViewUser();
        ViewQuestions();
        onClick();
    }

    private void onClick() {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                setAnim_button_click(btnYes);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAns(true);
                        speak.speak("Yes");
                    }
                }, 400);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                setAnim_button_click(btnNo);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAns(false);
                        speak.speak("No");
                    }
                }, 400);

            }
        });
        btnBackYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnim_button_click(btnBackYN);
                PlayMusic.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 400);
            }
        });
        btnSettingYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnim_button_click(btnSettingYN);
                PlayMusic.playClick(v.getContext());
                DialogSetting dialogSetting = new DialogSetting(YesNoActivity.this);
                dialogSetting.show(mpGame);
            }
        });
    }

    private void ViewQuestions() {
        if (currentQuestion >= listCauDo.size()) {
            //End
            Toast.makeText(YesNoActivity.this, "End", Toast.LENGTH_SHORT).show();
        } else {
            btnNo.setVisibility(View.VISIBLE);
            btnYes.setVisibility(View.VISIBLE);
            progressTime.setVisibility(View.VISIBLE);
            CauDo cauDo = listCauDo.get(currentQuestion);
            String[] ans = new String[]{cauDo.getWrongAns(), cauDo.getTrueAns()};
            int rd = new Random().nextInt(ans.length);
            speak.speak(ans[rd]);
            txCauHoi.setTextColor(Color.BLACK);
            txCauHoi.setText(ans[rd]);
            Picasso.with(this).load(cauDo.getImgUrl()).into(imgWordView);
            setCountDownTimer();
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
                playSound(R.raw.gameover);
                dialogTimeOut();
            }
        }.start();
    }

    private void showAns(boolean check) {
        btnNo.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);
        progressTime.setVisibility(View.GONE);
        count.cancel();
        CauDo cauDo = listCauDo.get(currentQuestion);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cauDo.getTrueAns().equals(txCauHoi.getText().toString())) {
                    if (check) {
                        playSound(R.raw.success);
                        score += 25;
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
                        score += 25;
                    }
                    txCauHoi.startAnimation(animBlink());
                    txCauHoi.setTextColor(Color.RED);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txCauHoi.setText(cauDo.getTrueAns());
                            speak.speak(txCauHoi.getText().toString());
                            txCauHoi.startAnimation(animBlink());
                            txCauHoi.setTextColor(Color.BLUE);
                        }
                    }, 1500);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txScore.setText(String.valueOf(score));
                        db.updateDiem(username, score);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("diem", String.valueOf(score));
                        editor.apply();
                        currentQuestion++;
                        ViewQuestions();
                    }
                }, 4500);

            }
        }, 1500);
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
        mpGame.start();
        timer = 20000;
        db = new UserDB();
        listCauDo = cauDoList;
        Collections.shuffle(listCauDo);
        speak = new ToSpeak(this);
    }

    private void setViewUser() {
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        score = Integer.parseInt(sharedPreferences.getString("diem", ""));
        txScore.setText(String.valueOf(score));
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

    private void refreshQuestion() {
        Collections.shuffle(listCauDo);
        ViewQuestions();
    }

    private void dialogTimeOut() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_timeout);
        final ImageButton igbBack, btnReplay;
        igbBack = dialog.findViewById(R.id.btnBack);
        btnReplay = dialog.findViewById(R.id.btnReplay);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        windowAttributes.windowAnimations = R.style.dialog_setting_anim;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(YesNoActivity.this);
                setAnim_button_click(igbBack);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        dialog.cancel();
                    }
                }, 400);

            }
        });

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(YesNoActivity.this);
                setAnim_button_click(btnReplay);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshQuestion();
                        dialog.dismiss();
                    }
                }, 400);

            }
        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (count != null) {
            count.cancel();
        }
        mpGame.pause();
    }

    @Override
    public void onBackPressed() {

    }

}