package com.ducle.learnengforkids.FireBase;


import androidx.annotation.NonNull;

import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordDB {
    private FirebaseDatabase database;
    private DatabaseReference loaiRef, wordRef;
    private List<LoaiTu> listLoai;
    private List<TuVung> listTuVung;

    public WordDB() {
        database = FirebaseDatabase.getInstance();
        loaiRef = database.getReference("LoaiTu");
        wordRef = database.getReference("Words");
        listLoai = new ArrayList<>();
        listTuVung = new ArrayList<>();
    }

    public List<TuVung> getListTuVung(){
        listTuVung = new ArrayList<>();
        wordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTuVung.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TuVung tuVung = dataSnapshot.getValue(TuVung.class);
                        listTuVung.add(tuVung);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listTuVung;
    }

    public List<LoaiTu> getListLoai(){
        listLoai = new ArrayList<>();
        loaiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoai.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    LoaiTu loaiTu = dataSnapshot.getValue(LoaiTu.class);
                    listLoai.add(loaiTu);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return listLoai;
    }


}
