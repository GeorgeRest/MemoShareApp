package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.LikeAdapter;
import com.george.memoshareapp.adapters.UserPublishRecyclerAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class NewPersonFragment extends Fragment {
    //内部 选 点赞和发布

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private RecyclerView recycleViewStagged;
    private LatLng currentLatLng;
    private List<ImageParameters> parameters;
    private SmartRefreshLayout smartRefreshLayout;
    private List<Post> likePost;
    private LikeAdapter recyclerViewAdapter;
    private int offset = 0;
    private RecyclerView test_publish_recycler;
    private List<Post> postList;
    private UserPublishRecyclerAdapter userPublishRecyclerAdapter;

    public NewPersonFragment() {

    }

    public static NewPersonFragment newInstance(String param1) {
        NewPersonFragment fragment = new NewPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        switch (mParam1) {
            case "发布":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_release, container, false);
                test_publish_recycler = (RecyclerView) rootView.findViewById(R.id.test_publish_recycler);
                SmartRefreshLayout publishSmartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.publish_refreshLayout);
                test_publish_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                postList = new DisplayManager().getPostList();
                userPublishRecyclerAdapter = new UserPublishRecyclerAdapter(getActivity(), postList);
                test_publish_recycler.setAdapter(userPublishRecyclerAdapter);
                DisplayManager publishDisplayManager = new DisplayManager(getActivity());
                publishSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {

                        List<Post> newPosts = publishDisplayManager.getPostList();
                        if (newPosts.isEmpty() || newPosts.size() == 0) {
                            refreshlayout.setNoMoreData(true);
                        } else {
                            int initialSize = postList.size();
                            postList.addAll(newPosts);
                            userPublishRecyclerAdapter.notifyItemRangeInserted(initialSize, newPosts.size());
                            offset += 10;
                        }
                        refreshlayout.finishLoadMore();
                    }
                });

                publishSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        refreshlayout.setNoMoreData(false);
                        offset = 0;  // 重置offset
                        postList.clear();  // 清空likePost列表
                        postList.addAll(publishDisplayManager.getPostList());  // 重新获取数据
                        userPublishRecyclerAdapter.notifyDataSetChanged();  // 通知适配器数据集变化
                        offset += 10;  // 更新offset
                        refreshlayout.finishRefresh();
                    }
                });
                break;
            case "点赞":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_dainzan, container, false);
                recycleViewStagged = (RecyclerView) rootView.findViewById(R.id.recycleViewStagged);
                smartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshLayout);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                recycleViewStagged.setLayoutManager(layoutManager);
                likePost = new DisplayManager(getActivity()).getLikePost(offset);
                for (Post post:likePost) {
                    System.out.println("likePost" + post.getId());
                }

                currentLatLng = ((MyApplication) getActivity().getApplication()).getCurrentLatLng();
                recyclerViewAdapter = new LikeAdapter(getActivity(), likePost, currentLatLng);
                ((DefaultItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
                ((SimpleItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
                recycleViewStagged.getItemAnimator().setChangeDuration(0);
                recycleViewStagged.setHasFixedSize(true);
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                recycleViewStagged.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
                recycleViewStagged.setAdapter(recyclerViewAdapter);
                DisplayManager displayManager = new DisplayManager(getActivity());
                smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {

                        List<Post> newPosts = displayManager.getLikePost(offset);
                        if (newPosts.isEmpty() || newPosts.size() == 0) {
                            refreshlayout.setNoMoreData(true);
                        } else {
                            int initialSize = likePost.size();
                            likePost.addAll(newPosts);
                            recyclerViewAdapter.notifyItemRangeInserted(initialSize, newPosts.size());
                            offset += 10;
                        }
                        refreshlayout.finishLoadMore();
                    }
                });

                smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        refreshlayout.setNoMoreData(false);
                        offset = 0;  // 重置offset
                        likePost.clear();  // 清空likePost列表
                        likePost.addAll(displayManager.getLikePost(offset));  // 重新获取数据
                        recyclerViewAdapter.notifyDataSetChanged();  // 通知适配器数据集变化
                        offset += 10;  // 更新offset
                        refreshlayout.finishRefresh();
                    }
                });

                break;

            default:
                break;
        }
        return rootView;
    }

    private void refreshLikeData() {
        // 首先检查 likePost 是否已经初始化
        if (likePost == null) {
            likePost = new ArrayList<>();
        } else {
            likePost.clear(); // 清空旧数据
        }
        offset = 0; // 重置offset
        List<Post> newPosts = new DisplayManager(getActivity()).getLikePost(offset);
        likePost.addAll(newPosts); // 添加新的点赞数据
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyDataSetChanged(); // 通知adapter数据发生变化
        } else {
            // 在这里初始化你的adapter，如果它在onCreateView方法中还未被初始化
            recyclerViewAdapter = new LikeAdapter(getActivity(), likePost, currentLatLng);
            recycleViewStagged.setAdapter(recyclerViewAdapter);
        }
        offset += 10; // 更新offset
        smartRefreshLayout.setNoMoreData(false); // 恢复加载更多的功能
    }



}
