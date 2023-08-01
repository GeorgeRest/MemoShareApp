package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Remind;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemindServiceApi {

    @POST("/remind/postRemind")
    Call<HttpData<Remind>> postRemind(@Body Remind remind);

    @GET("/remind/getRemind/{date}/{phone}")
    Call<HttpListData<Remind>> getRemind(@Path("date") String date, @Path("phone") String phone);

    @GET("/remind/getRemindDate/{phone}")
    Call<HttpListData<String>> getRemindDate( @Path("phone") String phone);

}
