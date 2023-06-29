package com.george.memoshareapp.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.runnable.SavePhotoRunnable;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaveContent2DB {
    private static String photoAbsolutePath1;
    private static final String TAG = "PostManager";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  static void saveContent2DB(List<ImageParameters> imageParametersList,List<String> photoPathList,List<Uri> imageUriList, Context context, String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        File photoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photoCache");
        if (!photoFolder.exists()) {// 文件夹不存在，执行创建文件夹的操作
            photoFolder.mkdirs(); // 创建文件夹
        }
        for (int i = 0; i < imageUriList.size(); i++) {
            Uri uri = imageUriList.get(i);
            String uriStringPath = Base64.getUrlEncoder().encodeToString(uri.toString().getBytes());
            File file = new File(photoFolder, uriStringPath);
            if (file.exists()) {
                String photoAbsolutePath = file.getAbsolutePath();
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

                photoAbsolutePath1 = file.getAbsolutePath();
                photoPathList.add(photoAbsolutePath1);
            }
            BitmapFactory.Options options = ImageUtil.getBitmapOptionsFromImageUri(context, uri);
            if (options != null) {
                int imageWidth = options.outWidth;
                int imageHeight = options.outHeight;
                ImageParameters imageParameters = new ImageParameters(photoAbsolutePath1, imageWidth, imageHeight);
                imageParametersList.add(imageParameters);
            }

            SavePhotoRunnable savePhotoRunnable = new SavePhotoRunnable(uri, file, photoFolder, imageUriList, context);
            executor.execute(savePhotoRunnable);
        }
        Log.d(TAG, "saveContent2DB: " + photoPathList);

        Post post = new Post(phoneNumber, editTextContent, photoPathList, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
        post.setImageParameters(imageParametersList);
        for (ImageParameters imageParameters : imageParametersList) {
            imageParameters.setPost(post);
            imageParameters.save();
        }
        if(record.size()!=0 ){
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
}
