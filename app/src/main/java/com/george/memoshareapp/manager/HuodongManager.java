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
import com.george.memoshareapp.beans.OutterActivityBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.http.api.HuodongServiceApi;
import com.george.memoshareapp.http.api.PostServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.OutHuodongDataListener;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.runnable.SavePhotoRunnable;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

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

    public void getHuoDongListByPage(int pageNum,int pageSize,int count, OutHuodongDataListener<List<OutterActivityBean>> listener){
        kv = MMKV.defaultMMKV();
        HuodongServiceApi huodongServiceApi = RetrofitManager.getInstance().create(HuodongServiceApi.class);
        Log.d(TAG, "getHuoDongListByPage: 执行到这里");
        Call<HttpListData<OutterActivityBean>> call = huodongServiceApi.getActivitys(pageNum, pageSize);
        call.enqueue(new Callback<HttpListData<OutterActivityBean>>() {
            @Override
            public void onResponse(Call<HttpListData<OutterActivityBean>> call, Response<HttpListData<OutterActivityBean>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: 执行到success中的Success");
                    HttpListData<OutterActivityBean> outterActivityListData = response.body();
                    Log.d(TAG, "onResponse: isLastPage: "+String.valueOf(outterActivityListData.isLastPage()));
                    List<OutterActivityBean> activityBeans = outterActivityListData.getItems();
                    for (OutterActivityBean outterActivityBean:activityBeans){//每次只返回分页的数据，还是返回分页以及以前的数据
                        String key = "activity_position_"+outterActivityBean.getActivity_id();
                        int index = activityBeans.indexOf(outterActivityBean);
                        if(count > 0){
                            index += count;
                            kv.encode(key, index);
//                            int nowIndex = activityBeans.indexOf(outterActivityBean);
//                            String photoCachePath = outterActivityBean.getFirstImagePath();
//                            String avatarCachePath = outterActivityBean.getHeadPortraitPath();
//                            String photoDetailPath = AppProperties.SERVER_MEDIA_URL + photoCachePath;
//                            String avatarDetailPath = AppProperties.SERVER_MEDIA_URL + avatarCachePath;
//                            activityBeans.get(nowIndex).setFirstImagePath(photoDetailPath);
//                            activityBeans.get(nowIndex).setHeadPortraitPath(avatarDetailPath);
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
            public void onFailure(Call<HttpListData<OutterActivityBean>> call, Throwable t) {
                Log.d(TAG, "onResponse: 执行到error中的Error");
                listener.onLoadError(t.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadHuodong(Context context, List<Uri> activityImagePaths, String phoneNumber, String activityPublishContent, String location, double longitude, double latitude, Date publishedDate , boolean isFollowing , int followId){
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
                } else {
                    Toasty.info(context, "活动发布失败，请重试", Toast.LENGTH_SHORT, true).show();
//                    Logger.d("Upload upload fail");
                }
                ((AddHuoDongActivity)context).finish();
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

}
