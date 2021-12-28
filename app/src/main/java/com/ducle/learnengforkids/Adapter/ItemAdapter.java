package com.ducle.learnengforkids.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Module.LoaiTu;
import com.ducle.learnengforkids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewholder> {

    private Context mcontext;
    private List<LoaiTu> loaiTuList;
    private onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position, View v);
        void onItemLongCLick(int pos, View v);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }


    public void setData(List<LoaiTu> list) {
        this.loaiTuList = list;
        notifyDataSetChanged();
    }

    public ItemAdapter(Context mcontext, List<LoaiTu> mListItem) {
        this.mcontext = mcontext;
        this.loaiTuList = mListItem;
    }

    @NonNull
    @Override
    public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menudictionary, parent, false);
        return new ItemViewholder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewholder holder, int position) {
        LoaiTu item = loaiTuList.get(position);
        if (item == null) {
            return;
        }
        Picasso.with(mcontext).load(item.getUrl()).into(holder.img_item);
        holder.tv_name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        if (loaiTuList != null) {
            return loaiTuList.size();
        }
        return 0;
    }

    public class ItemViewholder extends RecyclerView.ViewHolder {

        private ImageView img_item;
        private TextView tv_name;

        public ItemViewholder(@NonNull View itemView,onItemClickListener listener) {
            super(itemView);
            img_item = itemView.findViewById(R.id.img_item);
            tv_name = itemView.findViewById(R.id.tvitem_name);

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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemLongCLick(position,v);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
