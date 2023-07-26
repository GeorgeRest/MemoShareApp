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
    private String chatTitleName;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_more);
        intent = getIntent();
        initView();

        if (intent.getBooleanExtra("comeFromContactListActivity",false)){
            addedContactList = (List<User>) intent.getSerializableExtra("addedContactList");

            chatTitleName = intent.getStringExtra("chatTitleName");
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
                // 在返回按钮被点击时，设置返回数据并结束当前页面
                if (intent.getBooleanExtra("comeFromContactListActivity",false)){
                    Intent resultIntent = new Intent(ChatGroupMoreActivity.this, ChatGroupActivity.class);
                    resultIntent.putExtra("addedContactList", (Serializable) chatGroupMemberImageAdapter.getContacts());
                    System.out.println("============--"+chatGroupMemberImageAdapter.getContacts());
                    resultIntent.putExtra("comeFromChatGroupMoreActivity",true);
                    resultIntent.putExtra("photoChatTitleName",chatTitleName);
                    startActivity(resultIntent);
                }


                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // 获取从C页面返回的数据
            List<User> resultData = (List<User>) data.getSerializableExtra("resultData");
            // 根据获取的数据进行后续处理
            if (resultData != null && !resultData.isEmpty()) {
                contactList=resultData;
            }

        }
    }



    @Override
    public void onBackPressed() {
        // Set the result data and pass the updated list of added contacts through the interface
        Intent resultIntent = new Intent(this,ChatGroupActivity.class);
        resultIntent.putExtra("addedContactList", (Serializable) addedContactList);
        setResult(RESULT_OK, resultIntent);
        // Finish the activity
        finish();
    }


}