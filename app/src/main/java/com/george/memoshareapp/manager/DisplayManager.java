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
    private final int limit = 10;
    Context Context;

    public DisplayManager(Context context) {
        this.Context = context;
    }


    public List<Post> getPostList() {
        List<Post> postList = LitePal.where("ispublic = ?", "1")
                .limit(limit)
                .offset(offset)
                .find(Post.class,true);
        offset += limit;
        return postList;
    }

    public void resetOffset() {
        this.offset = 0;
    }
}
