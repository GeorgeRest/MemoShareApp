package com.george.memoshareapp.manager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.activities.CreatedAlbumActivity;
import com.george.memoshareapp.adapters.AlbumAdapter;
import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.PhotoInAlbum;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.AlbumApi;
import com.george.memoshareapp.http.api.AlbumServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.AlbumDataListener;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumManager {
    private int count=0;
    private List<File> MediaFileList = new ArrayList<>();
    private PhotoInAlbum photoInAlbum;
    private Context context;
    private List<PhotoInAlbum> photoInAlbumList;
    private static final String TAG = "AlbumManager";
    private Album album;
    private LoadingDialog loadingDialog;
    private CreatedAlbumActivity createdAlbumActivity;
    private List<Album> albumList=new ArrayList<>();

    private List<PhotoInAlbum> photoInAlbumList1=new ArrayList<>();
    private String userPhoneNumber;

    public AlbumManager(Context context) {
        this.context = context;
    }

    public void saveAlbumAndPhoto2DB(List<String> realPhotoPath,String phoneNumber, String albumName, String albumDescription, List<String> PhotoRealPathName, String firstPhotoName){
        SharedPreferences preferences = context.getSharedPreferences("User", MODE_PRIVATE);
        userPhoneNumber = preferences.getString("phoneNumber", "");
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(currentTime);
        album = new Album(phoneNumber, albumName, albumDescription, formattedDate);
        album.save();
        long count = LitePal.count(Album.class);
        photoInAlbumList = new ArrayList<>();
        for (String photoPathName:PhotoRealPathName) {//只有照片名
            if (photoPathName.equals(firstPhotoName)){
                photoInAlbum = new PhotoInAlbum(count, userPhoneNumber, photoPathName, 1);
            }else {
                photoInAlbum= new PhotoInAlbum(count, userPhoneNumber, photoPathName, 0);
            }
            photoInAlbum.save();
            photoInAlbumList.add(photoInAlbum);
        }
        createdAlbumActivity = (CreatedAlbumActivity) context;
        upload2file(realPhotoPath);//这个是照片地址全的，不只是名字，连前面的Stroy/。。。/XXXX.jpg
        saveAlbum2IDE(album,photoInAlbumList);
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
                }else{
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.d("Upload upload fail"+t.getMessage());
            }
        });
    }
    public void saveAlbum2IDE(Album album,List<PhotoInAlbum> photoInAlbumList1){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpData<Album>> call = albumApi.saveAlbum(album);
        call.enqueue(new Callback<HttpData<Album>>() {
            @Override
            public void onResponse(Call<HttpData<Album>> call, Response<HttpData<Album>> response) {
                if (response.isSuccessful()){
                    saveAlbumPhoto2IDE(photoInAlbumList1);
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
    public List<Album> getAlbumsByPhoneNumber(String phoneNumber, TextView createdalbumText, ImageView pleaseCreatedAlbum, RecyclerView albumsRecyclerView, Context context, AlbumDataListener<List<Album>> listener, String type){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpListData<Album>> call = albumApi.getAlbumByPhoneNumber(phoneNumber);
        call.enqueue(new Callback<HttpListData<Album>>() {
            @Override
            public void onResponse(Call<HttpListData<Album>> call, Response<HttpListData<Album>> response) {
                albumList = response.body().getItems();
                HttpListData<Album> ListAlbum = response.body();
                getPhotoInAlbum(listener,ListAlbum,albumList,createdalbumText,pleaseCreatedAlbum,phoneNumber,loadingDialog,albumsRecyclerView,context);
            }
            @Override
            public void onFailure(Call<HttpListData<Album>> call, Throwable t) {
                loadingDialog.dismiss();
                count++;
            }
        });
        return albumList;
    }
    public List<PhotoInAlbum> getPhotoInAlbum(AlbumDataListener listener,HttpListData<Album> ListAlbum,List<Album> albumList,TextView createdalbumText,ImageView pleaseCreatedAlbum,String phoneNumber,LoadingDialog loadingDialog, RecyclerView albumsRecyclerView,Context context){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpListData<PhotoInAlbum>> call = albumApi.getPhotoInAlbum(phoneNumber);
        call.enqueue(new Callback<HttpListData<PhotoInAlbum>>() {
            @Override
            public void onResponse(Call<HttpListData<PhotoInAlbum>> call, Response<HttpListData<PhotoInAlbum>> response) {
                photoInAlbumList1 = response.body().getItems();

                if(albumList.size()!=0){
                    AlbumAdapter albumAdapter = new AlbumAdapter(albumList,photoInAlbumList1,context);
                    albumsRecyclerView.setAdapter(albumAdapter);
                }else {
                    Toast.makeText(context,"albumList,photoList都为空",Toast.LENGTH_LONG);
                    pleaseCreatedAlbum.setVisibility(View.VISIBLE);
                    createdalbumText.setVisibility(View.VISIBLE);
                }
                listener.onSuccess(ListAlbum);

                count++;
            }
            @Override
            public void onFailure(Call<HttpListData<PhotoInAlbum>> call, Throwable t) {
                listener.onError("request Failed");
                count++;
            }
        });
        return photoInAlbumList1;

    }
    public List<Album> getAlbumsByPhoneNumber(String phoneNumber, TextView createdalbumText, ImageView pleaseCreatedAlbum, RecyclerView albumsRecyclerView, Context context){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpListData<Album>> call = albumApi.getAlbumByPhoneNumber(phoneNumber);
        call.enqueue(new Callback<HttpListData<Album>>() {
            @Override
            public void onResponse(Call<HttpListData<Album>> call, Response<HttpListData<Album>> response) {
                albumList = response.body().getItems();
                HttpListData<Album> ListAlbum = response.body();
                getPhotoInAlbum(ListAlbum,albumList,createdalbumText,pleaseCreatedAlbum,phoneNumber,loadingDialog,albumsRecyclerView,context);
            }
            @Override
            public void onFailure(Call<HttpListData<Album>> call, Throwable t) {
                System.out.println("从服务器获取AlbumList失败");
                loadingDialog.dismiss();
                count++;
                //listener.onError("request Failed");
            }
        });
        return albumList;
    }
    public List<PhotoInAlbum> getPhotoInAlbum(HttpListData<Album> ListAlbum,List<Album> albumList,TextView createdalbumText,ImageView pleaseCreatedAlbum,String phoneNumber,LoadingDialog loadingDialog, RecyclerView albumsRecyclerView,Context context){
        AlbumApi albumApi = RetrofitManager.getInstance().create(AlbumApi.class);
        Call<HttpListData<PhotoInAlbum>> call = albumApi.getPhotoInAlbum(phoneNumber);
        call.enqueue(new Callback<HttpListData<PhotoInAlbum>>() {
            @Override
            public void onResponse(Call<HttpListData<PhotoInAlbum>> call, Response<HttpListData<PhotoInAlbum>> response) {
                photoInAlbumList1 = response.body().getItems();
                if(albumList.size()!=0){
                    AlbumAdapter albumAdapter = new AlbumAdapter(albumList,photoInAlbumList1,context);
                    albumsRecyclerView.setAdapter(albumAdapter);
                }else {
                    Toast.makeText(context,"albumList,photoList都为空",Toast.LENGTH_LONG);
                    pleaseCreatedAlbum.setVisibility(View.VISIBLE);
                    createdalbumText.setVisibility(View.VISIBLE);
                }

                count++;
            }
            @Override
            public void onFailure(Call<HttpListData<PhotoInAlbum>> call, Throwable t) {

                count++;
            }
        });
        return photoInAlbumList1;

    }
    public int getCount(){
        return count;
    }

}
