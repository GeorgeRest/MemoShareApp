package com.george.memoshareapp.http.api;


import com.george.memoshareapp.beans.ChatRoom;
import com.george.memoshareapp.beans.ChatRoomMember;
import com.george.memoshareapp.beans.ChatRoomRequest;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.response.HttpData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ChatRoomApi {
    @POST("chat/createChatRoom")
    Call<ChatRoomRequest> createChatRoom(@Body ChatRoomRequest chatRoomRequest);
    @POST("chat/addChatRoomMember")
    Call<HttpData<ChatRoom>> AddChatRoomMember(@Body List<ChatRoomMember> chatRoomMember);

    @GET("chat/getChatRoomAndMembers/{userId}")
    Call<ChatRoomRequest> getChatRoomAndMembers(@Path("userId") String userId);

    @PUT("chat/user/{userId}/chatrooms/{chatRoomId}/read")
    Call<Void> updateLastReadAt(@Path("userId") String userId, @Path("chatRoomId") Integer chatRoomId);

    @POST("chat/addChatRoomMember")
    Call<List<User>> addChatRoomMember(@Body List<ChatRoomMember> chatRoomMember);
}