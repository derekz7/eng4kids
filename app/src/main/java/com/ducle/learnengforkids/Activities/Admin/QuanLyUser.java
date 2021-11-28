package com.ducle.learnengforkids.Activities.Admin;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Activities.DangNhapActivity;
import com.ducle.learnengforkids.Adapter.UserAdapter;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.R;

import java.util.List;

public class QuanLyUser extends AppCompatActivity {
    private List<User> userList;
    private UserAdapter userAdapter;
    private RecyclerView rcvUser;
    private UserDB db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnguoidung);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        userAdapter = new UserAdapter(this,userList);
        rcvUser.setLayoutManager(layoutManager);
        rcvUser.setAdapter(userAdapter);

    }
    private void init(){
        db = new UserDB();
        userList = DangNhapActivity.userList;
        rcvUser = findViewById(R.id.lv_listUser);

    }
}
