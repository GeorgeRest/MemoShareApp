package com.george.memoshareapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.events.ChatMessageEvent;
import com.george.memoshareapp.events.SendMessageEvent;
import com.george.memoshareapp.manager.ChatManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatService extends Service {

    private WebSocket mWebSocket;
    private final IBinder mBinder = new ChatService.LocalBinder();
    private ChatManager chatManager;

    public class LocalBinder extends Binder {
        public  ChatService getService() {
            return ChatService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chatManager = new ChatManager(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = AppProperties.getWebsocketUrl(UserManager.getSelfPhoneNumber(this));
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        mWebSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatManager.updateLastReadTimeInThread();
                    }
                }, 5000);
                Logger.d("WebSocket 连接成功");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                chatManager.updateLastReadTimeInThread();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create();
                        ChatMessage message = gson.fromJson(text, ChatMessage.class);
                        new ChatManager(ChatService.this).saveChatMessage(message);
                        EventBus.getDefault().post(new ChatMessageEvent(message)); //接收到的消息给到ChatActivity
                        // 现在你可以操作message对象，例如显示消息内容
                        Logger.d("WebSocket 收到消息"+message);
                    }
                }).start();

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);

                Logger.d("WebSocket 连接关闭"+"reason"+reason+"code"+code);
            }



            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);

                Logger.d("WebSocket 连接失败"+t.getMessage());
            }
        });
        return START_STICKY;
    }
    public void sendMessage(String message) {
        chatManager.updateLastReadTimeInThread();
        if (mWebSocket != null && message != null) {
            mWebSocket.send(message);
        }
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mWebSocket != null) {
            mWebSocket.close(1000, "Service destroyed");

        }
        super.onDestroy();
    }

    /**
     * 发送消息给服务器
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SendMessageEvent(SendMessageEvent event) {
        ChatMessage message = event.message;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 将Date对象格式化为字符串
        String createdAt = sdf.format(now);
        message.setCreatedAt(createdAt);
        message.setUpdatedAt(createdAt);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(message.getChatRoomId());
        chatRoom.setLastMessage(message.getContent());
        chatRoom.setLastMessageTime(createdAt);
        chatRoom.setLastMessageType(message.getMessageType());
        message.setChatRoom(chatRoom);
        chatManager.saveChatMessage(message);
        sendMessage(new Gson().toJson(message));
    }

}