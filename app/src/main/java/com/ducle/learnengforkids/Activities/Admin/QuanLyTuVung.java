package com.ducle.learnengforkids.Activities.Admin;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Activities.DangNhapActivity;
import com.ducle.learnengforkids.Adapter.ItemAdapter;
import com.ducle.learnengforkids.Adapter.TuVungAdapter;
import com.ducle.learnengforkids.FireBase.WordDB;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;

import java.util.List;

public class QuanLyTuVung extends AppCompatActivity {
    private List<TuVung> tuVungList;
    private TuVungAdapter tuVungAdapter;
    private RecyclerView lvDSTu;
    ImageButton igbAddWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qltuvung);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lvDSTu.setLayoutManager(layoutManager);
        tuVungAdapter = new TuVungAdapter(this);
        tuVungAdapter.setData(tuVungList);
        lvDSTu.setAdapter(tuVungAdapter);
    }

    private void init() {
        igbAddWord = findViewById(R.id.btnAddWord);
        lvDSTu = findViewById(R.id.rcv_listTV);
        tuVungList = DangNhapActivity.tuVungList;
    }
}
