package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentServiceApi {
    @POST("/comment/postComment")
    Call<HttpData<CommentBean>> postComment(@Body CommentBean comment);

    @POST("comment/postReply")
    Call<HttpData<ReplyBean>> postReply(@Body ReplyBean reply);

    @POST("comment/postSubReply")
    Call<HttpData<ReplyBean>> postSubReply(@Body ReplyBean reply);

    @GET("comment/getComments/{postId}")
    Call<HttpListData<CommentBean>> getComments(@Path("postId") int postId);

    @GET("comment/getInform/{phoneNumber}")
    Call<HttpListData<CommentBean>> getInform(@Path("phoneNumber") String phoneNumber);

}



