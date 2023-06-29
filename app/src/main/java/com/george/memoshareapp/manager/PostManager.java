package com.george.memoshareapp.manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;

import com.george.memoshareapp.activities.ReleaseActivity;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.http.api.PostApiService;
import com.george.memoshareapp.runnable.SavePhotoRunnable;
import com.george.memoshareapp.utils.ImageUtil;
import com.george.memoshareapp.utils.SaveContent2DB;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class PostManager {
    private static final String TAG = "PostManager";
    private Context context;
    private Uri uri;
    private List<Uri> imageUriList;
    private String uriStringPath;
    private String photoAbsolutePath;
    private File file;
    private SavePhotoRunnable savePhotoRunnable;
    private ImageParameters imageParameters;

    private List<String> photoPathList = new ArrayList<>();
    private List<ImageParameters> imageParametersList = new ArrayList<>();

    private LifecycleOwner lifecycleOwner;
    private List<File> recordFileList = new ArrayList<>();
    private List<File> MediaFileList = new ArrayList<>();
    public PostManager(Context context) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }
    public PostManager(Context context) {
        this.context = context;

    }
    public Post findUser(String id) {
        Post post = LitePal.where("id = ?", id).findFirst(Post.class);
        if (post != null) {
            return post;
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDBParameter(List<Uri> imageUriList, String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
        this.imageUriList = imageUriList;
        SaveContent2DB.saveContent2DB(imageParametersList,photoPathList,imageUriList,context,phoneNumber, editTextContent, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveContent2DB(String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
            photoFolder.mkdirs(); // 创建文件夹
        }
        for (int i = 0; i < imageUriList.size(); i++) {
            uri = imageUriList.get(i);
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
            BitmapFactory.Options options = ImageUtil.getBitmapOptionsFromImageUri(context, uri);
            if (options != null) {
                int imageWidth = options.outWidth;
                int imageHeight = options.outHeight;
                imageParameters = new ImageParameters(photoAbsolutePath, imageWidth, imageHeight);
                imageParametersList.add(imageParameters);
            }

            savePhotoRunnable = new SavePhotoRunnable(uri, file, photoFolder, imageUriList, context);
            executor.execute(savePhotoRunnable);
        }
        Log.d(TAG, "saveContent2DB: " + photoPathList);

        for (ImageParameters imageParameters:imageParametersList) {
            Log.d(TAG, "saveContent2DB: " + imageParameters);

        }
        recordFileList.clear();
        MediaFileList.clear();
        ////////////////////////////////////////////////
        for (Uri imageUri:imageUriList) {
            String realPath = getRealPathFromUri(context, imageUri);
            File file = new File(realPath);
            MediaFileList.add(file);
        }

        for (Recordings recordings : record) {
            File recordFile = new File(recordings.getRecordCachePath());
            recordFileList.add(recordFile);
        }


//===============================================

        Map<String, RequestBody> fields = new HashMap<>();
        List<MultipartBody.Part> files = new ArrayList<>();
        for (File file : MediaFileList) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFiles", file.getName(), requestFile);
            files.add(body);
        }
        for (File file : recordFileList) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("audioFiles", file.getName(), requestFile);
            files.add(body);
        }
        fields.put("phoneNumber", toRequestBody(phoneNumber));
        fields.put("publishedText", toRequestBody(editTextContent));
        fields.put("location", toRequestBody(location));
        fields.put("longitude", toRequestBody(String.valueOf(longitude)));
        fields.put("latitude", toRequestBody(String.valueOf(latitude)));
        fields.put("isPublic", toRequestBody(String.valueOf(PUBLIC_PERMISSION)));
        fields.put("publishedTime", toRequestBody(publishedTime));
        fields.put("memoryTime", toRequestBody(memoireTime));
        fields.put("like", toRequestBody(String.valueOf(0)));
        fields.put("shareCount", toRequestBody(String.valueOf(0)));
        fields.put("contacts", toRequestBody(new Gson().toJson(contacts)));
        fields.put("imageParameters", toRequestBody(new Gson().toJson(imageParametersList)));
        fields.put("recordings", toRequestBody(new Gson().toJson(record)));

        PostApiService postApiService = RetrofitManager.getInstance().create(PostApiService.class);
        Call<ResponseBody> call = postApiService.publishData(files, fields);
        call.enqueue(new Callback<ResponseBody>() {

            private ReleaseActivity releaseActivity;

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                releaseActivity = (ReleaseActivity) PostManager.this.context;
                if(response.isSuccessful()){
                    Logger.d("Upload upload success");
                    Toasty.info(context, "发布成功", Toast.LENGTH_SHORT, true).show();

                }else{
                    Logger.d("Upload upload fail");
                }
                releaseActivity.finish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.d("Upload upload fail"+t.getMessage());
                loadingDialog.endAnim();
                loadingDialog.dismiss();
                releaseActivity.finish();
            }
        });

        Post post = new Post(phoneNumber, editTextContent, photoPathList, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
        post.setImageParameters(imageParametersList);
        for (ImageParameters imageParameters : imageParametersList) {
            imageParameters.setPost(post);
            imageParameters.save();
        }
        if (record.size() != 0) {
            for (Recordings recordings : record) {
                recordings.setPost(post);
                recordings.save();
            }
        }
        post.save();
        post.setLike(0);
        post.update(post.getId());
        if (post.getIsPublic() != 0) {
            HomeWholeRecyclerViewAdapter adapter = HomeWholeRecyclerViewAdapter.getInstance();
            adapter.addData(post, imageUriList);
            EventBus.getDefault().post(new ScrollToTopEvent());
        }
        executor.shutdown();
    }

    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
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

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void saveContent2DB(String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
//        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
//            photoFolder.mkdirs(); // 创建文件夹
//        }
//        for (int i = 0; i < imageUriList.size(); i++) {
//            uri = imageUriList.get(i);
//            uriStringPath = Base64.getUrlEncoder().encodeToString(uri.toString().getBytes());
//            file = new File(photoFolder, uriStringPath);
//            if (file.exists()) {
//                photoAbsolutePath = file.getAbsolutePath();
//                photoPathList.add(photoAbsolutePath);
//            } else {
//                // 将照片路径写入文件
//                FileWriter writer = null;
//                try {
//                    writer = new FileWriter(uriStringPath, true);
//                    writer.write(file.getAbsolutePath() + "\n");
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                photoAbsolutePath = file.getAbsolutePath();
//                photoPathList.add(photoAbsolutePath);
//            }
//            BitmapFactory.Options options = ImageUtil.getBitmapOptionsFromImageUri(context, uri);
//            if (options != null) {
//                int imageWidth = options.outWidth;
//                int imageHeight = options.outHeight;
//                imageParameters = new ImageParameters(photoAbsolutePath, imageWidth, imageHeight);
//                imageParametersList.add(imageParameters);
//            }
//
//            savePhotoRunnable = new SavePhotoRunnable(uri, file, photoFolder, imageUriList, context);
//            executor.execute(savePhotoRunnable);
//        }
//        Log.d(TAG, "saveContent2DB: " + photoPathList);
//
//        Post post = new Post(phoneNumber, editTextContent, photoPathList, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
//        post.setImageParameters(imageParametersList);
//        for (ImageParameters imageParameters : imageParametersList) {
//            imageParameters.setPost(post);
//            imageParameters.save();
//        }
//    if(record.size()!=0 ){
//            for (Recordings recordings : record) {
//                recordings.setPost(post);
//                recordings.save();
//            }
//        }
//        post.save();
//        post.setLike(0);
//        post.update(post.getId());
//        if (post.getIsPublic() != 0) {
//            HomeWholeRecyclerViewAdapter adapter = HomeWholeRecyclerViewAdapter.getInstance();
//            adapter.addData(post, imageUriList);
//            EventBus.getDefault().post(new ScrollToTopEvent());
//        }
//        executor.shutdown();
//    }
}