package com.ducle.learnengforkids.FireBase;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ducle.learnengforkids.Module.CauDo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CauDoDB {
    private FirebaseDatabase database;
    private DatabaseReference Ref;
    private List<CauDo> cauDoList;
    private int maxid = 0;

    public CauDoDB() {
        database = FirebaseDatabase.getInstance();
        Ref = database.getReference("CauDo");
        cauDoList = getAllCauDo();
    }

    public void InsertCauDo(CauDo cauDo){
        cauDo.setId(maxid);
        Ref.child(String.valueOf(cauDo.getId())).setValue(cauDo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d("Insert","Error");
                }

            }
        });

    }

    public List<CauDo> getAllCauDo(){
        cauDoList = new ArrayList<>();
       Ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxid = (int) snapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CauDo cauDo = dataSnapshot.getValue(CauDo.class);
                    cauDoList.add(cauDo);
                }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       return cauDoList;
    }
    public void updateData(){
        Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CauDo cauDo = snapshot.getValue(CauDo.class);
                if (cauDo != null){
                    cauDoList.add(cauDo);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CauDo cauDo = snapshot.getValue(CauDo.class);
                if (cauDo == null){
                    return;
                }
                if (cauDoList.isEmpty()){
                    return;
                }
                for (int i = 0; i < cauDoList.size(); i++){
                    if (cauDo.getId() == cauDoList.get(i).getId()){
                        cauDoList.set(i,cauDo);
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

}
