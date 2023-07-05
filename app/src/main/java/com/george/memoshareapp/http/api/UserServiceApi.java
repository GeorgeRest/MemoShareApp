package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.response.HttpData;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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


    @Multipart
    @PUT("user/updateUser/{phoneNumber}")
    Call<Boolean> updateUser(
            @Path("phoneNumber") String phoneNumber,
            @Query("gender") String gender,
            @Query("signature") String signature,
            @Query("name") String name,
            @Query("region") String region,
            @Query("birthday") String birthday,
            @Part MultipartBody.Part headPortrait
    );

    @GET("user/getUser/{phoneNumber}")
    Call<HttpData<User>> getUserByPhoneNumber(@Path("phoneNumber") String phoneNumber);
}
