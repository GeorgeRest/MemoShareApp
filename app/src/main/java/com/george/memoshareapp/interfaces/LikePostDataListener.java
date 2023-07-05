package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.http.response.HttpListData;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.interfaces
 * @className: LikePostDataListener
 * @author: George
 * @description: TODO
 * @date: 2023/7/4 21:47
 * @version: 1.0
 */
public interface LikePostDataListener<T>{
    void onPostLikeSuccess(HttpListData<Post> data);
    void onPostLikeSuccessError(String errorMessage);
}
