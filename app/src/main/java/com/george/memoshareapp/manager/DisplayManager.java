package com.george.memoshareapp.manager;

import android.content.Context;

import com.george.memoshareapp.beans.Post;

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
    Context Context;

    public DisplayManager(Context context) {
        this.Context = context;
    }

    public List<Post> getPostList() {
        List<Post> postList = new ArrayList<>();
        postList = LitePal.where("ispublic = ?", "1")
                .limit(10)
                .offset(offset)
                .find(Post.class);
        offset += 10;
        if (postList.size() != 0) {
            return postList;
        } else {
            return null;
        }

    }
}
