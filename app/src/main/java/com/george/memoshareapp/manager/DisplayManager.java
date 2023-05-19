package com.george.memoshareapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.activities.ReleaseActivity;
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
    private SharedPreferences sp;
    private int offset = 0;
    private final int limit = 10;
    Context Context;
    List<Post> treePostList = new ArrayList<>();

    public DisplayManager(Context context) {
        this.Context = context;
        sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
    }


    public List<Post> getPostList() {
        List<Post> postList = LitePal.where("ispublic = ?", "1")
                .limit(limit)
                .offset(offset)
                .order("id desc")
                .find(Post.class, true);
        offset += limit;
        return postList;
    }

    public List<Post> showMemoryTree(double latitude, double longitude) {
        treePostList.clear();
        LatLng latLng1 = new LatLng(latitude, longitude);
        String phoneNumber = sp.getString("phoneNumber", "");
        List<Post> postList = LitePal
                .select("photocachepath", "longitude", "latitude", "contacts")
                .where("phonenumber !=? and ispublic = ?", phoneNumber,"1")
                .find(Post.class, true);
        if (postList != null) {
            for (Post post : postList) {
                if (!Double.isNaN(post.getLatitude()) && !Double.isNaN(post.getLongitude())) {
                    LatLng latLng2 = new LatLng(post.getLatitude(), post.getLongitude());
                    float distance = AMapUtils.calculateLineDistance(latLng1, latLng2);
                    if (distance < 2000) {
                        treePostList.add(post);
                    }
                }
            }
        }
        return treePostList;
    }


}
