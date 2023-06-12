package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.view.NiceImageView;

public class FriendBaseQuickAdapter extends BaseQuickAdapter<User, FriendBaseQuickAdapter.ViewHolder> {


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @Nullable User user) {
        //todo 获取头像
        viewHolder.tv_friend_name.setText(user.getName());
        viewHolder.tv_signature.setText(user.getSignature());
        //todo 关注按钮
        viewHolder.btn_follow.setText("");
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_friend_list, null);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NiceImageView niv_photo;
        TextView tv_friend_name;
        TextView tv_signature;
        Button btn_follow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            niv_photo = itemView.findViewById(R.id.niv_photo);
            tv_friend_name = itemView.findViewById(R.id.tv_friend_name);
            tv_signature = itemView.findViewById(R.id.tv_signature);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }
}
