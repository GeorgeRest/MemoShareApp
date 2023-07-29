package com.george.memoshareapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.george.memoshareapp.beans.ChatMessage;
import com.george.memoshareapp.manager.RetrofitManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = "ws://192.168.1.3:6028/websocket/17";
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
                EventBus.getDefault().post(message);
                // 现在你可以操作message对象，例如显示消息内容
                Logger.d("WebSocket 收到消息"+message);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebSocket != null) {
            mWebSocket.close(1000, "Service destroyed");
        }
    }
}