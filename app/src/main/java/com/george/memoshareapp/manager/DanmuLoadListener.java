package com.george.memoshareapp.manager;

import com.george.memoshareapp.beans.Danmu;

import java.util.List;

public interface DanmuLoadListener {
    void onDanmuListLoadSuccess(List<Danmu> danmuList);
    void onDanmuListLoadFail(String error);
}
