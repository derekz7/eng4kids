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

import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TuVungAdapter extends RecyclerView.Adapter<TuVungAdapter.TuVungViewHolder> {

    private Context mcontext;
    private List<TuVung> listTu;
    private onItemCLickListener mListener;

    public interface onItemCLickListener{
        void onItemClick(int pos, View view);
    }
    public void setOnItemClick(onItemCLickListener listener){
        this.mListener = listener;
    }
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

        return new TuVungViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TuVungViewHolder holder, int position) {
        TuVung tuVung = listTu.get(position);
        if (tuVung == null) {
            return;
        }
        Picasso.with(mcontext).load(tuVung.getImgUrl()).into(holder.imgView);
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

        public TuVungViewHolder(@NonNull View itemView, onItemCLickListener listener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_Word);
            tvNoiDung = itemView.findViewById(R.id.tv_noiDungTu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position,v);
                        }
                    }
                }
            });
        }
    }

}
