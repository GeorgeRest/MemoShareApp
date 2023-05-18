package com.george.memoshareapp.manager;

import android.content.Context;

import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.utils.DateFormat;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.manager
 * @className: DisplayMangger
 * @author: George
 * @description: TODO
 * @date: 2023/5/15 15:10
 * @version: 1.0
 */
public class DisplayManager {
    private int offset = 0;
    private final int limit = 10;
    Context Context;

    public DisplayManager(Context context) {
        this.Context = context;
    }


    public List<Post> getPostList() {
        List<Post> postList = LitePal.where("ispublic = ?", "1")
                .limit(limit)
                .offset(offset)
                .order("id desc")
                .find(Post.class,true);
        offset += limit;
        //重复调用bug
//        if (postList == null) {
//            postList = new ArrayList<>();
//        }else{
//            for (Post post : postList) {
//                String messageDate = DateFormat.getMessageDate(post.getPublishedTime());
//                post.setPublishedTime(messageDate);
//            }
//        }
        return postList;
    }
    public List<Post> updatePostList(){
        return null;
    }


}
