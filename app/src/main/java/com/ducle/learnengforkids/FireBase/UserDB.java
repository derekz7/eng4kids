package com.ducle.learnengforkids.FireBase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.Module.User;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private FirebaseDatabase database;
    private DatabaseReference UserRef;
    private List<User> userList;

    public UserDB() {
        database = FirebaseDatabase.getInstance();
        UserRef = database.getReference("User");
        userList = new ArrayList<>();
    }

    public void taoTaiKhoan(User user, Activity activity) {
        UserRef.child(user.getUsername()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(activity, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public List<User> getListUser(){
        userList = new ArrayList<>();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return userList;
    }

    public void reloadData(List<User> list){
        UserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    list.add(user);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user == null){
                    return;
                }
                if (list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (user.getUsername().equals(list.get(i).getUsername())){
                        list.set(i,user);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean checkUsername(String username) {
        boolean kt = true;
        if (userList == null){
            return true;
        }
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                kt = false;
                break;
            }
        }
        return kt;
    }
    public void updateDiem(String username, int diem){
       UserRef.child(username).child("diem").setValue(diem, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Log.d("UpdateDiem","Update diem thanh cong");
           }
       });

    }

    public User getUserbyUsername(List<User> list, String username){
        User user = new User();
        for (User user1: list){
            if (user1.getUsername().equals(username)){
                user = user1;
            }
        }
        return user;
    }
    public User getUser(String username){
        final User[] userLog = {new User()};
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getUsername().equals(username)){
                        userLog[0] = user;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return userLog[0];
    }

}
