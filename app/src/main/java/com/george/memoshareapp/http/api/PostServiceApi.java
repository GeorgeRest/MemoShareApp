package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.http.response.HttpListData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostServiceApi {

    @Multipart
    @POST("api/post")
    Call<ResponseBody> publishData(
            @Part List<MultipartBody.Part> files,
            @PartMap Map<String, RequestBody> fields
    );

    @GET("api/getPosts")
    Call<HttpListData<Post>> getPosts(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("phoneNumber") String phoneNumber);

    @PUT("/api/{phoneNumber}/{postId}/like")
    Call<Integer> updateLikeCount(@Path("phoneNumber") String phoneNumber, @Path("postId") int postId, @Query("isLiked") boolean isLiked);

    @GET("/api/{postId}/like/count")
    Call<Integer> getLikeCount(@Path("postId") int postId);

    @GET("api/getPostsByPhoneNumber")
    Call<HttpListData<Post>> getPostsByPhoneNumber(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("phoneNumber") String phoneNumber);
    @GET("api/getFriendPosts")
    Call<HttpListData<Post>> getFriendPosts(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("phoneNumber") String phoneNumber);

    @GET("api/getLikePostsByPhoneNumber")
    Call<HttpListData<Post>> getLikePostsByPhoneNumber(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("phoneNumber") String phoneNumber);
}
