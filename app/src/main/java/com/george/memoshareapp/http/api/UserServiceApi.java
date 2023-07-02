package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.response.HttpData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceApi {
    @POST("user/login")
    Call<HttpData<User>> loginUser(@Body User user);

    @POST("user/register")
    Call<HttpData<User>> uploadUser(@Body User user);

    @POST("user/loginVcCode")
    Call<HttpData<User>> loginVcCode(@Body User user);
    @POST("user/userIsExit")
    Call<HttpData<User>> userIsExit(@Body User user);

    @POST("user/updatePassword")
    Call<HttpData<User>> changePassword(@Body User user);


    @GET("user/countFollowing/{phoneNumber}")
    Call<Long> countFollowing(@Path("phoneNumber") String phoneNumber);


    @GET("user/countFans/{phoneNumber}")
    Call<Long> countFans(@Path("phoneNumber") String phoneNumber);

    @GET("user/countFriends/{phoneNumber}")
    Call<Long> countFriends(@Path("phoneNumber") String phoneNumber);




    @GET("user/getUser/{phoneNumber}")
    Call<HttpData<User>> getUserByPhoneNumber(@Path("phoneNumber") String phoneNumber);
}
