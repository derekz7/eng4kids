package com.ducle.learnengforkids.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Adapter.ObjAdapter;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.CauHoi;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.R;
import com.ducle.learnengforkids.ToSpeak;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DoanHinhActivity extends AppCompatActivity {
    private int timer = 20000;
    private RecyclerView rcvLuaChon;
    private ImageView imgDapAn;
    private ProgressBar progressCount;
    private TextView tvNoiDung, tvDiem;
    List<CauHoi> cauHoiList = ChoiGameActivity.cauHoiList;
    int currentQuestion = 0, score = 0, newScore = 10 ;
    private ObjAdapter luachonAdapter;
    private SharedPreferences sharedPreferences;
    private CountDownTimer countDownTimer;
    private ToSpeak toSpeak;
    private CauHoi cauHoiNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamexemvachon);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        setViewUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoanHinhActivity.this, RecyclerView.HORIZONTAL, false);
        rcvLuaChon.setLayoutManager(linearLayoutManager);
//        ViewQuestion();
//        onClick();
        updateScore();
    }

    private void onClick() {
        luachonAdapter.setOnWordClickListener(new ObjAdapter.onObjectClickListener() {
            @Override
            public void onObjectClick(int position, View v) {
                if (checkDapAn(cauHoiList.get(currentQuestion).getListLuachon().get(position).getNoiDung())) {
                    Toast.makeText(DoanHinhActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    showAns();
                }
                else {
                    Toast.makeText(DoanHinhActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    showAns();
                }
            }
        });
    }


    private void ViewQuestion() {
        if (currentQuestion >= cauHoiList.size()) {
            countDownTimer.cancel();
            //Hoan thanh tro choi
            // progressCount.setVisibility(View.GONE);
            Toast.makeText(this, "Diem "+(newScore+score), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
            updateScore();
            newScore = 0;
        } else {
            rcvLuaChon.setVisibility(View.VISIBLE);
            progressCount.setVisibility(View.VISIBLE);
            CauHoi cauHoi = cauHoiList.get(currentQuestion);
            cauHoiNow = cauHoi;
            tvNoiDung.setText(cauHoi.getDapan().getNoiDung());
            toSpeak.speak(tvNoiDung.getText().toString());
            imgDapAn.setImageResource(R.drawable.question);
            Toast.makeText(DoanHinhActivity.this, "Cau " + currentQuestion, Toast.LENGTH_SHORT).show();
            luachonAdapter.setData(cauHoi.getListLuachon());
            rcvLuaChon.setAdapter(luachonAdapter);
            setCountDownTimer();
            countDownTimer.start();
        }

    }

    private void updateScore(){
        CountDownTimer count = new CountDownTimer(2000,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                score += 1;
                tvDiem.setText(String.valueOf(score));
            }

            @Override
            public void onFinish() {
                Toast.makeText(DoanHinhActivity.this, ""+score, Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void setCountDownTimer(){
        progressCount.setProgress(timer/1000);
        progressCount.setMax(timer/1000);
        countDownTimer = new CountDownTimer(timer,1000) {
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
        },5000);
    }

    private boolean checkDapAn(String traLoi) {
        boolean check;
        if (traLoi.equals(tvNoiDung.getText().toString())) {
            newScore += 10;
            updateScore();
            check = true;
        } else {
            check = false;
        }
        return check;
    }


    private void initUI() {
        tvDiem = findViewById(R.id.tvDiem);
        rcvLuaChon = findViewById(R.id.rcvTraLoiDH);
        imgDapAn = findViewById(R.id.imgDapAn);
        progressCount = findViewById(R.id.progressBarCount);
        tvNoiDung = findViewById(R.id.tvNoiDungCauHoiDH);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        score = Integer.parseInt(sharedPreferences.getString("diem", ""));
        toSpeak = new ToSpeak(this);
        luachonAdapter = new ObjAdapter(this);
    }

    private void setViewUser() {
        tvDiem.setText(String.valueOf(score));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
