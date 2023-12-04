package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.GroupChatActivity;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.view.NiceImageView;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.listener.OnSubItemClickListener;

import java.util.Arrays;
import java.util.List;

import cn.mtjsoft.groupavatarslib.GroupAvatarsLib;
import cn.mtjsoft.groupavatarslib.layout.DingLayoutManager;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.adapters
 * @className: ChatRoomListAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/11/21 19:27
 * @version: 1.0
 */
public class ChatRoomListAdapter extends BaseQuickAdapter<ChatRoom, ChatRoomListAdapter.ViewHolder> {

    private final User selfUser;
    private Context context;
    private UserManager userManager;

    public ChatRoomListAdapter(Context context) {
        this.context = context;
        userManager = new UserManager(context);
        selfUser = userManager.findUserByPhoneNumber(UserManager.getSelfPhoneNumber(context));

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @Nullable ChatRoom chatRoom) {
        viewHolder.tv_chatroom_last_message_time.setText(chatRoom.getLastMessageTime());
        String type = chatRoom.getType();
        if (chatRoom.getLastMessageType() != null) {
            if (chatRoom.getLastMessageType().equals("图片")) {
                viewHolder.tv_chatroom_last_message.setText("[图片]");
            } else if (chatRoom.getLastMessageType().equals("语音")) {
                viewHolder.tv_chatroom_last_message.setText("[语音]");
            } else {
                viewHolder.tv_chatroom_last_message.setText(chatRoom.getLastMessage());
            }
        }else{
            viewHolder.tv_chatroom_last_message.setText("");
        }
        if (type.equals("单人")) {
            String userId = chatRoom.getChatRoomMember().getUserId() + "";
            User user = userManager.findUserByPhoneNumber(userId);
            chatRoom.setName(user.getName());
            viewHolder.tv_chatroom_name.setText(user.getName());
            String headPortraitPath = user.getHeadPortraitPath();
            Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + headPortraitPath).into(viewHolder.iv_chatroom_photo);
        } else {
            String[] avatarArray = splitIntoArray(chatRoom.getAvatar());
            viewHolder.tv_chatroom_name.setText(chatRoom.getName());
            GroupAvatarsLib.init(context)
                    .setSize(40)
                    .setLayoutManager(new DingLayoutManager())
                    .setGroupId(chatRoom.getChatRoomId() + "")
                    .setDatas(Arrays.asList(avatarArray))
                    .setImageView(viewHolder.iv_chatroom_photo)
                    .build();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupChatActivity.class).putExtra("chatRoomId", chatRoom.getChatRoomId()+"").putExtra("chatRoomName", chatRoom.getName());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_chat_room, null);
        return new ChatRoomListAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NiceImageView iv_chatroom_photo;
        private TextView tv_chatroom_name;
        private TextView tv_chatroom_last_message;
        private TextView tv_chatroom_last_message_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_chatroom_photo = itemView.findViewById(R.id.iv_chatroom_photo);
            tv_chatroom_name = itemView.findViewById(R.id.tv_chatroom_name);
            tv_chatroom_last_message = itemView.findViewById(R.id.tv_chatroom_last_message);
            tv_chatroom_last_message_time = itemView.findViewById(R.id.tv_chatroom_last_message_time);
        }

    }

    public String[] splitIntoArray(String input) {
        // 使用 '+' 分割输入字符串
        String[] splitArray = input.split("\\+");
        // 遍历分割后的数组，并为每个元素添加前缀
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = AppProperties.SERVER_MEDIA_URL + splitArray[i];
        }
        return splitArray;
    }

}
