package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.DateFormat;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.Fragment
 * @className: HomePageFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/14 23:09
 * @version: 1.0
 */
public class HomePageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final int PAGE_SIZE = 10;
    private static final String TAG = "HomePageFragment";
    private int currentPage = 0;
    private String mParam1;
    private HomeWholeRecyclerViewAdapter outerAdapter;
    private List<Post> displayPostList;
    private SmartRefreshLayout smartRefreshLayout;
    private DisplayManager displayManager;
    private List<Post> postList;
    private RecyclerView outerRecyclerView;

    public HomePageFragment() {

    }

    public static HomePageFragment newInstance(String param1) {
        HomePageFragment fragment = new HomePageFragment();
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
            case "好友":
                rootView = inflater.inflate(R.layout.fragment_home_friend, container, false);
                outerRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);
                smartRefreshLayout = rootView.findViewById(R.id.refreshLayout);
                displayManager = new DisplayManager(getActivity());
                postList = displayManager.getPostList();

                if (postList != null) {
                    outerAdapter = new HomeWholeRecyclerViewAdapter(getActivity(), postList);
                    outerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    outerRecyclerView.setAdapter(outerAdapter);
                }
                smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
                smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
                smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {
                        List<Post> newPosts = HomePageFragment.this.displayManager.getPostList();
                        if (newPosts.isEmpty()||newPosts.size()==0) {
                            refreshlayout.setNoMoreData(true);
                        } else {
                            postList.addAll(newPosts);
                            outerAdapter.notifyDataSetChanged();
                        }
                        refreshlayout.finishLoadMore();
                    }
                });


                break;

            case "推荐":
                rootView = inflater.inflate(R.layout.fragment_home_recommend, container, false);

                break;
            case "活动":
                rootView = inflater.inflate(R.layout.fragment_home_activity, container, false);

                break;
            default:
                break;
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollToTopEvent(ScrollToTopEvent event) {
        Log.d(TAG, "onScrollToTopEvent: 事件接收成功");
        if (outerRecyclerView != null) {
            outerRecyclerView.scrollToPosition(0);
        }
    }

}
