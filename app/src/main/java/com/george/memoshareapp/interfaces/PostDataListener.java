package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.http.response.HttpListData;

public interface PostDataListener<T> {
        void onSuccess(HttpListData<Post> data,String type);
        void onError(String errorMessage);
    }