package com.george.memoshareapp.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.LikeAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.LocationUtil;
import com.google.android.gms.common.util.MapUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.wrapper.RefreshFooterWrapper;

import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends AppCompatActivity {

    private RecyclerView recycleViewStagged;
    private LatLng currentLatLng;
    private List<ImageParameters> parameters;
    private SmartRefreshLayout smartRefreshLayout;
    private List<Post> likePost;
    private LikeAdapter recyclerViewAdapter;
    private int offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_fragment);
        recycleViewStagged = (RecyclerView) findViewById(R.id.recycleViewStagged);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recycleViewStagged.setLayoutManager(layoutManager);

        likePost = new DisplayManager(this).getLikePost(offset);
        currentLatLng = ((MyApplication) getApplication()).getCurrentLatLng();


        recyclerViewAdapter = new LikeAdapter(LikeFragment.this, likePost, currentLatLng);
        ((DefaultItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);

        recycleViewStagged.getItemAnimator().setChangeDuration(0);

        recycleViewStagged.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recycleViewStagged.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recycleViewStagged.setAdapter(recyclerViewAdapter);
        DisplayManager displayManager = new DisplayManager(this);
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

    }
}
