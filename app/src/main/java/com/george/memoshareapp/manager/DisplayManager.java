package com.george.memoshareapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.DetailPhotoRecycleViewAdapter;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.http.api.PostServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.CustomItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private DetailPhotoRecycleViewAdapter detailPhotoRecycleViewAdapter;
    List<Post> treePostList = new ArrayList<>();
    private String phoneNumber;

    public DisplayManager() {

    }

    public DisplayManager(Context context) {
        this.Context = context;
        sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
    }


    public void showPhoto(RecyclerView recyclerView, List<ImageParameters> imageParameters, Context context) {


        switch (imageParameters.size()) {
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

        detailPhotoRecycleViewAdapter = new DetailPhotoRecycleViewAdapter(context, imageParameters);
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


    public List<Post> getLikePost(int offset) {
        List<Post> LikePostList = new ArrayList<>();
        String phoneNumber = sp.getString("phoneNumber", "");
        User user = LitePal
                .where("phoneNumber = ?", phoneNumber)
                .findFirst(User.class, true);

        List<Post> allLikedPosts = user.getLikePosts();
        Collections.reverse(allLikedPosts);
        if (offset >= allLikedPosts.size()) {
            return LikePostList;
        }
        int toIndex = Math.min(offset + 10, allLikedPosts.size());

        List<Post> postList = allLikedPosts.subList(offset, toIndex);
        for (Post post : postList) {
            List<ImageParameters> imageParametersList = LitePal.where("post_id = ?", String.valueOf(post.getId())).find(ImageParameters.class);
            post.setImageParameters(imageParametersList);
            System.out.println(post.getImageParameters().size());
            LikePostList.add(post); // 把修改过的post添加到LikePostList
        }
        return LikePostList; // 返回LikePostList，而不是postList
    }





    public List<Post> showMemoryTree(double latitude, double longitude) {
        treePostList.clear();
        LatLng latLng1 = new LatLng(latitude, longitude);
        String phoneNumber = sp.getString("phoneNumber", "");
        List<Post> postList = LitePal
                .where("phonenumber !=? and ispublic = ?", phoneNumber, "1")
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

    public void getPostListByPage(int pageNum, int pageSize,  PostDataListener<List<Post>> listener) {

        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<HttpListData<Post>> call = postServiceApi.getPosts(pageNum, pageSize);
        call.enqueue(new Callback<HttpListData<Post>>() {
            @Override
            public void onResponse(Call<HttpListData<Post>> call, Response<HttpListData<Post>> response) {
                if (response.isSuccessful()) {
                    HttpListData<Post> postListData = response.body();
                    Log.d("lastPage", String.valueOf(postListData.isLastPage()));
                    List<Post> postList = postListData.getItems();
                    for (Post post : postList) {
                        List<ImageParameters> imageParametersList = post.getImageParameters();
                        List<Recordings> recordingsList = post.getRecordings();
                        for (ImageParameters imageParameters : imageParametersList) {
                            String photoCachePath = imageParameters.getPhotoCachePath();
                            String path = AppProperties.SERVER_MEDIA_URL + photoCachePath;
                            imageParameters.setPhotoCachePath(path);
                        }
                        for (Recordings Recordings:recordingsList) {
                            String recordCachePath = Recordings.getRecordCachePath();
                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
                            Recordings.setRecordCachePath(path);
                        }
                    }
                    postListData.setItems(postList);
                    listener.onSuccess(postListData);
                } else {
                    listener.onError("Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<Post>> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void updateLikeCount(int postId, String phoneNumber,boolean isLike){
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<Void> call = postServiceApi.updateLikeCount(phoneNumber,postId, isLike);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("updateLikeCount", "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("updateLikeCount", "onFailure: " + t.getMessage());
            }
        });

    }

}


