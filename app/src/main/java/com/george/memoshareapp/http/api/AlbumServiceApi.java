package com.george.memoshareapp.http.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AlbumServiceApi {
    @Multipart
    @POST("/uploadFile")
//    Call<ResponseBody> uploadFile(@Part List<MultipartBody.Part> files,
//                                  @PartMap Map<String, RequestBody> fields);
    Call<ResponseBody> uploadFile( @Part List<MultipartBody.Part> file);
}
