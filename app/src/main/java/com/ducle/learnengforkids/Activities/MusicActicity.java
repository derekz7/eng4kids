package com.ducle.learnengforkids.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ducle.learnengforkids.DialogLoading;
import com.ducle.learnengforkids.DialogSetting;
import com.ducle.learnengforkids.PlayMusic;
import com.ducle.learnengforkids.R;
import com.ducle.learnengforkids.Adapter.VideoAdapter;
import com.ducle.learnengforkids.Module.VideoYT;
import com.google.android.youtube.player.YouTubeBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MusicActicity extends YouTubeBaseActivity {

    private ImageButton btnhome, btnsetting, btnspeak, btnmute;
    public static String API_KEY = "AIzaSyCntiH1cgw2RQRePto4Bto1BzLZzTk4rFQ";
    private String ID_PLAYLIST = "PLMakVDXRH33FN6-i1BMa0YeTp4qroBG42";
//    https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLMakVDXRH33FN6-i1BMa0YeTp4qroBG42&key=AIzaSyCntiH1cgw2RQRePto4Bto1BzLZzTk4rFQ
    public String URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + ID_PLAYLIST + "&key=" + API_KEY + "&maxResults=50";
    private RecyclerView rcv_playlist;
    private VideoAdapter videoAdapter;
    private List<VideoYT> videoYTList;
    DialogLoading dialogLoading;
    private DialogSetting dialogSetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        videoYTList = new ArrayList<>();
        dialogLoading = new DialogLoading(this);
        dialogSetting = new DialogSetting(this);
        MainMenuActivity.mpbackground.pause();
        new MyAsyncTask().execute();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MusicActicity.this, RecyclerView.HORIZONTAL, false);
        rcv_playlist.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(MusicActicity.this, videoYTList);
        videoAdapter.setVideoClickListener(new VideoAdapter.onVideoClickListener() {
            @Override
            public void onVideoClick(int position) {
                Intent intent = new Intent(MusicActicity.this, PlayVideoActivity.class);
                intent.putExtra("ID_VIDEO",videoYTList.get(position).getVideo_ID());
                startActivity(intent);
            }
        });

        buttonClick();
    }

    private void buttonClick() {
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              PlayMusic.playClick(v.getContext());
                MainMenuActivity.setAnim_button_click(btnhome);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },400);

            }
        });
        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.setAnim_button_click(btnsetting);
                PlayMusic.playClick(v.getContext());
               dialogSetting.show(Gravity.CENTER);
            }
        });

    }

    private void JSONYouTube(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        String title = "", urlImg = "", videoID = "";
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            for (int i = 0; i < jsonItems.length(); i++) {

                                JSONObject jsonItem = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");
                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                urlImg = jsonMedium.getString("url");
                                JSONObject jsonResoureID = jsonSnippet.getJSONObject("resourceId");
                                videoID = jsonResoureID.getString("videoId");
                                VideoYT videoYT = new VideoYT(title, urlImg, videoID);
                                videoYTList.add(videoYT);
                            }

                            videoAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MusicActicity.this);
                builder.setIcon(R.drawable.cancel);
                builder.setTitle("Lỗi");
                builder.setMessage("Không thể tải xuống dữ liệu\nKiểm tra kết nối mạng và thử lại!");
                builder.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void initUI() {
        rcv_playlist = findViewById(R.id.rcv_playlist);
        btnhome = findViewById(R.id.btnhomemusic);
        btnsetting = findViewById(R.id.btnsettingmusic);
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            dialogLoading.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONYouTube(URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            rcv_playlist.setAdapter(videoAdapter);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogLoading.dismissDialog();
                }
            },3000);

            super.onPostExecute(unused);
        }
    }

   

}
