package com.george.memoshareapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.DetailPhotoRecycleViewAdapter;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.utils.CustomItemDecoration;

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
    private  SharedPreferences sp;
    private int offset = 0;
    private final int limit = 10;
    Context Context;
    private DetailPhotoRecycleViewAdapter detailPhotoRecycleViewAdapter;
    List<Post> treePostList = new ArrayList<>();

    public DisplayManager() {
    }
    public DisplayManager(Context context) {
        this.Context = context;
        sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
    }


    public void showPhoto(RecyclerView recyclerView,List<String> photoPath, Context context) {


        switch (photoPath.size()) {
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
                break;
            case 2:
            case 4:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                break;
            default:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                break;
        }
        recyclerView.setHasFixedSize(true);
        detailPhotoRecycleViewAdapter = new DetailPhotoRecycleViewAdapter(context,photoPath);
        recyclerView.setAdapter(detailPhotoRecycleViewAdapter);
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
        recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));

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

    /**
     * 获取评论列表数据
     */
    public List<CommentBean> getCommentData(Post post){
        ArrayList<CommentBean> commentList = new ArrayList<>();

        List<CommentBean> commentBeans = LitePal.where("post_id = ?", String.valueOf(post.getId()))
                .limit(limit)
                .offset(offset)
                .find(CommentBean.class);
        for (CommentBean commentBean:commentBeans) {
            List<ReplyBean> replyBeans = LitePal.where("commentbean_id = ?", String.valueOf(commentBean.getId())).find(ReplyBean.class);
            commentBean.setReplyList(replyBeans);
            commentList.add(commentBean);
        }
        offset += limit;
        return commentList;
    }



    public List<Post> showMemoryTree(double latitude, double longitude) {
        treePostList.clear();
        LatLng latLng1 = new LatLng(latitude, longitude);
        String phoneNumber = sp.getString("phoneNumber", "");
        List<Post> postList = LitePal
                .where("phonenumber !=? and ispublic = ?", phoneNumber,"1")
                .find(Post.class, true);
        if (postList != null) {
            for (Post post : postList) {
                if (post.getLatitude() != 0.0 && post.getLongitude() != 0.0) {
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


