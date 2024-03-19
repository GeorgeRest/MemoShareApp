package com.george.memoshareapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.george.memoshareapp.activities.AddHuoDongActivity;
import com.george.memoshareapp.beans.Danmu;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.events.HuoDongReleaseEvent;
import com.george.memoshareapp.http.api.HuodongServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.DanmuUploadListener;
import com.george.memoshareapp.interfaces.HuoDongImageDataListener;
import com.george.memoshareapp.interfaces.HuodongDataListener;
import com.george.memoshareapp.interfaces.HuodongDelListener;
import com.george.memoshareapp.interfaces.HuodongLikeListener;
import com.george.memoshareapp.runnable.SavePhotoRunnable;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HuodongManager {
    private Context context;
    private final String phoneNumber;
    private final static String TAG = "HuodongManager";
    private MMKV kv;

    public HuodongManager(Context context) {
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");
    }


    public void getHuoDongListByPage(int pageNum, int pageSize, int count, HuodongDataListener<List<InnerActivityBean>> listener){
        kv = MMKV.defaultMMKV();
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Log.d(TAG, "getHuoDongListByPage: 执行到这里");
        Call<HttpListData<InnerActivityBean>> call = huodongServiceApi.getActivitys(pageNum, pageSize);
        call.enqueue(new Callback<HttpListData<InnerActivityBean>>() {
            @Override
            public void onResponse(Call<HttpListData<InnerActivityBean>> call, Response<HttpListData<InnerActivityBean>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: 执行到success中的Success");
                    HttpListData<InnerActivityBean> outterActivityListData = response.body();
                    Log.d(TAG, "onResponse: isLastPage: "+String.valueOf(outterActivityListData.isLastPage()));
                    List<InnerActivityBean> activityBeans = outterActivityListData.getItems();
                    for (InnerActivityBean outterActivityBean:activityBeans){//每次只返回分页的数据，还是返回分页以及以前的数据
                        String key = "activity_position_"+outterActivityBean.getActivityId();
                        int index = activityBeans.indexOf(outterActivityBean);
                        if(count > 0){
                            index += count;
                            kv.encode(key, index);
                        }
                    }
                    outterActivityListData.setItems(activityBeans);
                    listener.onLoadSuccess(outterActivityListData,"活动");
                }else {
                    Log.d(TAG, "onResponse: 执行到success中的Error");
                    listener.onLoadError("Huodong Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<InnerActivityBean>> call, Throwable t) {
                Log.d(TAG, "onResponse: 执行到error中的Error");
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void uploadDanmu(int activityId, String content, String userId, DanmuUploadListener listener){
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);

        Map<String, RequestBody> fields = new HashMap<>();
        fields.put("userId", toRequestBody(userId));
        fields.put("content", toRequestBody(content));
        fields.put("activityId", toRequestBody(String.valueOf(activityId)));

        Call<ResponseBody> call = huodongServiceApi.uploadDanmu(fields);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(listener != null){
                        listener.danmuUploadSuccess(content);
                    }
                }else {
                    if(listener != null){
                        listener.danmuUploadFail("fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void getPersonalHuoDongListByPage(String phoneNumber,int pageNum,int pageSize,int count, HuodongDataListener<List<InnerActivityBean>> listener){
        kv = MMKV.defaultMMKV();
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Log.d(TAG, "getHuoDongListByPage: 执行到这里");
        Call<HttpListData<InnerActivityBean>> call = huodongServiceApi.getPersonalActivitys(phoneNumber,pageNum, pageSize);
        call.enqueue(new Callback<HttpListData<InnerActivityBean>>() {
            @Override
            public void onResponse(Call<HttpListData<InnerActivityBean>> call, Response<HttpListData<InnerActivityBean>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: 执行到success中的Success");
                    HttpListData<InnerActivityBean> outterActivityListData = response.body();
                    Log.d(TAG, "onResponse: isLastPage: "+String.valueOf(outterActivityListData.isLastPage()));
                    List<InnerActivityBean> activityBeans = outterActivityListData.getItems();
                    for (InnerActivityBean outterActivityBean:activityBeans){//每次只返回分页的数据，还是返回分页以及以前的数据
                        String key = "activity_position_"+outterActivityBean.getActivityId();
                        int index = activityBeans.indexOf(outterActivityBean);
                        if(count > 0){
                            index += count;
                            kv.encode(key, index);
                        }
                    }
                    outterActivityListData.setItems(activityBeans);
                    listener.onLoadSuccess(outterActivityListData,"活动");
                }else {
                    Log.d(TAG, "onResponse: 执行到success中的Error");
                    listener.onLoadError("Huodong Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<InnerActivityBean>> call, Throwable t) {
                Log.d(TAG, "onResponse: 执行到error中的Error");
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void getInnerHuoDongListByPage(int followId,int pageNum,int pageSize,int count, HuodongDataListener<List<InnerActivityBean>> listener){
        kv = MMKV.defaultMMKV();
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Log.d(TAG, "getHuoDongListByPage: 执行到这里");
        Call<HttpListData<InnerActivityBean>> call = huodongServiceApi.getInnerActivitys(followId,pageNum, pageSize);
        call.enqueue(new Callback<HttpListData<InnerActivityBean>>() {
            @Override
            public void onResponse(Call<HttpListData<InnerActivityBean>> call, Response<HttpListData<InnerActivityBean>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: 执行到success中的Success");
                    HttpListData<InnerActivityBean> outterActivityListData = response.body();
                    Log.d(TAG, "onResponse: isLastPage: "+String.valueOf(outterActivityListData.isLastPage()));
                    List<InnerActivityBean> activityBeans = outterActivityListData.getItems();
                    for (InnerActivityBean outterActivityBean:activityBeans){//每次只返回分页的数据，还是返回分页以及以前的数据
                        String key = "activity_position_"+outterActivityBean.getActivityId();
                        int index = activityBeans.indexOf(outterActivityBean);
                        if(count > 0){
                            index += count;
                            kv.encode(key, index);
                        }
                    }
                    outterActivityListData.setItems(activityBeans);
                    listener.onLoadSuccess(outterActivityListData,"活动");
                }else {
                    Log.d(TAG, "onResponse: 执行到success中的Error");
                    listener.onLoadError("Huodong Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<InnerActivityBean>> call, Throwable t) {
                Log.d(TAG, "onResponse: 执行到error中的Error");
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void getImagesByActivityId(int activityId, HuoDongImageDataListener listener) {
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Call<List<String>> call = huodongServiceApi.getImagesById(activityId);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    listener.onImageLoadSuccess(response.body());
                    Log.d(TAG, "onResponse: ImageName: " + response.body().toString());
                } else {
                    listener.onImageLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, "onResponse: 执行到error中的Error");
                listener.onImageLoadFailed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadHuodong(Context context, List<Uri> activityImagePaths, String phoneNumber, String activityPublishContent, String location, double longitude, double latitude, Date publishedDate , boolean isFollowing , int followId,String tag){
        Uri uri;
        String uriStringPath;
        File file;
        String photoAbsolutePath;
        SavePhotoRunnable savePhotoRunnable;
        List<String> photoPathList = new ArrayList<>();
        List<File> MediaFileList = new ArrayList<>();
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();


        ExecutorService executor = Executors.newFixedThreadPool(5);
        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
            photoFolder.mkdirs(); // 创建文件夹
        }
        for (int i = 0; i < activityImagePaths.size(); i++) {
            uri = activityImagePaths.get(i);
            uriStringPath = Base64.getUrlEncoder().encodeToString(uri.toString().getBytes());
            file = new File(photoFolder, uriStringPath);
            if (file.exists()) {
                photoAbsolutePath = file.getAbsolutePath();
                photoPathList.add(photoAbsolutePath);
            } else {
                // 将照片路径写入文件
                FileWriter writer = null;
                try {
                    writer = new FileWriter(uriStringPath, true);
                    writer.write(file.getAbsolutePath() + "\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                photoAbsolutePath = file.getAbsolutePath();
                photoPathList.add(photoAbsolutePath);
            }
            Log.d(TAG, "uploadHuodong: photoAbsolutePath = " + photoAbsolutePath);


            savePhotoRunnable = new SavePhotoRunnable(uri, file, photoFolder, activityImagePaths, context);
            executor.execute(savePhotoRunnable);
        }
        MediaFileList.clear();


        for (Uri imageUri : activityImagePaths) {
            String realPath = getRealPathFromUri(context, imageUri);
            File realFile = new File(realPath);
            MediaFileList.add(realFile);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateTimeString = dateFormat.format(publishedDate);
        Log.d(TAG, "uploadHuodong: dateTimeString = "+dateTimeString);
        Map<String, RequestBody> fields = new HashMap<>();
        fields.put("phoneNumber", toRequestBody(phoneNumber));
        fields.put("publishedText", toRequestBody(activityPublishContent));
        fields.put("location", toRequestBody(location == null ? "非洲阿拉巴马州" : location));
        fields.put("longitude", toRequestBody(String.valueOf(longitude)));
        fields.put("latitude", toRequestBody(String.valueOf(latitude)));
        fields.put("publishedTime", toRequestBody(dateTimeString));
        fields.put("isFollowing", toRequestBody(String.valueOf(isFollowing)));
        fields.put("followId", toRequestBody(String.valueOf(followId)));
        fields.put("tag", toRequestBody(tag));
        List<MultipartBody.Part> files = new ArrayList<>();
        for (File mediaFile : MediaFileList) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mediaFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("activityImageFiles", mediaFile.getName(), requestFile);
            files.add(body);
        }
        Log.d(TAG, "uploadHuodong: fields = "+fields.toString());
        Log.d(TAG, "uploadHuodong: files = "+files.size() + "\n" + files.toString());
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Call<ResponseBody> call = huodongServiceApi.publishActivity(files,fields);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                if (response.isSuccessful()) {

                    Toasty.info(context, "活动发布成功", Toast.LENGTH_SHORT, true).show();
                    Log.d("zxrelease", "onResponse: EventBus1");
                    EventBus.getDefault().post(new HuoDongReleaseEvent(isFollowing));
                    Log.d("zxrelease", "onResponse: EventBus2");
                    ((AddHuoDongActivity)context).finish();
                } else {

                    Toasty.info(context, "活动发布失败，请重试", Toast.LENGTH_SHORT, true).show();
//                    Logger.d("Upload upload fail");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.d("Upload upload fail" + t.getMessage());
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                Toasty.info(context, "活动发布失败，请重试", Toast.LENGTH_SHORT, true).show();
                ((AddHuoDongActivity)context).finish();
            }
        });
        executor.shutdown();
    }



    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public void delPersonalHuoDongs(List<Integer> delList , HuodongDelListener huodongDelListener) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Call<ResponseBody> call = huodongServiceApi.deletePersonalHuoDong(delList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    Toasty.info(context, "活动删除成功", Toast.LENGTH_SHORT, true).show();
                    huodongDelListener.onDeleteResult(true);
                } else {
                    Toasty.info(context, "活动删除失败，请重试", Toast.LENGTH_SHORT, true).show();
                    huodongDelListener.onDeleteResult(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                Toasty.info(context, "发生错误，请重试：" + t.getMessage(), Toast.LENGTH_SHORT, true).show();
                huodongDelListener.onDeleteResult(false);
            }
        });

    }

    public void getDanmuList(int activityId,DanmuLoadListener danmuLoadListener) {

        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Call<List<Danmu>> call = huodongServiceApi.getDanmuList(activityId);
        call.enqueue(new Callback<List<Danmu>>() {
            @Override
            public void onResponse(Call<List<Danmu>> call, Response<List<Danmu>> response) {
                if(response.isSuccessful()){
                    List<Danmu> danmuList = response.body();
                    if(danmuLoadListener != null){
                        for(Danmu danmu : danmuList){
                            Log.d("zxtextdanmu1", "onResponse: danmu : "+danmu.toString());
                        }
                        danmuLoadListener.onDanmuListLoadSuccess(danmuList);
                    }
                }else {
                    if(danmuLoadListener != null){
                        danmuLoadListener.onDanmuListLoadFail("fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Danmu>> call, Throwable t) {
                if(danmuLoadListener != null){
                    danmuLoadListener.onDanmuListLoadFail(t.getMessage());
                }
            }
        });

    }

    public void getLikeByActivityId(int activityId,String phoneNumber, HuodongLikeListener huodongLikeListener) {
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        huodongServiceApi.getLikeByActivityId(activityId,phoneNumber).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(huodongLikeListener != null){
                    if(response.isSuccessful()){
                        huodongLikeListener.onLikeSuccess(response.body());
                    }else {
                        huodongLikeListener.onLikeFail("fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (huodongLikeListener != null){
                    huodongLikeListener.onLikeFail(t.getMessage());
                }
            }
        });

    }

    public void updateLikeState(int activityId,String phoneNumber, HuodongLikeListener huodongLikeListener) {
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        huodongServiceApi.updataLikeState(activityId,phoneNumber).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(huodongLikeListener != null){
                    if(response.isSuccessful()){
                        huodongLikeListener.onLikeSuccess(response.body());
                    }else {
                        huodongLikeListener.onLikeFail("fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (huodongLikeListener != null){
                    huodongLikeListener.onLikeFail(t.getMessage());
                }
            }
        });


    }
}
