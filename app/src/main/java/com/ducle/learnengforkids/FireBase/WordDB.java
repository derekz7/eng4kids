package com.ducle.learnengforkids.FireBase;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.Module.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class WordDB {
    private FirebaseDatabase database;
    private DatabaseReference wordRef;
    private StorageReference storeRef;
    private List<TuVung> listTuVung;

    public WordDB() {
        database = FirebaseDatabase.getInstance();
        wordRef = database.getReference("Words");
        storeRef = FirebaseStorage.getInstance().getReference();
        listTuVung = new ArrayList<>();
    }

    public List<TuVung> getListTuVung() {
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


    public List<TuVung> getListTubyLoai(List<TuVung> list, LoaiTu loaiTu) {
        List<TuVung> listTu = new ArrayList<>();
        for (TuVung tuVung : list) {
            if (tuVung.getLoaiTu().getId() == loaiTu.getId()) {
                listTu.add(tuVung);
            }
        }
        return listTu;
    }

    public void upLoadToFireBase(Context context, Uri uri, String noiDung, LoaiTu loaiTu) {
        StorageReference fileRef = storeRef.child(loaiTu.getName()).child(noiDung + "." + getFileExtension(uri, context));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        TuVung tuVung = new TuVung(noiDung, loaiTu, uri.toString());
                        wordRef.child(tuVung.getNoiDung()).setValue(tuVung);
                        Toast.makeText(context, "Thêm từ mới thành công!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri, Context context) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void deleteTuVung(Context context, TuVung tuVung) {
        wordRef.child(tuVung.getNoiDung()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateImg(Context context, Uri uri, TuVung tuVung, LoaiTu loaiTu) {
        StorageReference fileRef = storeRef.child(loaiTu.getName()).child(tuVung.getNoiDung() + "." + getFileExtension(uri, context));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        tuVung.setImgUrl(uri.toString());
                        Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
//                        wordRef.child(tuVung.getNoiDung()).updateChildren(tuVung.toMap(), new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//
//                            }
//                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
