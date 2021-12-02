package com.ducle.learnengforkids.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.FireBase.CauDoDB;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.CauDo;
import com.ducle.learnengforkids.Module.CauHoi;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChoiGameActivity extends AppCompatActivity {
    private ImageButton igbYesNo, igbDoanHinh;
    private ImageButton igbHome, igbSetting;
    public static List<CauHoi> listCauHoi;
    public static List<CauDo> cauDoList;
    private CauDoDB cauDoDB;
    public static List<TuVung> listTu = MainMenuActivity.listTuVung;
    public static MediaPlayer mpGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choigame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        getData();

        onClick();
        playMusic();

    }

    private void getData() {
        cauDoDB = new CauDoDB();
        listCauHoi = getCauHoiList();
        cauDoList = cauDoDB.getAllCauDo();
    }

    private void onClick() {
        igbYesNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbYesNo.startAnimation(MainMenuActivity.getAnimClick(v.getContext()));
                PlayMusic.playClick(v.getContext());
                startActivity(new Intent(ChoiGameActivity.this, YesNoActivity.class));
            }
        });
        igbDoanHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbDoanHinh.startAnimation(MainMenuActivity.getAnimClick(v.getContext()));
                PlayMusic.playClick(v.getContext());
                Intent intent = new Intent(ChoiGameActivity.this, DoanHinhActivity.class);
                startActivity(intent);
            }
        });
        igbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(igbHome);
                PlayMusic.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 400);

            }
        });
        igbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(igbSetting);
                PlayMusic.playClick(v.getContext());
                DialogSetting dialogSetting = new DialogSetting(ChoiGameActivity.this);
                dialogSetting.show(mpGame);
            }
        });

    }


    public static List<TuVung> randomLuachon() {
        List<TuVung> listLuaChon = new ArrayList<>();
        List<TuVung> list = getListTu(listTu);
        Collections.shuffle(list);
        int index;
        for (int i = 0; i < 4; i++) {
            index = new Random().nextInt(list.size());
            if (i > 0) {
                for (int j = 0; j < listLuaChon.size(); j++) {
                    if (listLuaChon.get(j).getNoiDung().equals(list.get(index).getNoiDung())) {
                        index = new Random().nextInt(list.size());
                        j = 0;
                    }
                }
            }
            listLuaChon.add(list.get(index));
        }
        Collections.shuffle(listLuaChon);
        return listLuaChon;
    }

    public static List<CauHoi> getCauHoiList() {
        listCauHoi = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<TuVung> luachon = randomLuachon();
            TuVung tuVung = luachon.get(new Random().nextInt(luachon.size()));
            CauHoi cauHoi = new CauHoi(luachon, tuVung);
            listCauHoi.add(cauHoi);
        }
        return listCauHoi;
    }

    private void initUI() {
        igbDoanHinh = findViewById(R.id.igbDoanHinh);
        igbYesNo = findViewById(R.id.igbYesNo);
        igbHome = findViewById(R.id.btnHomeGame);
        igbSetting = findViewById(R.id.btnSettingGame);

    }

    public static List<TuVung> getListTu(List<TuVung> list) {
        List<TuVung> listTu = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getLoaiTu().getName().equals("Alphabet")) {
                listTu.add(list.get(i));
            }
        }
        return listTu;
    }


    private void playMusic() {
        mpGame = MediaPlayer.create(this, R.raw.dogandponny);
        mpGame.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0.2f, 0.2f);
                mp.start();
                mp.setLooping(true);
            }
        });
        mpGame.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mpGame.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpGame.stop();
    }
}