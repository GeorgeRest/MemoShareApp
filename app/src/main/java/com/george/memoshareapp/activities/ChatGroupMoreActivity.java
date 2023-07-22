package com.george.memoshareapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ContactInfo;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;

import java.util.List;

public class ChatGroupMoreActivity extends AppCompatActivity {

    private TextView photo_chat_title_name;
    private List<ContactInfo> contactList;
    private String photoChatTitleName;
    private ImageView chat_group_back;
    private SharedPreferences sp;
    private String phoneNumber;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_more);
        initView();
        Intent intent = getIntent();
        contactList = (List<ContactInfo>) intent.getSerializableExtra("contactList");//有图片
        photoChatTitleName = intent.getStringExtra("photoChatName");
        photo_chat_title_name.setText(photoChatTitleName);
        //根据list里面的contactinfo的电话号，找用户（朋友里）
        sp = getSharedPreferences("User", MODE_PRIVATE);
        //我
        phoneNumber = sp.getString("phoneNumber", "");
        UserManager userManager = new UserManager(this);
        User userMe = userManager.findUserByPhoneNumber(phoneNumber);
        String MyName = userMe.getName();
        String headPortraitPath = userMe.getHeadPortraitPath();
        int myHeadPath = Integer.valueOf(headPortraitPath);
        ContactInfo contactInfo = new ContactInfo(MyName, phoneNumber,myHeadPath);
        contactList.add(contactInfo);

        //ChatGroupMemberImageAdapter chatGroupMemberImageAdapter = new ChatGroupMemberImageAdapter(this, contactList);
//        recyclerView.setAdapter(chatGroupMemberImageAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);

        photo_chat_title_name = (TextView) findViewById(R.id.photo_chat_name);
        chat_group_back = (ImageView) findViewById(R.id.chat_group_back);
        chat_group_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}