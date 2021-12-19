package com.ducle.learnengforkids.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducle.learnengforkids.Module.User;
import com.ducle.learnengforkids.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private onItemCLickListener mListener;

    public UserAdapter(Context context, List<User> userList, onItemCLickListener listener) {
        this.context = context;
        this.userList = userList;
        this.mListener = listener;
    }
    public interface onItemCLickListener{
        void onClickDeleteItem(User user);
    }

    public void setData(List<User> list){
        this.userList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) {
            return;
        }
        holder.tv_username.setText(user.getUsername());
        holder.tvPass.setText("password: "+user.getMatKhau());
        holder.igbXoaUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDeleteItem(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tvPass;
        private ImageButton igbXoaUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tvPass = itemView.findViewById(R.id.tv_pass);
            igbXoaUser = itemView.findViewById(R.id.igbXoaUser);
        }
    }
}
