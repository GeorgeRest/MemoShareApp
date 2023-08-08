package com.george.memoshareapp.http.api;


import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.ChatRoomRequest;
import com.george.memoshareapp.http.response.HttpData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatRoomApi {
    @POST("chat/createChatRoom")
    Call<ChatRoom> createChatRoom(@Body ChatRoomRequest chatRoomRequest);
    @POST("chat/addChatRoomMember")
    Call<HttpData<ChatRoom>> AddChatRoomMember(@Body List<ChatRoomMember> chatRoomMember);

}
