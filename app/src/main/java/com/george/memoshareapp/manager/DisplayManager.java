package com.george.memoshareapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.DetailPhotoRecycleViewAdapter;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.PostServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.LikePostDataListener;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.interfaces.getLikeCountListener;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.CustomItemDecoration;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

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
    private MMKV kv;

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

    public void getPostListByPage(int pageNum, int pageSize, int itemCount, String phoneNumber, PostDataListener<List<Post>> listener) {
        kv = MMKV.defaultMMKV();
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<HttpListData<Post>> call = postServiceApi.getPosts(pageNum, pageSize, phoneNumber);
        call.enqueue(new Callback<HttpListData<Post>>() {
            @Override
            public void onResponse(Call<HttpListData<Post>> call, Response<HttpListData<Post>> response) {
                if (response.isSuccessful()) {
                    HttpListData<Post> postListData = response.body();
                    Log.d("lastPage", String.valueOf(postListData.isLastPage()));
                    List<Post> postList = postListData.getItems();
                    for (Post post : postList) {
                        String key = "post_position_" + post.getId();
                        int index = postList.indexOf(post);
                        if (itemCount > 0) {
                            index += itemCount;
                        }
                        kv.encode(key, index);
                        List<ImageParameters> imageParametersList = post.getImageParameters();
                        List<Recordings> recordingsList = post.getRecordings();
                        for (ImageParameters imageParameters : imageParametersList) {
                            String photoCachePath = imageParameters.getPhotoCachePath();
                            String path = AppProperties.SERVER_MEDIA_URL + photoCachePath;
                            imageParameters.setPhotoCachePath(path);
                        }
                        for (Recordings Recordings : recordingsList) {
                            String recordCachePath = Recordings.getRecordCachePath();
                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
                            Recordings.setRecordCachePath(path);
                        }


                    }
                    postListData.setItems(postList);
                    listener.onSuccess(postListData,"推荐");
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
    public void getPostListByPageFriend(int pageNum, int pageSize, int itemCount, String phoneNumber, PostDataListener<List<Post>> listener) {
        kv = MMKV.defaultMMKV();
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<HttpListData<Post>> call = postServiceApi.getFriendPosts(pageNum, pageSize, phoneNumber);
        call.enqueue(new Callback<HttpListData<Post>>() {
            @Override
            public void onResponse(Call<HttpListData<Post>> call, Response<HttpListData<Post>> response) {
                if (response.isSuccessful()) {
                    HttpListData<Post> postListData = response.body();
                    Log.d("lastPage", String.valueOf(postListData.isLastPage()));
                    List<Post> postList = postListData.getItems();
                    for (Post post : postList) {
                        String key = "post_position_" + post.getId();
                        int index = postList.indexOf(post);
                        if (itemCount > 0) {
                            index += itemCount; // 计算正确的索引位置
                        }
                        kv.encode(key, index);
                        List<ImageParameters> imageParametersList = post.getImageParameters();
                        List<Recordings> recordingsList = post.getRecordings();
                        for (ImageParameters imageParameters : imageParametersList) {
                            String photoCachePath = imageParameters.getPhotoCachePath();
                            String path = AppProperties.SERVER_MEDIA_URL + photoCachePath;
                            imageParameters.setPhotoCachePath(path);
                        }
                        for (Recordings Recordings : recordingsList) {
                            String recordCachePath = Recordings.getRecordCachePath();
                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
                            Recordings.setRecordCachePath(path);
                        }


                    }
                    postListData.setItems(postList);
                    listener.onSuccess(postListData,"好友");
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

//    public void getPostsByPhoneNumber(int pageNum, int pageSize, String phoneNumber, PostDataListener<List<Post>> listener) {
//        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
//        Call<HttpListData<Post>> call = postServiceApi.getPostsByPhoneNumber(pageNum, pageSize, phoneNumber);
//        call.enqueue(new Callback<HttpListData<Post>>() {
//            @Override
//            public void onResponse(Call<HttpListData<Post>> call, Response<HttpListData<Post>> response) {
//                if (response.isSuccessful()) {
//
//                    HttpListData<Post> postListData = response.body();
//                    Log.d("lastPage", String.valueOf(postListData.isLastPage()));
//                    List<Post> postList = postListData.getItems();
//                    for (Post post : postList) {
//                        List<ImageParameters> imageParametersList = post.getImageParameters();
//                        List<Recordings> recordingsList = post.getRecordings();
//                        for (ImageParameters imageParameters : imageParametersList) {
//                            String photoCachePath = imageParameters.getPhotoCachePath();
//                            String path = AppProperties.SERVER_MEDIA_URL + photoCachePath;
//                            imageParameters.setPhotoCachePath(path);
//                        }
//                        for (Recordings Recordings : recordingsList) {
//                            String recordCachePath = Recordings.getRecordCachePath();
//                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
//                            Recordings.setRecordCachePath(path);
//                        }
//                    }
//                    postListData.setItems(postList);
//                    listener.onSuccess(postListData);
//                } else {
//                    listener.onError("Request failed");
//                }
//            }
//            @Override
//            public void onFailure(Call<HttpListData<Post>> call, Throwable t) {
//                Logger.d(t.getMessage());
//            }
//        });
//
//    }


    public void getPostsByPhoneNumber(int pageNum, int pageSize, String phoneNumber, PostDataListener<List<Post>> listener,String type) {
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<HttpListData<Post>> call = postServiceApi.getPostsByPhoneNumber(pageNum, pageSize, phoneNumber);
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
                        for (Recordings Recordings : recordingsList) {
                            String recordCachePath = Recordings.getRecordCachePath();
                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
                            Recordings.setRecordCachePath(path);
                        }
                    }
                    postListData.setItems(postList);
                    listener.onSuccess(postListData,type);
                } else {
                    listener.onError("Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<Post>> call, Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }
    public void getLikePostsByPhoneNumber(int pageNum, int pageSize, String phoneNumber, LikePostDataListener<List<Post>> listener, String type) {
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<HttpListData<Post>> call = postServiceApi.getLikePostsByPhoneNumber(pageNum, pageSize, phoneNumber);
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
                        for (Recordings Recordings : recordingsList) {
                            String recordCachePath = Recordings.getRecordCachePath();
                            String path = AppProperties.SERVER_RECORD_URL + recordCachePath;
                            Recordings.setRecordCachePath(path);
                        }
                    }
                    postListData.setItems(postList);
                    listener.onPostLikeSuccess(postListData);
                } else {
                    listener.onPostLikeSuccessError("Request failed");
                }
            }

            @Override
            public void onFailure(Call<HttpListData<Post>> call, Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    public void updateLikeCount(int postId, String phoneNumber, boolean isLike, getLikeCountListener updateLike) {
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<Integer> call = postServiceApi.updateLikeCount(phoneNumber, postId, isLike);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                updateLike.onSuccess(response.body());//点赞数量

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("updateLikeCount", "onFailure: " + t.getMessage());
            }
        });

    }

    public void getLikeCount(int postId, getLikeCountListener getLikeCountListener) {
        PostServiceApi postServiceApi = RetrofitManager.getInstance().create(PostServiceApi.class);
        Call<Integer> call = postServiceApi.getLikeCount(postId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                getLikeCountListener.onSuccess(response.body());//点赞数量
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("updateLikeCount", "onFailure: " + t.getMessage());
            }
        });

    }

}


