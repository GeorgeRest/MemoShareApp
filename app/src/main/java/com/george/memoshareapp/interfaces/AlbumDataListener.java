package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.http.response.HttpListData;

public interface AlbumDataListener<T> {
    void onSuccess(HttpListData<Album> data);
    void onError(String errorMessage);
}
