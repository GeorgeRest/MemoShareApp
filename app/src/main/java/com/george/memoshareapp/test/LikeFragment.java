package com.george.memoshareapp.test;

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

import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends AppCompatActivity {

    private RecyclerView recycleViewStagged;
    private LatLng currentLatLng;
    private List<ImageParameters> parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_fragment);
        recycleViewStagged = (RecyclerView) findViewById(R.id.recycleViewStagged);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recycleViewStagged.setLayoutManager(layoutManager);

        List<Post> likePost = new DisplayManager(this).getLikePost();
        currentLatLng = ((MyApplication) getApplication()).getCurrentLatLng();



        LikeAdapter recyclerViewAdapter = new LikeAdapter(LikeFragment.this, likePost, currentLatLng);
        ((DefaultItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) recycleViewStagged.getItemAnimator()).setSupportsChangeAnimations(false);

        recycleViewStagged.getItemAnimator().setChangeDuration(0);

        recycleViewStagged.setHasFixedSize(true);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recycleViewStagged.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recycleViewStagged.setAdapter(recyclerViewAdapter);

    }
}
