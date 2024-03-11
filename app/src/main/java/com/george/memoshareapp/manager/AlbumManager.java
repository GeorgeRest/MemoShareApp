package com.george.memoshareapp.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.george.memoshareapp.activities.CreatedAlbumActivity;
import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.AlbumApi;
import com.george.memoshareapp.http.api.AlbumServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumManager {
    private List<File> MediaFileList = new ArrayList<>();
    private PhotoInAlbum photoInAlbum;
    private Context context;
    private List<PhotoInAlbum> photoInAlbumList;
    private static final String TAG = "AlbumManager";
    private Album album;
    private LoadingDialog loadingDialog;
    private CreatedAlbumActivity createdAlbumActivity;
    public AlbumManager(Context context) {
        this.context = context;
    }

    public void saveAlbumAndPhoto2DB(List<String> realPhotoPath,String phoneNumber, String albumName, String albumDescription, List<String> UriPhotoPathList, String firstPhotoUri){
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(currentTime);
        album = new Album(phoneNumber, albumName, albumDescription, formattedDate);
        album.save();
        long count = LitePal.count(Album.class);
        photoInAlbumList = new ArrayList<>();
        for (String uri:UriPhotoPathList) {//只有照片名
            if (uri.equals(firstPhotoUri)){
                photoInAlbum = new PhotoInAlbum(count, uri, 1);
            }else {
                photoInAlbum= new PhotoInAlbum(count, uri, 0);
            }
            photoInAlbum.save();
            photoInAlbumList.add(photoInAlbum);
        }
        createdAlbumActivity = (CreatedAlbumActivity) context;
        upload2file(realPhotoPath);//这个是照片地址全的，不只是名字，连前面的Stroy/。。。/XXXX.jpg
        saveAlbum2IDE(album);
    }
    public void upload2file( List<String> imageUriList) {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
        List<MultipartBody.Part> files = new ArrayList<>();
        for (String path:imageUriList) {
            File file = new File(path);
            MediaFileList.add(file);
        }
        for (File file : MediaFileList) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            files.add(body);
        }
        AlbumServiceApi albumServiceApi = RetrofitManager.getInstance().create(AlbumServiceApi.class);
        Call<ResponseBody> call = albumServiceApi.uploadFile(files);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Logger.d("Upload upload success");
                    System.out.println("77777777777777"+"到file了"+"----response:"+response);
                    Toasty.info(context, "到file成功", Toast.LENGTH_SHORT, true).show();
                }else{
                    System.out.println("77777777777777"+"没到file");
                    Logger.d("Upload upload fail");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.d("Upload upload fail"+t.getMessage());
            }
        });
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
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(context, "相册创建成功 O(∩_∩)O", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "服务器忙碌，请待会儿再上传把~", Toast.LENGTH_SHORT).show();
                }
                createdAlbumActivity.finish();
            }
            @Override
            public void onFailure(Call<HttpData<PhotoInAlbum>> call, Throwable t) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                createdAlbumActivity.finish();
                Log.d("isFollowing", "onResponse: " + t.getMessage());
            }
        });
    }
}
