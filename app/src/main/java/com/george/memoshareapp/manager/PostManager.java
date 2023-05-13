package com.george.memoshareapp.manager;

import android.content.Context;
import android.net.Uri;

import com.george.memoshareapp.beans.Recordings;

import java.util.List;

public class PostManager {
    private Context context;

    public PostManager(Context context) {
        this.context = context;
    }
   public void saveContent2DB(String phoneNumber, String editTextContent, List<Uri> phoneCachePath, List<Recordings> record, String contacts, String location, double longitude, double latitude, int PUBLIC_PERMISSION, String publishedTime, String memoireTime){
//       Post post = new Post( phonenumber, ediTextContent, phoneCachePath, record, contacts, location, longitude, latitude, ispublic, publishedtime, memoireTime);
//       post.save();

   }
}
