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
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.interfaces.OnAddedContactListener;
import com.george.memoshareapp.manager.UserManager;

import java.io.Serializable;
import java.util.List;

public class ChatGroupMoreActivity extends AppCompatActivity  {

    private TextView photo_chat_title_name;
    private List<User> contactList;
    private String photoChatTitleName;
    private ImageView chat_group_back;
    private SharedPreferences sp;
    private String phoneNumber;
    private RecyclerView recyclerView;
    private List<User> friendList;
    private ChatGroupMemberAdapter chatGroupMemberImageAdapter;
    private List<User> addedContactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_more);

        initView();
        Intent intent = getIntent();
        if (intent.getBooleanExtra("comeFromContactListActivity",false)){
            addedContactList = (List<User>) intent.getSerializableExtra("addedContactList");
            OnAddedContactListener addedContactListener = new ChatGroupActivity();
            addedContactListener.onAddedContact(addedContactList);
            String chatTitleName = intent.getStringExtra("chatTitleName");
            photo_chat_title_name.setText(chatTitleName);
            chatGroupMemberImageAdapter = new ChatGroupMemberAdapter(this, addedContactList,friendList,photoChatTitleName);
            recyclerView.setAdapter(chatGroupMemberImageAdapter) ;
        }else {
            contactList = (List<User>) intent.getSerializableExtra("contact_list");
            friendList = (List<User>) intent.getSerializableExtra("FriendList");
            photoChatTitleName = intent.getStringExtra("photo_chat_name");
            photo_chat_title_name.setText(photoChatTitleName);

            phoneNumber = getSharedPreferences("User", MODE_PRIVATE).getString("phoneNumber", "");
            User userMe = new UserManager(this).findUserByPhoneNumber(phoneNumber);
            contactList.add(userMe);

             chatGroupMemberImageAdapter = new ChatGroupMemberAdapter(this, contactList,friendList,photoChatTitleName);
            recyclerView.setAdapter(chatGroupMemberImageAdapter) ;
        }





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
            public void onClick(View v) {
                finish();
            }});

    }



    @Override
    public void onBackPressed() {
        // Set the result data and pass the updated list of added contacts through the interface
        Intent resultIntent = new Intent();
        resultIntent.putExtra("addedContactList", (Serializable) addedContactList);
        setResult(RESULT_OK, resultIntent);
        // Finish the activity
        finish();
    }


}