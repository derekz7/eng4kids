package com.ducle.learnengforkids.Activities.Admin;

import static com.ducle.learnengforkids.Activities.Admin.QuanLyActivity.listTu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Adapter.TuVungAdapter;
import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

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
        tuVungAdapter.setOnItemClick(new TuVungAdapter.onItemCLickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                TuVung tuVung = tuVungList.get(pos);
                dialog_suaTuVung(tuVung);
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
        Toast.makeText(this, ""+loaiTu.getName(), Toast.LENGTH_SHORT).show();
        tuVungList = db.getListTubyLoai(listTu,loaiTu);
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
                if (imgAnh != null && name.length() != 0 && imgUri != null) {
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

    public void dialog_suaTuVung(TuVung tuVung) {
        Dialog dialogSua = new Dialog(this);
        dialogSua.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSua.setContentView(R.layout.dialog_themtumoi);
        Button btnSave, btnCancel, btnOpenLib;
        ImageButton igbCancel = dialogSua.findViewById(R.id.igbCancel_X);
        TextView tv_Title = dialogSua.findViewById(R.id.tvTitle);
        tv_Title.setText(tuVung.getNoiDung());
        EditText edtName = dialogSua.findViewById(R.id.editNoiDung);
        edtName.setText(tuVung.getNoiDung());
        edtName.setEnabled(false);
        imgAnh = dialogSua.findViewById(R.id.imgAnh);
        Picasso.with(this).load(tuVung.getImgUrl()).into(imgAnh);
        btnOpenLib = dialogSua.findViewById(R.id.btnChonAnh);
        btnSave = dialogSua.findViewById(R.id.igbSave);
        btnCancel = dialogSua.findViewById(R.id.igbCancel);
        btnCancel.setText(R.string.xoa);
        Window window = dialogSua.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        windowAttributes.windowAnimations = R.style.Dialog_Animations;
        window.setAttributes(windowAttributes);
        dialogSua.setCancelable(true);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyTuVung.this);
                builder.setIcon(R.drawable.cancel);
                builder.setTitle("Bạn có thực sự muốn xoá");
                builder.setMessage("Việc xoá dữ liệu sẽ không thể hoàn tác!");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tuVungList.remove(tuVung);
                        db.deleteTuVung(QuanLyTuVung.this,tuVung);
                        tuVungAdapter.setData(tuVungList);
                        dialog.dismiss();
                        dialogSua.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (imgAnh != null && imgUri != null) {
                    DialogLoading dialogLoading = new DialogLoading(v.getContext());
                    dialogLoading.show();
                    db.updateImg(QuanLyTuVung.this, imgUri, tuVung, loaiTu);
                    getData();
                    tuVungAdapter.setData(tuVungList);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoading.dismissDialog();
                            dialogSua.dismiss();
                            finish();
                        }
                    }, 1500);

                } else {
                    Toast.makeText(QuanLyTuVung.this, "Bạn chưa thay đổi ảnh", Toast.LENGTH_SHORT).show();
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
        igbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                PlayMusic.startAnimationClick(v,v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogSua.dismiss();
                    }
                },400);

            }
        });
        dialogSua.show();

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
