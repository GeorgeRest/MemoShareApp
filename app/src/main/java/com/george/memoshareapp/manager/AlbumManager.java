package com.george.memoshareapp.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.http.api.AlbumApi;
import com.george.memoshareapp.http.response.HttpData;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumManager {

    private PhotoInAlbum photoInAlbum;
    private Context context;
    private List<PhotoInAlbum> photoInAlbumList;

    public AlbumManager(Context context) {
        this.context = context;
    }

    public void saveAlbumAndPhoto2DB(String phoneNumber, String albumName, String albumDescription, List<Uri> UriPhotoPathList, Uri firstPhotoUri){
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(currentTime);
        Album album = new Album(phoneNumber, albumName, albumDescription, formattedDate);
        album.save();
        long count = LitePal.count(Album.class);
        photoInAlbumList = new ArrayList<>();
        for (Uri uri:UriPhotoPathList) {
            if (uri.equals(firstPhotoUri)){
                photoInAlbum = new PhotoInAlbum(count, uri.toString(), 1);
            }else {
                photoInAlbum= new PhotoInAlbum(count, uri.toString(), 0);
            }
            photoInAlbum.save();
            photoInAlbumList.add(photoInAlbum);
        }
        saveAlbum2IDE(album);

    }
    public void saveAlbum2IDE(Album album){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpData<Album>> call = albumApi.saveAlbum(album);
        call.enqueue(new Callback<HttpData<Album>>() {
            @Override
            public void onResponse(Call<HttpData<Album>> call, Response<HttpData<Album>> response) {
                if (response.isSuccessful()){
                    saveAlbumPhoto2IDE(photoInAlbumList);
                }else {
                    Log.d("isFollowing", "onResponse: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<HttpData<Album>> call, Throwable t) {
                Log.d("isFollowing", "onResponse: " + t.getMessage());
            }
        });

    }
    public void saveAlbumPhoto2IDE(List<PhotoInAlbum> photoInAlbumList){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpData<PhotoInAlbum>> call = albumApi.savePhotoInAlbum(photoInAlbumList);
        call.enqueue(new Callback<HttpData<PhotoInAlbum>>() {
            @Override
            public void onResponse(Call<HttpData<PhotoInAlbum>> call, Response<HttpData<PhotoInAlbum>> response) {
            }
            @Override
            public void onFailure(Call<HttpData<PhotoInAlbum>> call, Throwable t) {
                Log.d("isFollowing", "onResponse: " + t.getMessage());
            }
        });

    }
}
