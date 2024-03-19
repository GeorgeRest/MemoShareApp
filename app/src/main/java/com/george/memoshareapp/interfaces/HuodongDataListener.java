package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.http.response.HttpListData;

public interface HuodongDataListener<T> {
        void onLoadSuccess(HttpListData<InnerActivityBean> data, String type);
        void onLoadError(String errorMessage);
    }