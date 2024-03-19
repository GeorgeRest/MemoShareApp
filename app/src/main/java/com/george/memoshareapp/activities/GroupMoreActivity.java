package com.george.memoshareapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ChatGroupMemberAdapter;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GroupMoreActivity extends BaseActivity {

    public static final int REQUEST_CODE_ADD_MEMBER = 1;
    private TextView photo_chat_title_name;
    private List<User> contactList = new ArrayList<>();
    private ImageView chat_group_back;
    private SharedPreferences sp;
    private String phoneNumber;
    private RecyclerView recyclerView;
    private ChatGroupMemberAdapter chatGroupMemberImageAdapter;
    private List<User> addedContactList;
    private String chatTitleName;
    private Intent intent;
    private ChatRoom chatRoom;
    private String chatRoomId;
    private RelativeLayout rlTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_more);
        intent = getIntent();
        initView();
        chatTitleName = intent.getStringExtra("ChatRoomName");
        chatRoomId = intent.getStringExtra("chatRoomId");
        photo_chat_title_name.setText(chatTitleName);
        UserManager userManager = new UserManager(this);
        chatRoom = LitePal.where("chatRoomId=?", chatRoomId).findFirst(ChatRoom.class);
        if(chatRoom.getType().equals("多人")){
            rlTime.setVisibility(View.VISIBLE);
        }else{
            rlTime.setVisibility(View.GONE);
        }
        List<ChatRoomMember> chatRoomMembers = LitePal.where("chatRoomId=?", chatRoomId).find(ChatRoomMember.class);
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            User user = userManager.findUserByPhoneNumber(chatRoomMember.getUserId() + "");
            contactList.add(user);
        }

        chatGroupMemberImageAdapter = new ChatGroupMemberAdapter(this, contactList, chatTitleName, chatRoomId);
        recyclerView.setAdapter(chatGroupMemberImageAdapter);
        int spanCount = 5;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_more_member_rv_image);
        photo_chat_title_name = (TextView) findViewById(R.id.photo_chat_name);
        chat_group_back = (ImageView) findViewById(R.id.chat_group_back);
        RelativeLayout rl_addLocation = (RelativeLayout) findViewById(R.id.rl_addLocation);
        rlTime = (RelativeLayout) findViewById(R.id.RL_time);
        TextView tv_exit = (TextView) findViewById(R.id.tv_exit);
        chat_group_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupMoreActivity.this, ChatPictureActivity.class);
                intent.putExtra("chatRoomId", chatRoomId);
                startActivity(intent);
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new XPopup.Builder(GroupMoreActivity.this)
                        .hasBlurBg(true)
                        .asConfirm("退出", "确定退出群聊么",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        Toasty.success(GroupMoreActivity.this, "退出成功");
                                    }
                                })
                        .show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_MEMBER && resultCode == RESULT_OK) {

            List<User> invitedUsers = (List<User>) data.getSerializableExtra("invitedUsers");
            for (User user : invitedUsers) {
                user.saveOrUpdate("phoneNumber=?", user.getPhoneNumber());
                ChatRoomMember chatRoomMember = new ChatRoomMember();
                chatRoomMember.setChatRoomId(Integer.parseInt(chatRoomId));
                chatRoomMember.setUserId(Long.parseLong(user.getPhoneNumber()));
                chatRoomMember.save();
            }
            chatGroupMemberImageAdapter.addAllUsers(invitedUsers);

        }
    }
}