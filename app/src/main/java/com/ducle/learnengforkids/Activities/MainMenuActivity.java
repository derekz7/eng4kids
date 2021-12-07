package com.ducle.learnengforkids.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.ducle.learnengforkids.FireBase.LoaiTuDB;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;

import java.util.ArrayList;
import java.util.List;

import com.ducle.learnengforkids.Adapter.ItemAdapter;

public class MainMenuActivity extends AppCompatActivity {
    private RecyclerView rcv_menu_tudien;
    private LinearLayout liner_layoutmenu, item1, item2, item3;
    private ImageButton btnsetting, imbHocTA, imbGame, imbVideo;
    public static LottieAnimationView anim_ballon;
    private ItemAdapter itemAdapter;
    private WordDB wordDB;
    private LoaiTuDB loaiTuDB;
    public static List<LoaiTu> loaiTuList;
    public static List<TuVung> listTuVung, listTubyLoai;
    private TextView tvUsername, tvScore;
    private SharedPreferences sharedPreferences;
    private UserDB userDB;
    private String username = "";
    public static MediaPlayer mpbackground;
    private List<User> userList;
    private User userLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        getData();
        setUserView();
        rcv_menu_tudien.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcv_menu_tudien.setLayoutManager(layoutManager);
////        gan gia tri
        itemAdapter = new ItemAdapter(this,loaiTuList);
        rcv_menu_tudien.setAdapter(itemAdapter);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcv_menu_tudien);
        rcv_menu_tudien.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View v = snapHelper.findSnapView(layoutManager);
                int pos = layoutManager.getPosition(v);
                RecyclerView.ViewHolder viewholder = rcv_menu_tudien.findViewHolderForAdapterPosition(pos);
                RelativeLayout rll = viewholder.itemView.findViewById(R.id.rll);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rll.animate().setDuration(300).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();
                } else {
                    rll.animate().setDuration(300).scaleX(0.75f).scaleY(0.75f).setInterpolator(new AccelerateInterpolator()).start();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        eventClickButton();
        //music background
        playMusic();


    }


    private void eventClickButton() {

        itemAdapter.setOnItemClickListener(new ItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PlayMusic.playClick(v.getContext());
                v.startAnimation(getAnimClick(v.getContext()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listTubyLoai = getListTuVungbyLoai(loaiTuList.get(position));
                        Intent intent = new Intent(MainMenuActivity.this, DictionaryActivity.class);
                        intent.putExtra("ID_ITEM",listTubyLoai.get(position).getLoaiTu().getName());
                        startActivity(intent);
                    }
                }, 500);

            }
        });



        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                setAnim_button_click(btnsetting);
                DialogSetting dialogSetting = new DialogSetting(MainMenuActivity.this);
                dialogSetting.show(mpbackground);
            }
        });


        imbHocTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                item1.startAnimation(getAnimClick(v.getContext()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liner_layoutmenu.setVisibility(View.GONE);
                        rcv_menu_tudien.setVisibility(View.VISIBLE);
                    }
                }, 1000);


            }
        });
        imbGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                item3.startAnimation(getAnimClick(v.getContext()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainMenuActivity.this, ChoiGameActivity.class);
                        startActivity(intent);
                    }
                }, 800);

            }
        });
        imbVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                item2.startAnimation(getAnimClick(v.getContext()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainMenuActivity.this, WatchVideoActicity.class));
                    }
                }, 800);
            }
        });

    }


    private void initUI() {
        tvUsername = findViewById(R.id.tvUser);
        tvScore = findViewById(R.id.tvScore);
        liner_layoutmenu = findViewById(R.id.linear_layoutmenu);
        btnsetting = findViewById(R.id.btnsetting);
        imbVideo = findViewById(R.id.img_amnhac);
        imbGame = findViewById(R.id.img_kiemtra);
        imbHocTA = findViewById(R.id.img_tudien);
        rcv_menu_tudien = findViewById(R.id.rcv_menu);
        anim_ballon = findViewById(R.id.anim_ballon);
        rcv_menu_tudien.setVisibility(View.GONE);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
    }

    private void setUserView() {
        userLog = userDB.getUserbyUsername(userList, username);
        tvUsername.setText(userLog.getUsername());
        tvScore.setText(String.valueOf(userLog.getDiem()));
    }

    private void getData() {
        userDB = new UserDB();
        wordDB = new WordDB();
        loaiTuDB = new LoaiTuDB();
        userList = DangNhapActivity.userList;
        listTubyLoai = new ArrayList<>();
        loaiTuList = loaiTuDB.getListLoai();
        listTuVung = wordDB.getListTuVung();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

    }

    private List<TuVung> getListTuVungbyLoai(LoaiTu loaiTu) {
        List<TuVung> list = new ArrayList<>();
        for (TuVung tuVung : listTuVung) {
            if (tuVung.getLoaiTu().getName().equals(loaiTu.getName())) {
                list.add(tuVung);
            }
        }
        return list;
    }


    //animation
    public static void setAnim_button_click(ImageButton button) {
        Animation btn_click = AnimationUtils.loadAnimation(button.getContext(), R.anim.anim_scale_button);
        button.startAnimation(btn_click);
    }

    public static Animation getAnimClick(Context context) {
        Animation itemClick = AnimationUtils.loadAnimation(context, R.anim.click_item);
        return itemClick;
    }


    @Override
    public void onBackPressed() {
        liner_layoutmenu.setVisibility(View.VISIBLE);
        rcv_menu_tudien.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpbackground.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpbackground.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpbackground.start();
        setUserView();
    }

    private void playMusic() {
        mpbackground = MediaPlayer.create(this, R.raw.waltz);
        mpbackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        mpbackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }


}