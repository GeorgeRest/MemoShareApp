package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentServiceApi {
    @GET("comment/getComments")
    Call<HttpListData<CommentBean>> getComments(@Query("postId") int postId);

    @POST("comment/postComment")
    Call<HttpData<CommentBean>> postComment(@Body CommentBean comment);

    @POST("comment/postReply")
    Call<HttpData<ReplyBean>> postReply(@Body ReplyBean reply);

    @POST("comment/postSubReply")
    Call<HttpData<ReplyBean>> postSubReply(@Body ReplyBean reply);

}



