package com.ducle.learnengforkids.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TuVungAdapter extends RecyclerView.Adapter<TuVungAdapter.TuVungViewHolder> {

    private Context mcontext;
    private List<TuVung> listTu;


    public TuVungAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setData(List<TuVung> list){
        this.listTu = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TuVungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tuvung,parent,false);

        return new TuVungViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuVungViewHolder holder, int position) {
        TuVung tuVung = listTu.get(position);
        if (tuVung == null) {
            return;
        }
        Glide.with(mcontext).load(tuVung.getImgUrl()).into(holder.imgView);
        holder.tvNoiDung.setText(tuVung.getNoiDung());
    }

    @Override
    public int getItemCount() {
        if (listTu != null){
            return listTu.size();
        }
        return 0;
    }

    public class TuVungViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgView;
        private TextView tvNoiDung;
        private ImageButton igbDel;

        public TuVungViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_Word);
            tvNoiDung = itemView.findViewById(R.id.tv_noiDungTu);
            igbDel = itemView.findViewById(R.id.igbDelWord);
        }
    }

}
