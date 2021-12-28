package com.ducle.learnengforkids.Activities.Admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Activities.DangNhapActivity;
import com.ducle.learnengforkids.Adapter.UserAdapter;
import com.ducle.learnengforkids.FireBase.UserDB;
import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.R;

import java.util.ArrayList;
import java.util.List;

public class QuanLyUser extends AppCompatActivity {
    private List<User> userList;
    private UserAdapter userAdapter;
    private EditText edtKeyWord;
    private ImageButton igbFind, igbBack;
    private RecyclerView rcvUser;
    private UserDB db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnguoidung);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUser.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(this, userList, new UserAdapter.onItemCLickListener() {
            @Override
            public void onClickDeleteItem(User user) {
                onClickDeleteUser(user);
            }
        });
        rcvUser.setAdapter(userAdapter);
        igbFind.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String keyword = edtKeyWord.getText().toString().trim();
                if (keyword.length() > 0){
                    List<User> list = findUser(keyword);
                    userAdapter.setData(list);
                }
                else{
                    userAdapter.setData(userList);
                }
            }
        });
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<User> findUser(String keyword) {
        List<User> list = new ArrayList<>();
        for (User user : userList){
            if (user.getUsername().contains(keyword)){
                list.add(user);
            }
        }
        return list;
    }

    private void onClickDeleteUser(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                userList.remove(user);
                db.deleteUser(QuanLyUser.this,user);
                db.reloadData(userList);
                userAdapter.setData(userList);
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void init(){
        db = new UserDB();
        userList = DangNhapActivity.userList;
        rcvUser = findViewById(R.id.lv_listUser);
        edtKeyWord = findViewById(R.id.edtKeyWord);
        igbFind = findViewById(R.id.igbFind);
        igbBack = findViewById(R.id.btnBackListUser);
    }


}
