package com.ducle.learnengforkids.Activities.Admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.ducle.learnengforkids.Adapter.ItemAdapter;
import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.FireBase.LoaiTuDB;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;
import java.util.List;

public class QuanLyActivity extends AppCompatActivity {
    private LinearLayout itemQLTu, itemQLUser;
    private ImageButton igbQLTu, igbQLUser, igbSetting, igbBack, igbAddLoai;
    private RecyclerView rcvLoai;
    private ItemAdapter itemAdapter;
    private List<LoaiTu> loaiTuList;
    public static List<TuVung> listTu;
    private LoaiTuDB db;
    private WordDB wordDB;
    public static MediaPlayer mp;
    private ImageView imgDSLoai,imgAnh;
    private Uri imgUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        getData();
        playMusic();
        setRecycleView();
        onClick();
    }

    private void setRecycleView() {
        rcvLoai.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvLoai.setLayoutManager(layoutManager);
////        gan gia tri
        itemAdapter = new ItemAdapter(this,loaiTuList);
        rcvLoai.setAdapter(itemAdapter);

    }

    private void getData() {
        db = new LoaiTuDB();
        wordDB = new WordDB();
        loaiTuList = db.getListLoai();
        listTu = wordDB.getListTuVung();
    }

    private void onClick() {
        igbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSetting dialogSetting = new DialogSetting(QuanLyActivity.this);
                dialogSetting.show(mp);
            }
        });

        igbQLTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideItem();
                rcvLoai.setVisibility(View.VISIBLE);
            }
        });
        igbQLUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this, QuanLyUser.class));
            }
        });

        itemAdapter.setOnItemClickListener(new ItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(QuanLyActivity.this, QuanLyTuVung.class);
                intent.putExtra("LoaiTu",loaiTuList.get(position));
                startActivity(intent);
            }
        });
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideItem();
                    }
                },400);

            }
        });
        igbAddLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_themLoai();
            }
        });
    }

    private void init() {
        igbAddLoai = findViewById(R.id.btnAddLoai);
        igbSetting = findViewById(R.id.btnSetting);
        igbQLTu = findViewById(R.id.igbQLTuVung);
        igbQLUser = findViewById(R.id.igbQuanLyUser);
        rcvLoai = findViewById(R.id.rcv_menu);
        itemQLUser = findViewById(R.id.itemQlUser);
        itemQLTu = findViewById(R.id.itemQlTu);
        rcvLoai.setVisibility(View.GONE);
        igbBack = findViewById(R.id.btnBack);
        imgDSLoai = findViewById(R.id.imgDSLoai);
        imgDSLoai.setVisibility(View.GONE);
        igbBack.setVisibility(View.GONE);
        igbAddLoai.setVisibility(View.GONE);
    }


    private void hideItem() {
        if (itemQLTu.getVisibility() == View.VISIBLE && itemQLUser.getVisibility() == View.VISIBLE) {
            rcvLoai.setVisibility(View.VISIBLE);
            itemQLTu.setVisibility(View.GONE);
            itemQLUser.setVisibility(View.GONE);
            imgDSLoai.setVisibility(View.VISIBLE);
            igbBack.setVisibility(View.VISIBLE);
            igbAddLoai.setVisibility(View.VISIBLE);
        } else {
            rcvLoai.setVisibility(View.GONE);
            itemQLTu.setVisibility(View.VISIBLE);
            itemQLUser.setVisibility(View.VISIBLE);
            imgDSLoai.setVisibility(View.GONE);
            igbBack.setVisibility(View.GONE);
            igbAddLoai.setVisibility(View.GONE);
        }

    }

    private void playMusic() {
        mp = MediaPlayer.create(this, R.raw.waltz);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                mp.setVolume(0.3f, 0.3f);
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    public void dialog_themLoai() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_newloaitu);
        Button btnSave, btnCancel,btnOpenLib;
        EditText edtName = dialog.findViewById(R.id.edtTenLoai);
        imgAnh = dialog.findViewById(R.id.imgAnhLoai);
        btnOpenLib = dialog.findViewById(R.id.btnOpenLib);
        btnSave = dialog.findViewById(R.id.igbSave);
        btnCancel = dialog.findViewById(R.id.igbCancel);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        windowAttributes.windowAnimations = R.style.Dialog_Animations;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                if (imgAnh != null && name.length() != 0){
                    db.upLoadToFireBase(QuanLyActivity.this,imgUri,loaiTuList.size()+1,name);
                    getData();
                    hideItem();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            finish();
                        }
                    },1500);

                }else {
                    Toast.makeText(QuanLyActivity.this, "Vui lòng nhập tên và chọn hình ảnh!" ,Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnOpenLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();
            imgAnh.setImageURI(imgUri);
        }
    }

    @Override
    public void onBackPressed() {
       hideItem();
    }
}
