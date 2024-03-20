package com.george.memoshareapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.george.memoshareapp.activities.GroupChatActivity;
import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.WebSocketMessage;
import com.george.memoshareapp.events.ChatMessageEvent;
import com.george.memoshareapp.events.ForceLogoutEvent;
import com.george.memoshareapp.events.SendMessageEvent;
import com.george.memoshareapp.manager.ChatManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
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
        public ChatService getService() {
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
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
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
                Logger.d("WebSocket 收到消息" + text);
                Gson gson = new Gson();
                WebSocketMessage webSocketMessage = gson.fromJson(text, WebSocketMessage.class);
                if("session_closed".equals(webSocketMessage.getType())){
                    //处理logOut
                    handleForceLogout();
                    mWebSocket.close(1000, "session_closed");
                    Logger.d("WebSocket 收到消息" + webSocketMessage.getMessage());
                    return;
                }

                chatManager.updateLastReadTimeInThread();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create();
                        ChatMessage message = gson.fromJson(text, ChatMessage.class);
                        new ChatManager(ChatService.this).saveChatMessage(message);
                        if(!GroupChatActivity.isInGroupChatActivity) {
                            showPopNotification(message);
                        }

                EventBus.getDefault().post(new ChatMessageEvent(message)); //接收到的消息给到ChatActivity
                // 现在你可以操作message对象，例如显示消息内容
                Logger.d("WebSocket 收到消息"+message);}
                }).start();
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Logger.d("WebSocket 连接关闭"+"reason"+reason+"code"+code);
                // 评估关闭的原因
                if ("New session opened！！！".equals(reason)) {
                    // 如果原因是新会话已经打开，则处理强制注销逻辑
//                    handleForceLogout();
                }

                Logger.d("WebSocket 连接关闭" + "reason" + reason + "code" + code);
            }


            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);

                Logger.d("WebSocket 连接失败" + t.getMessage());
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
     *
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
    private void handleForceLogout() {
        // 发送EventBus事件通知UI层显示强制注销的Dialog
        EventBus.getDefault().post(new ForceLogoutEvent());

        // 关闭WebSocket连接
        if (mWebSocket != null) {
            mWebSocket.close(1000, "Logged in from another device");
        }

        // 结束服务
        stopSelf();
    }
    private void showPopNotification(ChatMessage message) {
        String content = "";
        if (message.getMessageType().equals("图片")) {
            content = "[图片]";
        } else if (message.getMessageType().equals("语音")) {
            content = "[语音]";
        } else if (message.getMessageType().equals("文本")) {
            content = message.getContent();
            if (content.length() > 10) {
                content = content.substring(0, 10) + "...";
            }
        }
        String ChatRoomName = "";
        if (message.getChatRoom().getType().equals("多人")) {
            ChatRoomName = message.getChatRoom().getName();
        } else {
            ChatRoomName= message.getUser().getName();
        }

        // 创建一个final变量来在内部类中使用
        final String finalContent = content;
        final String finalChatRoomName = ChatRoomName;
        Glide.with(this)
                .asBitmap()
                .load(AppProperties.SERVER_MEDIA_URL + message.getUser().getHeadPortraitPath()) // 替换为你的图片URL
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        PopNotification.show(message.getUser().getName(), finalContent)
                                .setIcon(resource)
                                .setButton("回复", new OnDialogButtonClickListener<PopNotification>() {
                                    @Override
                                    public boolean onClick(PopNotification baseDialog, View v) {
                                        Intent intent = new Intent(ChatService.this, GroupChatActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("chatRoomId", message.getChatRoom().getId()+"");
                                        intent.putExtra("chatRoomName", finalChatRoomName);
                                        startActivity(intent);
                                        return false;
                                    }
                                })
                                .showLong();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}