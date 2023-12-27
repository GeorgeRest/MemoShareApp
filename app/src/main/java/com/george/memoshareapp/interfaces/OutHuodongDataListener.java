package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.OutterActivityBean;
import com.george.memoshareapp.http.response.HttpListData;

public interface OutHuodongDataListener<T> {
        void onLoadSuccess(HttpListData<OutterActivityBean> data, String type);
        void onLoadError(String errorMessage);
    }