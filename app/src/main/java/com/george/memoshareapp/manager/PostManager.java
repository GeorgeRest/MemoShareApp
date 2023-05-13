package com.george.memoshareapp.manager;

import android.content.Context;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;

import java.util.List;

public class PostManager {
    private Context context;

    public PostManager(Context context) {
        this.context = context;
    }
   public void saveContent2DB(String phoneNumber, String editTextContent, List<String> phoneCachePath, List<Recordings> record, List<String> contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime){
       int phoneNumber1 =  Integer.parseInt(phoneNumber);
       Post post = new Post(  phoneNumber1, editTextContent, phoneCachePath, record, contacts, location, longitude, latitude, PUBLIC_PERMISSION, publishedTime, memoireTime);
       post.save();

   }
}
