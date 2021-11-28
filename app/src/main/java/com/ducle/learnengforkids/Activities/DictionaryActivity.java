package com.ducle.learnengforkids.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ducle.learnengforkids.Adapter.ObjAdapter;
import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;
import com.ducle.learnengforkids.ToSpeak;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity {
    private ImageView imgView;
    private TextView tv_Color;
    private RecyclerView rcv_alphabet;
    private ObjAdapter objAdapter;
    private TextToSpeech toSpeech;
    private String id = "";
    private ImageButton igbSetting, btnhome;
    private ToSpeak toSpeak;
    private List<TuVung> list = MainMenuActivity.listTubyLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dictionary);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DictionaryActivity.this, RecyclerView.HORIZONTAL, false);
        rcv_alphabet.setLayoutManager(linearLayoutManager);
        imgView.setVisibility(View.GONE);
        tv_Color.setVisibility(View.GONE);
        loadLayout();
        buttonClick();

    }


    private void loadLayout() {
        objAdapter = new ObjAdapter(DictionaryActivity.this);
        objAdapter.setData(list);
        rcv_alphabet.setAdapter(objAdapter);
        if (id.equalsIgnoreCase("Alphabet")) {
            tv_Color.setVisibility(View.GONE);
        } else tv_Color.setVisibility(View.VISIBLE);
    }

//
//    private void speak(List<TuVung> list) {
//        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    toSpeech.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });
//        objAdapter.setOnWordClickListener(new ObjAdapter.onObjectClickListener() {
//            @Override
//            public void onObjectClick(int position, View v) {
//                PlayMusic.playClick(v.getContext());
//                v.startAnimation(MainMenuActivity.getAnimClick(getApplicationContext()));
//                toSpeech.speak(list.get(position).getNoiDung(), TextToSpeech.QUEUE_FLUSH, null);
//                imgView.setVisibility(View.VISIBLE);
//                Picasso.with(getApplicationContext()).load(list.get(position).getImgUrl()).into(imgView);
//                tv_Color.setText(list.get(position).getNoiDung());
//            }
//        });
//
//    }

    private void buttonClick() {

        objAdapter.setOnWordClickListener(new ObjAdapter.onObjectClickListener() {
            @Override
            public void onObjectClick(int position, View v) {
                PlayMusic.playClick(v.getContext());
                v.startAnimation(MainMenuActivity.getAnimClick(getApplicationContext()));
                toSpeak.speak(list.get(position).getNoiDung().toString());
                imgView.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(list.get(position).getImgUrl()).into(imgView);
                tv_Color.setText(list.get(position).getNoiDung());
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                MainMenuActivity.setAnim_button_click(btnhome);
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
                PlayMusic.playClick(v.getContext());
                MainMenuActivity.setAnim_button_click(igbSetting);
                DialogSetting dialogSetting = new DialogSetting(DictionaryActivity.this);
                dialogSetting.show(Gravity.CENTER);
            }
        });
    }

    private void init() {
        btnhome = findViewById(R.id.btnhomedict);
        igbSetting = findViewById(R.id.igbSettingDictionary);
        tv_Color = findViewById(R.id.tv_namecolor);
        imgView = findViewById(R.id.imgWordView);
        rcv_alphabet = findViewById(R.id.rcv_alphabet);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID_ITEM");
        toSpeak = new ToSpeak(this);
    }

}
