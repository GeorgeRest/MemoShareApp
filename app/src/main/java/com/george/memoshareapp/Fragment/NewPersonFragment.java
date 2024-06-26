package com.george.memoshareapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.AlbumAdapter;
import com.george.memoshareapp.adapters.LikeAdapter;
import com.george.memoshareapp.adapters.UserPublishRecyclerAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.Album;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.AlbumDataListener;
import com.george.memoshareapp.interfaces.LikePostDataListener;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.manager.AlbumManager;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.SpacesItemDecoration;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewPersonFragment extends Fragment implements PostDataListener<List<Post>>, LikePostDataListener {
    //内部 选 点赞和发布

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private RecyclerView LikeRecycleView;
    private LatLng currentLatLng;
    private List<ImageParameters> parameters;
    private SmartRefreshLayout likeSmartRefreshLayout;
    private List<Post> likePost;
    private LikeAdapter likeAdapter;
    private int offset = 0;
    private StaggeredGridLayoutManager layoutManager;
    private QuickAdapterHelper helper;
    private RecyclerView test_publish_recycler;
    int count=0;
    private List<Post> postList;
    private UserPublishRecyclerAdapter userPublishRecyclerAdapter;
    private StateLayout state;
    private DisplayManager displayManager;
    private SmartRefreshLayout publishSmartRefreshLayout;
    private int pageNum = 1;
    private int pageSize = 10;
    private SharedPreferences sp;
    private String phoneNumber;
    private List<Post> posts;
    private UserPublishRecyclerAdapter adapter;

    public NewPersonFragment() {
    }

    public static NewPersonFragment newInstance(String param1, String phoneNumber) {
        NewPersonFragment fragment = new NewPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString("phoneNumber", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            if (getArguments().getString("phoneNumber")!= null) {
                phoneNumber = getArguments().getString("phoneNumber");
            }else{
                phoneNumber = sp.getString("phoneNumber", "");
            }
        }
        Bundle args = getArguments();
        Logger.d(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        switch (mParam1) {
            case "发布":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_release, container, false);
                state = (StateLayout) rootView.findViewById(R.id.state);
                state.setEmptyLayout(R.layout.layout_empty);
                state.setErrorLayout(R.layout.layout_error);
                state.setLoadingLayout(R.layout.layout_loading);
                state.showLoading(null, false, false);
                displayManager = new DisplayManager();
                test_publish_recycler = (RecyclerView) rootView.findViewById(R.id.test_publish_recycler);
                displayManager.getPostsByPhoneNumber(pageNum, pageSize, phoneNumber, this, "发布");
                publishSmartRefreshLayout = rootView.findViewById(R.id.publish_refreshLayout);
                publishSmartRefreshLayout.setEnableAutoLoadMore(true);
                publishSmartRefreshLayout.setFooterHeight(80);

                publishSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        pageNum = 1;
                        displayManager.getPostsByPhoneNumber(pageNum, pageSize, phoneNumber, new PostDataListener<List<Post>>() {
                            @Override
                            public void onSuccess(HttpListData<Post> newPostData, String type) {
                                posts.clear(); // 清除原有数据
                                List<Post> newPosts = newPostData.getItems();
                                // 将新数据添加到列表中
                                posts.addAll(newPosts);
                                // 通知 adapter 更新列表
                                userPublishRecyclerAdapter.notifyDataSetChanged(); // 通知整个列表数据变化，重绘视图
                                refreshLayout.finishRefresh(); //结束刷新

                                if (newPostData.isLastPage()) {
                                    refreshLayout.setNoMoreData(true);
                                } else {
                                    pageNum++;
                                    refreshLayout.setNoMoreData(false); // 允许加载更多数据
                                }
                            }
                            @Override
                            public void onError(String error) {
                                refreshLayout.finishRefresh(false); // 结束刷新，但没有收到数据
                            }
                        }, "发布");
                    }
                });
                break;
            case "点赞":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_dainzan, container, false);
                LikeRecycleView = (RecyclerView) rootView.findViewById(R.id.recycleViewStagged);
                likeSmartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshLayout);
                likeSmartRefreshLayout.setEnableAutoLoadMore(true);

                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                LikeRecycleView.setLayoutManager(layoutManager);
                displayManager = new DisplayManager(getActivity());
                displayManager.getLikePostsByPhoneNumber(pageNum, pageSize, phoneNumber, this, "点赞");
                currentLatLng = ((MyApplication) getActivity().getApplication()).getCurrentLatLng();
                ((DefaultItemAnimator) LikeRecycleView.getItemAnimator()).setSupportsChangeAnimations(false);
                ((SimpleItemAnimator) LikeRecycleView.getItemAnimator()).setSupportsChangeAnimations(false);
                LikeRecycleView.getItemAnimator().setChangeDuration(0);
                LikeRecycleView.setHasFixedSize(true);
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                LikeRecycleView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
                likeAdapter = new LikeAdapter(getActivity(), currentLatLng);
                LikeRecycleView.setAdapter(likeAdapter); // 将适配器设置到RecyclerView

                likeSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        pageNum = 1;
                        displayManager.getLikePostsByPhoneNumber(pageNum, pageSize, phoneNumber, new LikePostDataListener<List<Post>>() {
                            @Override
                            public void onPostLikeSuccess(HttpListData<Post> data) {
                                posts.clear();
                                List<Post> newPosts = data.getItems();
                                posts.addAll(newPosts);
                                likeAdapter.notifyDataSetChanged();
                                likeSmartRefreshLayout.finishRefresh();
                                if (data.isLastPage()) {
                                    likeSmartRefreshLayout.setNoMoreData(true);
                                } else {
                                    pageNum++;
                                    likeSmartRefreshLayout.setNoMoreData(false);
                                }
                            }
                            @Override
                            public void onPostLikeSuccessError(String errorMessage) {
                                refreshlayout.finishRefresh(false);
                            }
                        }, "点赞");
                    }
                });
                break;
            case "相册":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_album, container, false);
                RecyclerView albumsRecyclerView = (RecyclerView) rootView.findViewById(R.id.albums_recycler_view);
                SmartRefreshLayout albumsSmartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshLayout);
                ImageView pleaseCreatedAlbum = rootView.findViewById(R.id.pleaseCreatedAlbum);
                TextView createdalbumText = rootView.findViewById(R.id.createdalbumText);
                pleaseCreatedAlbum.setVisibility(View.INVISIBLE);
                createdalbumText.setVisibility(View.INVISIBLE);
                int numberOfColumns = 2;
                albumsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                AlbumManager albumManager = new AlbumManager(getContext());
                AlbumAdapter albumAdapter = new AlbumAdapter(getContext());
                albumsRecyclerView.setAdapter(albumAdapter);
                albumsRecyclerView.setHasFixedSize(true);
                ((SimpleItemAnimator) albumsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

                albumsRecyclerView.getItemAnimator().setChangeDuration(0);

                if (count==0){
                    albumManager.getAlbumsByPhoneNumber(phoneNumber,createdalbumText,pleaseCreatedAlbum,albumsRecyclerView,getContext());
                    count++;
                }
                albumsSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                        albumManager.getAlbumsByPhoneNumber(phoneNumber,createdalbumText,pleaseCreatedAlbum,albumsRecyclerView,getContext(),new AlbumDataListener<List<Album>>(){
                            @Override
                            public void onSuccess(HttpListData<Album> data) {
                                albumsSmartRefreshLayout.finishRefresh(); //结束刷新
                            }
                            @Override
                            public void onError(String errorMessage) {
                                albumsSmartRefreshLayout.finishRefresh(); //结束刷新
                            }
                        },"相册");
                    }
                });

                break;

            default:
                break;
        }
        return rootView;
    }

    private void refreshData(HttpListData<Post> newPostData, @NonNull RefreshLayout refreshLayout) {
    }


    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSuccess(HttpListData<Post> postData, String type) {
        state.showContent(null);
        posts = postData.getItems();
        if (posts == null) {
            state.showEmpty(null);
        } else {
            userPublishRecyclerAdapter = new UserPublishRecyclerAdapter(getActivity(), posts);
            test_publish_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            test_publish_recycler.setAdapter(userPublishRecyclerAdapter);
        }
        if (postData.isLastPage()) {
            publishSmartRefreshLayout.setNoMoreData(true);
        } else {
            pageNum++;
            publishSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    displayManager.getPostsByPhoneNumber(pageNum, pageSize, phoneNumber, new PostDataListener<List<Post>>() {
                        @Override
                        public void onSuccess(HttpListData<Post> newPostData, String type) {
                            pageNum++;
                            List<Post> newPosts = newPostData.getItems();
                            posts.addAll(newPosts);
                            int startInsertPosition = posts.size();
                            userPublishRecyclerAdapter.notifyItemRangeInserted(startInsertPosition, newPosts.size());
                            refreshlayout.finishLoadMore();

                            if (newPostData.isLastPage()) {
                                refreshlayout.setNoMoreData(true);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            refreshlayout.finishLoadMore(false);
                        }
                    }, "发布");
                }
            });

        }
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    public void onPostLikeSuccess(HttpListData data) {
        posts = data.getItems();
        if (posts == null) {
//            state.showEmpty(null);
        } else {
            likeAdapter.submitList(posts);
        }
        if (data.isLastPage()) {
            likeSmartRefreshLayout.setNoMoreData(true);
        } else {
            pageNum++;
            likeSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    displayManager.getLikePostsByPhoneNumber(pageNum, pageSize, phoneNumber, new LikePostDataListener<List<Post>>() {
                        @Override
                        public void onPostLikeSuccess(HttpListData<Post> data) {
                            pageNum++;
                            List<Post> newPosts = data.getItems();
                            posts.addAll(newPosts);
                            int startInsertPosition = posts.size();
                            likeAdapter.notifyItemRangeInserted(startInsertPosition, newPosts.size());
                            refreshlayout.finishLoadMore();

                            if (data.isLastPage()) {
                                refreshlayout.setNoMoreData(true);
                            }
                        }

                        @Override
                        public void onPostLikeSuccessError(String errorMessage) {
                            refreshlayout.finishLoadMore(false);
                        }

                    }, "点赞");
                }
            });

        }

    }

    @Override
    public void onPostLikeSuccessError(String errorMessage) {


    }
}