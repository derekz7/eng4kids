package com.ducle.learnengforkids.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Adapter.ObjAdapter;
import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.CauHoi;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;
import com.ducle.learnengforkids.ToSpeak;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DoanHinhActivity extends AppCompatActivity {
    private final int timer = 20000;
    private ImageButton igbSpeakDH, btnBacKDH, igbSetting;
    private RecyclerView rcvLuaChon;
    private ImageView imgDapAn;
    private ProgressBar progressCount;
    private TextView tvNoiDung, tvDiem,tvCurrentQuestion;
    private  List<CauHoi> cauHoiList = ChoiGameActivity.cauHoiList;
    private int currentQuestion = 0, score = 0, newScore = 0;
    private ObjAdapter luachonAdapter;
    private SharedPreferences sharedPreferences;
    private CountDownTimer countDownTimer;
    private ToSpeak toSpeak;
    private CauHoi cauHoiNow;
    private String username = "";
    private UserDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamexemvachon);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChoiGameActivity.mpGame.start();
        initUI();
        setViewUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoanHinhActivity.this, RecyclerView.HORIZONTAL, false);
        rcvLuaChon.setLayoutManager(linearLayoutManager);
        cauHoiList = ChoiGameActivity.getCauHoiList();
        ViewQuestion();
        onClick();
    }

    private void onClick() {
        luachonAdapter.setOnWordClickListener(new ObjAdapter.onObjectClickListener() {
            @Override
            public void onObjectClick(int position, View v) {
                PlayMusic.playClick(v.getContext());
                v.startAnimation(MainMenuActivity.getAnimClick(v.getContext()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (checkDapAn(cauHoiList.get(currentQuestion).getListLuachon().get(position).getNoiDung())) {
                            playSound(R.raw.success);
                        } else {
                            playSound(R.raw.losing);
                        }
                        showAns();
                    }
                },400);

            }
        });
        igbSpeakDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(igbSpeakDH);
                PlayMusic.playClick(v.getContext());
                toSpeak.speak(tvNoiDung.getText().toString());
            }
        });
        btnBacKDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(btnBacKDH);
                PlayMusic.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },400);
            }
        });
        igbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(igbSetting);
                PlayMusic.playClick(v.getContext());
                DialogSetting dialogSetting = new DialogSetting(DoanHinhActivity.this);
                dialogSetting.show(ChoiGameActivity.mpGame);
            }
        });
    }

    private void ViewQuestion() {
        if (currentQuestion >= cauHoiList.size()) {
            countDownTimer.cancel();
            //Hoan thanh tro choi
            tvDiem.setText(String.valueOf(score + newScore));
            //save and update score
            db.updateDiem(username,(score + newScore));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("diem",String.valueOf(newScore + score));
            editor.apply();
            playSound(R.raw.achivement);
            dialogScore();
            currentQuestion = 0;
            newScore = 0;
        } else {
            rcvLuaChon.setVisibility(View.VISIBLE);
            progressCount.setVisibility(View.VISIBLE);
            CauHoi cauHoi = cauHoiList.get(currentQuestion);
            cauHoiNow = cauHoi;
            tvNoiDung.setText(cauHoi.getDapan().getNoiDung());
            tvCurrentQuestion.startAnimation(startAnim());
            tvCurrentQuestion.setText((currentQuestion+1)+"/"+cauHoiList.size());
            toSpeak.speak(tvNoiDung.getText().toString());
            imgDapAn.setImageResource(R.drawable.question);
            luachonAdapter.setData(cauHoi.getListLuachon());
            rcvLuaChon.setAdapter(luachonAdapter);
            setCountDownTimer();
            countDownTimer.start();
        }

    }


    private void setCountDownTimer() {
        progressCount.setProgress(timer / 1000);
        progressCount.setMax(timer / 1000);
        countDownTimer = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int count = progressCount.getProgress() - 1;
                progressCount.setProgress(count);
            }
            @Override
            public void onFinish() {
                showAns();
            }
        };
    }

    private void showAns() {
        rcvLuaChon.setVisibility(View.GONE);
        progressCount.setVisibility(View.GONE);
        countDownTimer.cancel();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getApplicationContext()).load(cauHoiNow.getDapan().getImgUrl()).into(imgDapAn);
                toSpeak.speak(tvNoiDung.getText().toString());
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestion++;
                ViewQuestion();
            }
        }, 5000);
    }

    private boolean checkDapAn(String traLoi) {
        boolean check;
        if (traLoi.equals(tvNoiDung.getText().toString())) {
            newScore += 25;
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    private void refreshQuestion(){
        cauHoiList.clear();
        cauHoiList = ChoiGameActivity.getCauHoiList();
        ViewQuestion();
    }


    //sound effect
    private void playSound(int rawRes) {
        MediaPlayer sound = MediaPlayer.create(this, rawRes);
        sound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                sound.setVolume(0.1f,0.1f);
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

    private void initUI() {
        igbSetting = findViewById(R.id.btnSettingGameDH);
        btnBacKDH = findViewById(R.id.btnBacKDH);
        igbSpeakDH = findViewById(R.id.igbSpeakDH);
        tvDiem = findViewById(R.id.tvDiem);
        rcvLuaChon = findViewById(R.id.rcvTraLoiDH);
        imgDapAn = findViewById(R.id.imgDapAn);
        progressCount = findViewById(R.id.progressBarCount);
        tvNoiDung = findViewById(R.id.tvNoiDungCauHoiDH);
        tvCurrentQuestion = findViewById(R.id.tvCurrentQuestion);
        db = new UserDB();
        toSpeak = new ToSpeak(this);
        cauHoiList = new ArrayList<>();
        luachonAdapter = new ObjAdapter(this);
    }

    private void setViewUser() {
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        username =  sharedPreferences.getString("username","");
        score = Integer.parseInt(sharedPreferences.getString("diem", ""));
        tvDiem.setText(String.valueOf(score));
    }
    //anim
    private Animation startAnim(){
        return AnimationUtils.loadAnimation(this,R.anim.anim_blink);
    }


    @SuppressLint("SetTextI18n")
        private void dialogScore(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_score);
        final ImageButton igbReplay, igbReturn;
        final TextView tvNewScore;
        igbReturn = dialog.findViewById(R.id.btnReturn);
        igbReplay = dialog.findViewById(R.id.btnReplay);
        tvNewScore = dialog.findViewById(R.id.tvNewScore);
        tvNewScore.setText("+ " + newScore);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        igbReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(DoanHinhActivity.this);
                MainMenuActivity.setAnim_button_click(igbReplay);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshQuestion();
                        dialog.cancel();
                    }
                },400);

            }
        });

        igbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(DoanHinhActivity.this);
                MainMenuActivity.setAnim_button_click(igbReturn);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        dialog.dismiss();
                    }
                },400);

            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayMusic.playClick(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        ChoiGameActivity.mpGame.pause();
    }


}
