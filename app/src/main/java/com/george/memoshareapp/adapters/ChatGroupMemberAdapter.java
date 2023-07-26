package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.ChatGroupMoreActivity;
import com.george.memoshareapp.activities.ContactListActivity;
import com.george.memoshareapp.activities.NewPersonPageActivity;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.properties.AppProperties;

import java.io.Serializable;
import java.util.List;

public class ChatGroupMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_CUSTOM_IMAGE = 2;

    private Context context;
    private List<User> contacts;
    private List<User> FriendList;
    private String photoChatTitleName;

    public ChatGroupMemberAdapter(Context context, List<User> contacts,List<User> FriendList,String photoChatTitleName) {
        this.context = context;
        this.contacts = contacts;
        this.FriendList=FriendList;
        this.photoChatTitleName=photoChatTitleName;
    }

    @Override
    public int getItemCount() {
        return contacts.size() + 1; // 加上一个位置用于显示自定义图片
    }

    @Override
    public int getItemViewType(int position) {
        if (position == contacts.size()) {
            return VIEW_TYPE_CUSTOM_IMAGE; // 返回用于显示自定义图片的视图类型
        } else {
            return VIEW_TYPE_USER; // 返回用于显示用户头像和姓名的视图类型
        }
    }
    public List<User> getContacts(){
        return contacts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            // 返回用于显示用户头像和姓名的 ViewHolder 对象
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_photo_item, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_CUSTOM_IMAGE) {
            // 返回用于显示自定义图片的 ViewHolder 对象
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image_item, parent, false);
            return new CustomImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            // 处理显示用户头像和姓名的逻辑

            User contact = contacts.get(position);
            String imagePath = AppProperties.SERVER_MEDIA_URL + contact.getHeadPortraitPath();
            int cornerRadius = 20;
            Glide.with(context)
                    .load(imagePath)
                    .transform(new RoundedCorners(cornerRadius)) // 设置圆角
                    .into(((ViewHolder) holder).ivPhoto);
            ((ViewHolder) holder).name.setText(contact.getName());
            ((ViewHolder) holder).ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, NewPersonPageActivity.class);
                        intent.putExtra("user", contact);
                        context.startActivity(intent);
                    }
                }
            });
        } else if (holder instanceof CustomImageViewHolder) {
            // 处理显示自定义图片的逻辑
            CustomImageViewHolder customImageViewHolder = (CustomImageViewHolder) holder;
            // 在这里处理显示自定义图片的逻辑
            customImageViewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContactListActivity.class);
                    intent.putExtra("alreadyExitContacts", (Serializable) contacts);//包括我自己，和已经存在的朋友
                    intent.putExtra("chatTitleName",photoChatTitleName);
                    intent.putExtra("FriendList", (Serializable) FriendList);
                    intent.putExtra("comeFromChatGroupMoreActivity",true);
                    ((ChatGroupMoreActivity)context).startActivityForResult(intent, 2);
                    if (context instanceof ChatGroupMoreActivity) {
                        ((ChatGroupMoreActivity) context).finish();
                    }
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            ivPhoto = view.findViewById(R.id.chat_more_member_image_view);
            name = view.findViewById(R.id.chat_more_member_name);
        }
    }

    static class CustomImageViewHolder extends RecyclerView.ViewHolder {

        ImageView add;
        public CustomImageViewHolder(View view) {
            super(view);
             add = view.findViewById(R.id.chat_more_add_member_image_view);
        }
    }
}
