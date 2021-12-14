package com.ducle.learnengforkids.Activities.Admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Adapter.TuVungAdapter;
import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;

import java.util.List;

public class QuanLyTuVung extends AppCompatActivity {
    private List<TuVung> tuVungList;
    private TuVungAdapter tuVungAdapter;
    private RecyclerView lvDSTu;
    private ImageButton igbAddWord, btnBackListWords;
    private ImageView imgAnh;
    private Uri imgUri;
    private WordDB db;
    private LoaiTu loaiTu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qltuvung);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getData();
        init();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        lvDSTu.setLayoutManager(layoutManager);
        tuVungAdapter = new TuVungAdapter(this);
        tuVungAdapter.setData(tuVungList);
        lvDSTu.setAdapter(tuVungAdapter);
        onClick();
    }

    private void onClick() {
        btnBackListWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        igbAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_themTuMoi();
            }
        });
    }

    private void init() {
        igbAddWord = findViewById(R.id.btnAddWord);
        btnBackListWords = findViewById(R.id.btnBackListWords);
        igbAddWord = findViewById(R.id.btnAddWord);
        lvDSTu = findViewById(R.id.rcv_listTV);
    }

    private void getData() {
        db = new WordDB();
        loaiTu = (LoaiTu) getIntent().getSerializableExtra("LoaiTu");
        tuVungList = db.getListTubyLoai(loaiTu);
    }

    public void dialog_themTuMoi() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themtumoi);
        Button btnSave, btnCancel, btnOpenLib;
        EditText edtName = dialog.findViewById(R.id.editNoiDung);
        imgAnh = dialog.findViewById(R.id.imgAnh);
        btnOpenLib = dialog.findViewById(R.id.btnChonAnh);
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
                if (imgAnh != null && name.length() != 0) {
                    DialogLoading dialogLoading = new DialogLoading(v.getContext());
                    dialogLoading.show();
                    db.upLoadToFireBase(QuanLyTuVung.this, imgUri, name, loaiTu);
                    getData();
                    tuVungAdapter.setData(tuVungList);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoading.dismissDialog();
                            dialog.dismiss();
                            finish();
                        }
                    }, 1500);

                } else {
                    edtName.setError("Vui lòng nhập nội dung và chọn hình ảnh");
                }

            }
        });
        btnOpenLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            imgAnh.setImageURI(imgUri);
        }

    }
}
