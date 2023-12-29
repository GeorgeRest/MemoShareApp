package com.george.memoshareapp.properties;

public class AppProperties {
    private static final String SERVER_URL ="http://11.1.1.21"; //修改这个两个ip
    private static final String IP ="11.1.1.21";

    public static final String PORT =":6028/";



    public static final String SERVER =SERVER_URL+PORT;

    public static final String SERVER_MEDIA_URL=SERVER_URL+"/files/media/";
    public static final String SERVER_RECORD_URL=SERVER_URL+"/files/record/";
    public static final String SERVER_CHAT_URL="http://192.168.1.6/files/upload/";
    public static final String DEFAULT_AVATAR=SERVER_MEDIA_URL+"default_avatar.png";

    public static String getWebsocketUrl(String userId) {
        return "ws://"+IP+PORT+"websocket/"+userId;
    }
}
