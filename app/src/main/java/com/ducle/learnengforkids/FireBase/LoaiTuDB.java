package com.ducle.learnengforkids.FireBase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ducle.learnengforkids.Module.LoaiTu;
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

public class LoaiTuDB {
    private FirebaseDatabase database;
    private DatabaseReference loaiRef;
    private StorageReference storeRef;
    private List<LoaiTu> listLoai;

    public LoaiTuDB(){
        database = FirebaseDatabase.getInstance();
        loaiRef = database.getReference("LoaiTu");
        storeRef = FirebaseStorage.getInstance().getReference();
        listLoai = new ArrayList<>();
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
    public void upLoadToFireBase(Activity activity, Uri uri, int id, String name) {
        StorageReference fileRef = storeRef.child("LoaiTu").child(name + "." +getFileExtension(uri,activity));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        LoaiTu loaiTu = new LoaiTu(id,name,uri.toString());
                        loaiRef.child(String.valueOf(id)).setValue(loaiTu);
                        Toast.makeText(activity, "Thêm loại từ thành công!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri mUri, Activity activity){
        ContentResolver cr = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
