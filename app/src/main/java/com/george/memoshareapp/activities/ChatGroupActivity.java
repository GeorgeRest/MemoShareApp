package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.interfaces.OnAddedContactListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity implements OnAddedContactListener {


    private List<User> contactList;
    private String photoChatName;
    private ImageView more;
    private TextView tv_title;
    private ImageView back;
    private List<User> friendList;
    private List<User> newAddList=new ArrayList<>();


    @Override
    public void onAddedContact(List<User> addedContactList) {
        newAddList=addedContactList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("comeFromChatGroupMoreActivity",false)){
            contactList = (List<User>) intent.getSerializableExtra("contact_list");
            photoChatName = intent.getStringExtra("photoChatTitleName");
        }else {
            contactList = (List<User>) intent.getSerializableExtra("contact_list");
            friendList = (List<User>) intent.getSerializableExtra("FriendList");
            photoChatName = intent.getStringExtra("photo_chat_name");
        }

        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.chat_group_iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(photoChatName);
        more = (ImageView) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatGroupActivity.this, ChatGroupMoreActivity.class);
                intent.putExtra("contact_list",(Serializable) contactList);
                intent.putExtra("FriendList",(Serializable) friendList);
                intent.putExtra("photo_chat_name",photoChatName);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




}