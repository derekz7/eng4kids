package com.ducle.learnengforkids.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Module.TuVung;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ObjAdapter extends RecyclerView.Adapter<ObjAdapter.WordViewHolder> {

    private Context context;
    private List<TuVung> listWord;
    private ObjAdapter.onObjectClickListener mListener;


    public interface onObjectClickListener {
        void onObjectClick(int position, View v);
    }

    public void setOnWordClickListener(onObjectClickListener listener) {
        mListener = listener;
    }

    public ObjAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<TuVung> list){
        this.listWord = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_alphabet,parent,false);
        return new WordViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjAdapter.WordViewHolder holder, int position) {
        TuVung word = listWord.get(position);
        Picasso.with(context).load(word.getImgUrl()).into(holder.imv_word);
    }

    @Override
    public int getItemCount() {
        if (listWord != null) {
            return listWord.size();
        }
        return 0;
    }

    public static class WordViewHolder  extends RecyclerView.ViewHolder{
        private ImageView imv_word;

        public WordViewHolder(@NonNull View itemView, onObjectClickListener listener) {
            super(itemView);
            imv_word = itemView.findViewById(R.id.imv_word);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener!= null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onObjectClick(position,v);
                        }
                    }
                }
            });
        }
    }
}
