package com.george.memoshareapp.http.api;

import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.http.response.HttpData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlbumApi {
    @POST("album/saveAlbum")
    Call<HttpData<Album>> saveAlbum(@Body Album album);
    @POST("album/savePhotoInAlbum")
    Call<HttpData<PhotoInAlbum>> savePhotoInAlbum(@Body List<PhotoInAlbum> photoInAlbumList);

}
