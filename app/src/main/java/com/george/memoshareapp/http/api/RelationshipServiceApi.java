package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.Relationship;
import com.george.memoshareapp.http.response.HttpData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RelationshipServiceApi {

    @POST("relationship/isFollowing")
    Call<HttpData<Relationship>> isFollowing(@Body Relationship relationship);
    @POST("relationship/followUser")
    Call<HttpData<Relationship>> followUser(@Body Relationship relationship);
    @POST("relationship/unfollowUser")
    Call<HttpData<Relationship>> unfollowUser(@Body Relationship relationship);




}
