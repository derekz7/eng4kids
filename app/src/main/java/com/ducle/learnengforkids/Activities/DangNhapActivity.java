package com.ducle.learnengforkids.Activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.ducle.learnengforkids.Activities.Admin.QuanLyActivity;
import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;

import java.util.List;

public class DangNhapActivity extends AppCompatActivity {
    private EditText edtUsernameDK, edtMatKhauDK2, edtMatKhauDK;
    private Button btnDangNhap, btnDangKy, btnSignUp;
    private EditText edtUsername, edtMatKhau;
    private UserDB userDB;
    private SharedPreferences sharedPreferences;
    private ConstraintLayout mainlayout, layout_signUp, layout_login;
    private LinearLayout layout_chuacoTK;
    private MediaPlayer mpbg;
    private VideoView videoBg;
    private Animation alpha_layout, fade_out, fade_in;
    public static List<TuVung> tuVungList;
    public static List<User> userList;
    private WordDB wdb;
    private DialogLoading dialogLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        getData();
        String path = "android.resource://com.ducle.learnengforkids/" + R.raw.forestvid;
        Uri uri = Uri.parse(path);
        videoBg.setVideoURI(uri);
        videoBg.start();
        videoBg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        showLoginView();
         playIntro();
        onClick();

    }

    private void showLoginView(){
        layout_login.startAnimation(alpha_layout);
        layout_chuacoTK.startAnimation(alpha_layout);
    }


    private void onClick() {
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDangKy.startAnimation(animClick());
                PlayMusic.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout_login.startAnimation(fade_out);
                        layout_chuacoTK.startAnimation(fade_out);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layout_signUp.setVisibility(View.VISIBLE);
                                layout_signUp.startAnimation(fade_in);
                                layout_login.setVisibility(View.GONE);
                                layout_chuacoTK.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }, 800);

            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDangNhap.startAnimation(animClick());
                PlayMusic.playClick(v.getContext());
                if (!isConnected(DangNhapActivity.this)) {
                    dialogErrConn();
                } else {
                    if (checkDangNhap()) {
                        if (edtUsername.getText().toString().equalsIgnoreCase("admin")) {
                            startActivity(new Intent(DangNhapActivity.this, QuanLyActivity.class));
                            finish();
                        } else {
                            Intent intent = new Intent(DangNhapActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Thông tin tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playClick(v.getContext());
                btnSignUp.startAnimation(animClick());
                if (!isConnected(DangNhapActivity.this)) {
                    dialogErrConn();
                } else {
                    dangKy();
                }

            }
        });
    }

    private void dangKy() {
        String username = edtUsernameDK.getText().toString().trim();
        String password = edtMatKhauDK.getText().toString().trim();
        String pass = edtMatKhauDK2.getText().toString().trim();
        dialogLoading.show();
        if (username.length() == 0 || password.length() <= 6 || !password.equals(pass)) {
            dialogLoading.dismissDialog();
            Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đủ các trường!\nMật khẩu phải > 6 kí tự", Toast.LENGTH_SHORT).show();
        }else {
            if (userDB.checkUsername(username)) {
                userDB.taoTaiKhoan(new User(username,password), DangNhapActivity.this);
                userDB.reloadData(userList);
                dialogLoading.dismissDialog();
                hideSignUp();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DangNhapActivity.this);
                builder.setIcon(R.drawable.cancel);
                builder.setTitle("Tên tài khoản đã tồn tại");
                builder.setMessage("Vui lòng nhập tên tài khoản khác");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }

        }
    }


    private boolean checkDangNhap() {
        String username = edtUsername.getText().toString().trim();
        String pass = edtMatKhau.getText().toString().trim();
        User user = new User();
        boolean check = false;
        for (User user1 : userList) {
            if (user1.getUsername().equals(username) && user1.getMatKhau().equals(pass)) {
                check = true;
                user = user1;

            }
        }
        if (username.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")) {
            check = true;
        }
        if (check) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", pass);
            editor.putString("diem",String.valueOf(user.getDiem()));
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.remove("password");
            editor.remove("diem");
            editor.apply();
        }
        return check;
    }


    private Animation animClick() {
        Animation btn_click = AnimationUtils.loadAnimation(this, R.anim.click_item);
        return btn_click;
    }


    private void init() {
        videoBg = findViewById(R.id.video_bg);
        layout_chuacoTK = findViewById(R.id.layout_btnSignup);
        layout_login = findViewById(R.id.layout_login);
        layout_signUp = findViewById(R.id.layout_signup);
        mainlayout = findViewById(R.id.layout_background);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtMatKhau = findViewById(R.id.edtPassWord);
        edtMatKhauDK2 = findViewById(R.id.edtPassDK2);
        edtUsernameDK = findViewById(R.id.edtUsernameDK);
        edtMatKhauDK = findViewById(R.id.edtPassDK);
        btnSignUp = findViewById(R.id.btnSignUp);
        alpha_layout = AnimationUtils.loadAnimation(this, R.anim.alpha_flash_screen);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        dialogLoading = new DialogLoading(this);
    }

    private void getData() {
        wdb = new WordDB();
        userDB = new UserDB();
        userList = userDB.getListUser();
        tuVungList = wdb.getListTuVung();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("username", ""));
        edtMatKhau.setText(sharedPreferences.getString("password", ""));
    }


    private void hideSignUp() {
        edtUsernameDK.setText("");
        edtMatKhauDK.setText("");
        edtMatKhauDK2.setText("");
        layout_signUp.startAnimation(fade_out);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_login.setVisibility(View.VISIBLE);
                layout_chuacoTK.setVisibility(View.VISIBLE);
                layout_login.startAnimation(fade_in);
                layout_chuacoTK.startAnimation(fade_in);
                layout_signUp.setVisibility(View.GONE);
            }
        }, 1000);


    }


    @Override
    public void onBackPressed() {
        if (layout_login.getVisibility() == View.VISIBLE) {
            return;
        } else {
            hideSignUp();
        }

    }

    @Override
    protected void onPause() {
        videoBg.suspend();
        mpbg.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBg.resume();
        mpbg.start();
    }

    @Override
    protected void onDestroy() {
        videoBg.stopPlayback();
        super.onDestroy();
        mpbg.stop();
    }

    private boolean isConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiConn != null && wifiConn.isConnected() || mobileConn != null && mobileConn.isConnected());
    }

    private void dialogErrConn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DangNhapActivity.this);
        builder.setIcon(R.drawable.cancel);
        builder.setTitle(R.string.loi);
        builder.setMessage(R.string.checkConnmess);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    public void playIntro(){
        mpbg = MediaPlayer.create(this,R.raw.intro);
        mpbg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        mpbg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}