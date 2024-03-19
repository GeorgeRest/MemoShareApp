package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.GroupChatActivity;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.manager.ChatRoomManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatRoomAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRoom> dataList; // ChatRoomItem 是自定义数据模型类

    public ChatRoomAdapter(Context context, List<ChatRoom> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_chatroom, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageView = convertView.findViewById(R.id.chatroom_photo);
            viewHolder.nameTextView = convertView.findViewById(R.id.chatroom_name);
            viewHolder.messageTextView = convertView.findViewById(R.id.chatroom_last_message);
            viewHolder.timeTextView = convertView.findViewById(R.id.chatroom_last_message_time);
            viewHolder.rl = convertView.findViewById(R.id.chat_RL);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChatRoom chatRoom = dataList.get(position);

        // 设置数据到视图中
//        viewHolder.imageView.setImageResource(chatRoom.get());
        viewHolder.nameTextView.setText(chatRoom.getName());
        ChatRoomManager chatRoomManager = new ChatRoomManager();
        String latestMessage = chatRoomManager.getLatestMessageContentByChatRoomName(chatRoom.getName());
        viewHolder.messageTextView.setText(latestMessage);
        String lastTime = chatRoomManager.getLatestMessageTimeByChatRoomName(chatRoom.getName());
//        String s = formatMessageTime(lastTime);
        viewHolder.timeTextView.setText(lastTime);
        // 设置点击事件
        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的聊天室信息
                ChatRoom chatRoom = dataList.get(position);

                // 启动 ChatGroupActivity 并传递聊天室信息
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("ChatRoomName", chatRoom.getName());// 传递聊天室ID或其他需要的信息
//                intent.putExtra("adapter",1);// 传递聊天室ID或其他需要的信息
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // ViewHolder 模式
    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView messageTextView;
        TextView timeTextView;
        RelativeLayout rl;
    }
    public String formatMessageTime(Date messageDate) {
        if (messageDate == null) {
            return "";
        }

        Date currentDate = new Date(); // 当前日期

        // 计算两个日期之间的时间差（毫秒）
        long timeDiff = currentDate.getTime() - messageDate.getTime();

        // 定义时间格式化器
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // 如果消息时间是今天
        if (isSameDay(currentDate, messageDate)) {
            return dateFormat.format(messageDate); // 返回格式化的时间（HH:mm）
        }

        // 如果消息时间是昨天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 减去一天
        if (isSameDay(calendar.getTime(), messageDate)) {
            return "昨天 " + dateFormat.format(messageDate); // 返回格式化的时间（昨天 HH:mm）
        }

        // 如果消息时间在一周内
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7); // 减去一周
        if (messageDate.after(calendar.getTime())) {
            // 返回消息时间的星期几
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayOfWeekFormat.format(messageDate) + " " + dateFormat.format(messageDate);
        }

        // 如果消息时间不在一周内，返回具体的日期
        SimpleDateFormat dateFormatWithMonth = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return dateFormatWithMonth.format(messageDate);
    }

    // 判断两个日期是否是同一天
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

}
