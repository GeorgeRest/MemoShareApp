package com.george.memoshareapp.exception;

import androidx.annotation.NonNull;

import com.george.memoshareapp.http.response.HttpData;
import com.hjq.http.exception.HttpException;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/06/25
 *    desc   : 返回结果异常
 */
public final class ResultException extends HttpException {

    private final HttpData<?> mData;

    public ResultException(String message, HttpData<?> data) {
        super(message);
        mData = data;
    }

    public ResultException(String message, Throwable cause, HttpData<?> data) {
        super(message, cause);
        mData = data;
    }

    @NonNull
    public HttpData<?> getHttpData() {
        return mData;
    }
}