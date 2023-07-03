package com.george.memoshareapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.ChangePasswordActivity;
import com.george.memoshareapp.activities.NewPersonPageActivity;
import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.RelationshipServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.view.NiceImageView;

import org.litepal.LitePal;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendBaseQuickAdapter extends BaseQuickAdapter<User, FriendBaseQuickAdapter.ViewHolder> {
    private  int choice;
    private Context context;
    private String target_number;
    private UserManager manager;
    private boolean isMe;
    private User userMe;


    public FriendBaseQuickAdapter(Context context, int choice, String target_phoneNumber,boolean isMe){
        this.context = context;
        this.choice = choice;
        this.target_number = target_phoneNumber;
        this.isMe = isMe;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @Nullable User otherUser) {
        manager = new UserManager(getContext());
        //Glide.with(context).load(user.getHeadPortraitPath()).into(viewHolder.niv_photo);    // 获取头像
        viewHolder.niv_photo.setImageResource(R.mipmap.app_icon);
        viewHolder.tv_friend_name.setText(otherUser.getName());
        viewHolder.tv_signature.setText(otherUser.getSignature());

        userMe = manager.findUserByPhoneNumber(target_number);
        //头像点击事件
        viewHolder.niv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, NewPersonPageActivity.class);
                intent1.putExtra("user", otherUser);
                context.startActivity(intent1);
            }
        });

        // 按钮点击事件
        if(isMe){
            RelationshipServiceApi serviceApi = RetrofitManager.getInstance().create(RelationshipServiceApi.class);
            Relationship relationship1 = new Relationship(userMe.getPhoneNumber(), otherUser.getPhoneNumber());
            if(choice == 0){
                viewHolder.btn_follow.setText("已关注");
                viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GradientDrawable background = (GradientDrawable) viewHolder.btn_follow.getBackground();
                        if (viewHolder.btn_follow.getText().equals("已关注")){
                            background.setColor(ContextCompat.getColor(v.getContext(), R.color.follow_btn));
                            viewHolder.btn_follow.setText("关注");
                            //todo 删除数据
                            Call<HttpData<Relationship>> call = serviceApi.unfollowUser(relationship1);
                            //manager.unfollowUser(userMe, otherUser);
                            call.enqueue(new Callback<HttpData<Relationship>>() {
                                @Override
                                public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                                    if (response.isSuccessful()) {
                                        Toasty.success(getContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toasty.error(getContext(), "取消关注失败，请重试", Toast.LENGTH_SHORT,true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {

                                    Toast.makeText(getContext(), "联网失败，请稍后", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (viewHolder.btn_follow.getText().equals("关注")){
                            background.setColor(ContextCompat.getColor(v.getContext(), R.color.white));
                            viewHolder.btn_follow.setText("已关注");
                            // todo 添加数据
                            //manager.followUser(userMe, otherUser);
                            Call<HttpData<Relationship>> call = serviceApi.followUser(relationship1);
                            call.enqueue(new Callback<HttpData<Relationship>>() {
                                @Override
                                public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                                    if (response.isSuccessful()) {
                                        Toasty.success(getContext(), "关注成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toasty.error(getContext(), "关注失败，请重试", Toast.LENGTH_SHORT,true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {
                                    Toast.makeText(getContext(), "联网失败，请稍后", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            } else if (choice == 1) {
                GradientDrawable background = (GradientDrawable) viewHolder.btn_follow.getBackground();
                String initiator_phoneNumber = otherUser.getPhoneNumber();
                //todo 检查我是否关注了otheruser
//                int status = manager.getStatus(target_number, initiator_phoneNumber);
//                if(status == 1){  //如果没关注
//                    viewHolder.btn_follow.setText("回关");
//                    background.setColor(ContextCompat.getColor(getContext(), R.color.follow_btn));
//                } else if (status == 3) { 如果关注
//                    viewHolder.btn_follow.setText("互相关注");
//                }

                Call<HttpData<Relationship>> followingCall = serviceApi.isFollowing(relationship1);
                followingCall.enqueue(new Callback<HttpData<Relationship>>() {
                    @Override
                    public void onResponse(Call<HttpData<Relationship>> call, Response<HttpData<Relationship>> response) {
                        if(response.isSuccessful()){
                            HttpData<Relationship> body = response.body();
                            int code = body.getCode();
                            if(code == 200){
                                viewHolder.btn_follow.setText("互相关注");
                            } else if (code == 401) {
                                viewHolder.btn_follow.setText("回关");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpData<Relationship>> call, Throwable t) {

                    }
                });
                //设置点击事件
                viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GradientDrawable background = (GradientDrawable) viewHolder.btn_follow.getBackground();
                        if (viewHolder.btn_follow.getText().equals("互相关注")){
                            background.setColor(ContextCompat.getColor(v.getContext(), R.color.follow_btn));
                            viewHolder.btn_follow.setText("回关");
                            //todo 删除数据
                            //manager.unfollowUser(userMe, otherUser);
                        } else if (viewHolder.btn_follow.getText().equals("回关")){
                            background.setColor(ContextCompat.getColor(v.getContext(), R.color.white));
                            viewHolder.btn_follow.setText("互相关注");
                            //todo 添加数据
                            //manager.followUser(userMe, otherUser);
                        }
                    }
                });
            } else if (choice == 2) {
                viewHolder.btn_follow.setText("发私信");
                viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo 弹出聊天框
                    }
                });
            }
        }else {
            viewHolder.btn_follow.setVisibility(View.GONE);
        }


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