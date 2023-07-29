package com.george.memoshareapp.activities.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.service.ChatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatService mService;
    private boolean mBound = false;
    private ListView listview;
    private List<String> MessageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(ChatActivity.this,
                android.R.layout.simple_list_item_1,MessageList);
        listview = findViewById(R.id.listView);
        listview.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void dis(View view) {
        if (mBound) {
            unbindService(connection);
            mBound = false;
        }
    }

    public void conne(View view) {
        Intent intent = new Intent(this, ChatService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChatMessage message) {
    MessageList.add(message.getContent());
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(ChatActivity.this,
                android.R.layout.simple_list_item_1,MessageList);
        listview.setAdapter(adapter);
    }
}