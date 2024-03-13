package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AlbumApi {
    @POST("album/saveAlbum")
    Call<HttpData<Album>> saveAlbum(@Body Album album);
    @POST("album/savePhotoInAlbum")
    Call<HttpData<PhotoInAlbum>> savePhotoInAlbum(@Body List<PhotoInAlbum> photoInAlbumList);
    @GET("album/getAlbum/{phoneNumber}")
    Call<HttpListData<Album>> getAlbumByPhoneNumber(@Path("phoneNumber") String phoneNumber);
    @GET("album/getPhotoInAlbum/{phoneNumber}")
    Call<HttpListData<PhotoInAlbum>> getPhotoInAlbum(@Path("phoneNumber") String phoneNumber);
//    @GET("/getFirstPhotoPath/{createdAlbumTime}")
//    Call<HttpData<String>> getFirstPhotoPathByCreateTime(@Path("createdAlbumTime") String createdAlbumTime);
}
