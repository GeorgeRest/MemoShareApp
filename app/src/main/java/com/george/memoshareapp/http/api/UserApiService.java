package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.response.HttpData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("user/register")
    Call<HttpData<User>> uploadUser(@Body User user);

    @POST("user/updatePassword")
    Call<HttpData<User>> changePassword(@Body User user);

}

