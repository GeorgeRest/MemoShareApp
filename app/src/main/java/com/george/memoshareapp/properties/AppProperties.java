package com.george.memoshareapp.properties;

public class AppProperties {
    private static final String SERVER_URL ="http://192.168.31.176"; //修改这个两个ip
    private static final String IP ="192.168.31.176";

    public static final String PORT =":6028/";

    //服务器 ip:82.156.138.114  port:6027

    public static final String SERVER =SERVER_URL+PORT;

    public static final String SERVER_MEDIA_URL=SERVER_URL+"/files/media/";
    public static final String SERVER_RECORD_URL=SERVER_URL+"/files/record/";
    public static final String SERVER_CHAT_URL="http://192.168.1.6/files/upload/";
    public static final String DEFAULT_AVATAR=SERVER_MEDIA_URL+"default_avatar.png";

    public static String getWebsocketUrl(String userId) {
        return "ws://"+IP+PORT+"websocket/"+userId;
    }
}
