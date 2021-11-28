package com.ducle.learnengforkids.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Module.VideoYT;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<VideoYT> videoYTList;
    private onVideoClickListener mListener;

    public interface onVideoClickListener {
        void onVideoClick(int position);
    }

    public void setVideoClickListener(onVideoClickListener listener) {
        mListener = listener;
    }

    private void setData(List<VideoYT> list) {
        this.videoYTList = list;
        notifyDataSetChanged();
    }

    public VideoAdapter(Context context, List<VideoYT> videoYTList) {
        this.context = context;
        this.videoYTList = videoYTList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_videoyoutube, parent, false);
        return new VideoViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        VideoYT videoYT = videoYTList.get(position);
        if (videoYT == null) {
            return;
        }
        holder.tvTitle.setText(videoYT.getTitle());
        Picasso.with(context).load(videoYT.getThumbnail()).into(holder.img_thumbnail);


    }

    @Override
    public int getItemCount() {
        if (videoYTList != null) {
            return videoYTList.size();
        }
        return 0;
    }


    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_thumbnail;
        private TextView tvTitle;

        public VideoViewHolder(@NonNull View itemView, onVideoClickListener listener) {
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.imv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onVideoClick(position);
                        }
                    }
                }
            });
        }
    }

}
