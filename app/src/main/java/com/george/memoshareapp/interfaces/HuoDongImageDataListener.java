package com.george.memoshareapp.interfaces;

import java.util.List;

public interface HuoDongImageDataListener {
    void onImageLoadSuccess(List<String> imageNames);

    void onImageLoadFailed();
}
