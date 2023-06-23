package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.QuickAdapterHelper;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.LikeAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.events.PostLikeUpdateEvent;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private StaggeredGridLayoutManager layoutManager;
    private QuickAdapterHelper helper;

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
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        switch (mParam1) {
            case "发布":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_release, container, false);
                break;
            case "点赞":
                rootView = inflater.inflate(R.layout.personalpage_pagefragment_dainzan, container, false);
                recycleViewStagged = (RecyclerView) rootView.findViewById(R.id.recycleViewStagged);
                smartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshLayout);
                smartRefreshLayout.setEnableAutoLoadMore(true);
                smartRefreshLayout.autoRefresh();//自动刷新
                smartRefreshLayout.autoLoadMore();//自动加载
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                recycleViewStagged.setLayoutManager(layoutManager);
                DisplayManager displayManager = new DisplayManager(getActivity());
                likePost = displayManager.getLikePost(offset);
                for (Post post : likePost) {
                    System.out.println("likePost" + post.getId());
                }

                currentLatLng = ((MyApplication) getActivity().getApplication()).getCurrentLatLng();
                System.out.println(offset + "offset1");

                recyclerViewAdapter = new LikeAdapter(getActivity(), currentLatLng);
                recyclerViewAdapter.submitList(likePost);


                ((DefaultItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
                ((SimpleItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
                recycleViewStagged.getItemAnimator().setChangeDuration(0);
                recycleViewStagged.setHasFixedSize(true);

                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                recycleViewStagged.addItemDecoration(new SpacesItemDecoration(spacingInPixels,spacingInPixels,spacingInPixels,spacingInPixels));
                recycleViewStagged.setAdapter(recyclerViewAdapter);


                recycleViewStagged.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override

                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                        super.onScrollStateChanged(recyclerView, newState);

                        layoutManager.invalidateSpanAssignments();//重新布局

                    }

                    @Override

                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                    }

                });


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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostUpdateEvent(PostLikeUpdateEvent event) {
        if (recyclerViewAdapter == null) {
            return;
        }

        System.out.println("--------------------" + event.getPost().getId() + "-------------------");
        likePost = new DisplayManager(getActivity()).getLikePost(0);
        recyclerViewAdapter.submitList(likePost);
        layoutManager.invalidateSpanAssignments();



    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
