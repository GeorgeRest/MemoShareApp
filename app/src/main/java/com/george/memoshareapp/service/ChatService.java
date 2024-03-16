package com.george.memoshareapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.events.ChatMessageEvent;
import com.george.memoshareapp.events.ForceLogoutEvent;
import com.george.memoshareapp.events.SendMessageEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatService extends Service {

    private WebSocket mWebSocket;
    private final IBinder mBinder = new ChatService.LocalBinder();

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
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String phoneNumber = sp.getString("phoneNumber", "");
        String url = "ws://192.168.43.204:6028/websocket/"+phoneNumber;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        mWebSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Logger.d("WebSocket 连接成功");
                System.out.println(response.body()+"----------");

            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                ChatMessage message = gson.fromJson(text, ChatMessage.class);

                EventBus.getDefault().post(new ChatMessageEvent(message)); //接收到的消息给到ChatActivity
                // 现在你可以操作message对象，例如显示消息内容
                Logger.d("WebSocket 收到消息"+message);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Logger.d("WebSocket 连接关闭"+"reason"+reason+"code"+code);
                // 评估关闭的原因
                if ("New session opened！！！".equals(reason)) {
                    // 如果原因是新会话已经打开，则处理强制注销逻辑
                    handleForceLogout();
                }
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
        sendMessage(new Gson().toJson(message));
    }
//    private void handleForceLogout() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
//                .setSmallIcon(R.drawable.app_icon) // 设置通知图标
//                .setContentTitle("账号异常")
//                .setContentText("您的账号在另一设备登录了。您已被强制下线。")
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_MAX);
//
//        notificationManager.notify(0, builder.build());
//
//        // 关闭WebSocket连接
//        if (mWebSocket != null) {
//            mWebSocket.close(1000, "Logged in from another device");
//        }
//
//        // 结束服务
//        stopSelf();
//    }
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

}