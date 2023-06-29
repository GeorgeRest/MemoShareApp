package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.response.HttpData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServiceApi {
    @POST("user/login")
    Call<HttpData<User>> loginUser(@Body User user);
    @POST("user/loginVcCode")
    Call<HttpData<User>> loginVcCode(@Body User user);
}
