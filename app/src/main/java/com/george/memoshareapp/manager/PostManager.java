package com.george.memoshareapp.manager;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.runnable.SavePhotoRunnable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostManager {
    private Context context;
    private List<Uri> imageUriList;
    private List<String> photoPathList;

    public PostManager(Context context) {
        this.context = context;
    }
    public void getDBParameter(List<Uri>imageUriList, String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime){
        this.imageUriList = imageUriList;
        saveContent2DB(phoneNumber, editTextContent, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);

    }
   public void saveContent2DB(String phoneNumber, String editTextContent, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime){
       ExecutorService executor = Executors.newFixedThreadPool(5);

       SavePhotoRunnable savePhotoRunnable = new SavePhotoRunnable(imageUriList, context);
       executor.execute(savePhotoRunnable);
       photoPathList = savePhotoRunnable.getPhotoPathList();
       Post post = new Post(  phoneNumber, editTextContent, photoPathList, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
       post.save();
       executor.shutdown();


   }
}
