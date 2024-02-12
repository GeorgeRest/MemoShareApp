package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.http.response.HttpListData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface HuodongServiceApi {
    @Multipart
    @POST("api/activitypublish")
    Call<ResponseBody> publishActivity(
            @Part List<MultipartBody.Part> files,
            @PartMap Map<String, RequestBody> fields);

    @GET("api/getActivitys")
    Call<HttpListData<InnerActivityBean>> getActivitys(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("api/getPersonalActivitys")
    Call<HttpListData<InnerActivityBean>> getPersonalActivitys(@Query("phoneNumber") String phoneNumber, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("api/getInnerActivitys")
    Call<HttpListData<InnerActivityBean>> getInnerActivitys(@Query("followId") int followId,@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("api/getImagesById")
    Call<List<String>> getImagesById(@Query("activityId") int activityId);

    @GET("api/deleteActivitys")
    Call<ResponseBody> deletePersonalHuoDong(@Query("activityIds") List<Integer> activityIds);

}
