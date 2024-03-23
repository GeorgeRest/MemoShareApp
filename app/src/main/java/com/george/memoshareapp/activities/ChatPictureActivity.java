package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatPictureAdapter;
import com.george.memoshareapp.beans.ChatAttachment;
import com.george.memoshareapp.manager.ChatManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatPictureActivity extends BaseActivity {

    private RecyclerView rv_picture_list;
    private ChatPictureAdapter mPictureAdapter;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_picture);
        rv_picture_list = findViewById(R.id.rv_picture_list);
        iv_back = findViewById(R.id.iv_back);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false);
        rv_picture_list.setLayoutManager(layoutManager);
        mPictureAdapter = new ChatPictureAdapter();
        Intent intent = getIntent();
        String chatRoomId = intent.getStringExtra("chatRoomId");
        List<ChatAttachment> chatPictureList = new ChatManager(this).getChatPictureList(chatRoomId);
        List<Object> pictureList = new ArrayList<>();
        if(chatPictureList!=null){
            Date preDate = new Date(0);
            for (ChatAttachment chatPicture :chatPictureList) {
                Date currentDate = new Date(Long.parseLong(chatPicture.getCreatedAt()));
                if (!isSameDay(preDate, currentDate)) {
                    pictureList.add(Long.parseLong(chatPicture.getCreatedAt()));
                    preDate = currentDate;
                }
                pictureList.add(chatPicture.getFilePath());
            }
        }
        iv_back.setOnClickListener(v -> finish());
        mPictureAdapter.setData(pictureList);
        mPictureAdapter.notifyDataSetChanged();
        rv_picture_list.setAdapter(mPictureAdapter);
    }
    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}