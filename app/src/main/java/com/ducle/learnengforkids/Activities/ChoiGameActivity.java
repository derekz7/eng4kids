package com.ducle.learnengforkids.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ducle.learnengforkids.Module.CauHoi;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChoiGameActivity extends AppCompatActivity {
    private ImageButton igbYesNo, igbDoanHinh;
    public static List<CauHoi> cauHoiList;
    private List<TuVung> listTu = MainMenuActivity.listTuVung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choigame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        SetUpCauHoi();
        onClick();

    }

    private void onClick() {
        igbYesNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiGameActivity.this,YesNoActivity.class));
            }
        });
        igbDoanHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiGameActivity.this,DoanHinhActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<TuVung> randomLuachon(){
        List<TuVung> listLuaChon = new ArrayList<>();
        List<TuVung> list = getListTu(listTu);
        Collections.shuffle(list);
        int index = 0;
        for (int i = 0 ; i < 4; i++){
            index = new Random().nextInt(list.size());
            if (i > 0){
              for (int j = 0; j < listLuaChon.size();j++) {
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

    private void SetUpCauHoi(){
        for (int i = 0; i < 10; i++){
            List<TuVung> luachon = randomLuachon();
            TuVung tuVung = luachon.get(new Random().nextInt(luachon.size()));
            CauHoi cauHoi = new CauHoi(luachon,tuVung);
            cauHoiList.add(cauHoi);
        }
    }

    private void initUI() {
        igbDoanHinh = findViewById(R.id.igbDoanHinh);
        igbYesNo = findViewById(R.id.igbYesNo);
        cauHoiList = new ArrayList<>();
    }
    private List<TuVung> getListTu(List<TuVung> list){
        List<TuVung> listTu = new ArrayList<>();
        for (int i = 0 ; i < list.size(); i++){
            if (!list.get(i).getLoaiTu().getName().equals("Alphabet"))
            {
                listTu.add(list.get(i));
            }
        }
        return listTu;
    }
}