package com.george.memoshareapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatGroupMemberAdapter;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.ChatRoomManager;
import com.george.memoshareapp.manager.UserManager;

import java.util.List;

public class TestChatGroupMoreActivity extends AppCompatActivity {
    private TextView photo_chat_title_name;
    private List<User> contactList;
    private ImageView chat_group_back;
    private SharedPreferences sp;
    private String phoneNumber;
    private RecyclerView recyclerView;
    private List<User> friendList;
    private ChatGroupMemberAdapter chatGroupMemberImageAdapter;
    private List<User> addedContactList;
    private String chatTitleName;
    private Intent intent;
    private int chatRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_more);
        intent = getIntent();
        initView();
        chatTitleName = intent.getStringExtra("ChatRoomName");
        ChatRoom room = new ChatRoomManager().getChatRoomByChatRoomName(chatTitleName);//带时间
        chatRoomID = room.getId();
        contactList =new ChatRoomManager().getMembersByChatRoomNameId(chatRoomID);
        friendList = new UserManager(TestChatGroupMoreActivity.this).getAllUsersFromFriendUser();
        photo_chat_title_name.setText(chatTitleName);
        phoneNumber = getSharedPreferences("User", MODE_PRIVATE).getString("phoneNumber", "");
        User userMe = new UserManager(this).findUserByPhoneNumber(phoneNumber);
        boolean userMeExitInContectList=false;
        for (User u:contactList) {
            if ((u.getPhoneNumber()).equals(userMe.getPhoneNumber())){
                userMeExitInContectList=true;
                break;
            }
        }
        if (!userMeExitInContectList){
            contactList.add(userMe);
        }
        chatGroupMemberImageAdapter = new ChatGroupMemberAdapter(this, contactList,friendList,chatTitleName,chatRoomID);
        recyclerView.setAdapter(chatGroupMemberImageAdapter) ;
        int spanCount = 5; // 设置网格的列数
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_more_member_rv_image);
        photo_chat_title_name = (TextView) findViewById(R.id.photo_chat_name);
        chat_group_back = (ImageView) findViewById(R.id.chat_group_back);
        chat_group_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        contactList = new ChatRoomManager().getMembersByChatRoomNameId(chatRoomID);
        friendList = new UserManager(TestChatGroupMoreActivity.this).getAllUsersFromFriendUser();
        chatGroupMemberImageAdapter = new ChatGroupMemberAdapter(this, contactList,friendList,chatTitleName,chatRoomID);
        recyclerView.setAdapter(chatGroupMemberImageAdapter) ;
    }

}