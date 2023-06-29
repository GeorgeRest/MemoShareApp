package com.george.memoshareapp.http.api;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface PostApiService {

    @Multipart
    @POST("api/post")
    Call<ResponseBody> publishData(
            @Part List<MultipartBody.Part> files,
            @PartMap Map<String, RequestBody> fields
    );
}
