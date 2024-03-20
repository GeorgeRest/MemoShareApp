package com.george.memoshareapp.interfaces;

public interface DanmuUploadListener {
    void danmuUploadSuccess(String content);
    void danmuUploadFail(String error);
}
