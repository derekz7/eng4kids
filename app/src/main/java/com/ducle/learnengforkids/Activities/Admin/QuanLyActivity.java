package com.ducle.learnengforkids.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ducle.learnengforkids.R;

public class QuanLyActivity extends AppCompatActivity {
    ImageButton igbQLTu, igbQLUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        igbQLTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this, QuanLyTuVung.class));
            }
        });
        igbQLUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this, QuanLyUser.class));
            }
        });
    }
    private void init(){
        igbQLTu = findViewById(R.id.igbQLTuVung);
        igbQLUser = findViewById(R.id.igbQuanLyUser);
    }
}
