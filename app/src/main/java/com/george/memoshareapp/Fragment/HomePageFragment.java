package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.events.LastClickedPositionEvent;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.manager.DisplayManager;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class HomePageFragment extends Fragment implements PostDataListener<List<Post>> {//内部

    private static final String ARG_PARAM1 = "param1";
    private static final int PAGE_SIZE = 10;
    private static final String TAG = "HomePageFragment";
    private int currentPage = 0;
    private String mParam1;
    private HomeWholeRecyclerViewAdapter outerAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private DisplayManager displayManager;
    private RecyclerView outerRecyclerView;
    public static int lastClickedPosition = -1;
    private StateLayout state;
    private View rootView;
    private int pageNum = 1;
    private int pageSize = 10;
    private List<Post> posts;

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

        switch (mParam1) {
            case "好友":
                rootView = inflater.inflate(R.layout.fragment_home_friend, container, false);
                state = (StateLayout) rootView.findViewById(R.id.state);
                state.setEmptyLayout(R.layout.layout_empty);
                state.setErrorLayout(R.layout.layout_error);
                state.setLoadingLayout(R.layout.layout_loading);
                state.showLoading(null, false, false);
                displayManager = new DisplayManager();
                displayManager.getPostListByPage(pageNum, pageSize, this);

                outerRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);

                smartRefreshLayout = rootView.findViewById(R.id.refreshLayout);
                smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
                smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
                smartRefreshLayout.setEnableAutoLoadMore(true);
                smartRefreshLayout.setFooterHeight(80);
                smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        pageNum = 1;
                        displayManager.getPostListByPage(pageNum, pageSize, new PostDataListener<List<Post>>() {
                            @Override
                            public void onSuccess(HttpListData<Post> newPostData) {
                                posts.clear(); // 清除原有数据
                                List<Post> newPosts = newPostData.getItems();
                                // 将新数据添加到列表中
                                posts.addAll(newPosts);
                                // 通知 adapter 更新列表
                                outerAdapter.notifyDataSetChanged(); // 通知整个列表数据变化，重绘视图
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
                        });
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
    public void onLastClickedPositionEvent(LastClickedPositionEvent event) {
        lastClickedPosition = event.getPosition();
        if (lastClickedPosition != -1) {
            outerAdapter.notifyItemChanged(lastClickedPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollToTopEvent(ScrollToTopEvent event) {

        if (outerRecyclerView != null) {
            outerRecyclerView.scrollToPosition(0);
        }
    }


    @Override
    public void onSuccess(HttpListData<Post> postData) {
        System.out.println("-----------");
        state.showContent(null);
        System.out.println(postData.isLastPage() + "===========-------------");
        posts = postData.getItems();
        System.out.println("----------" + posts.get(0).getImageParameters().get(0).getPhotoCachePath());

        if (posts == null) {
            state.showEmpty(null);
        } else {
            outerAdapter = new HomeWholeRecyclerViewAdapter(getActivity(), posts);
            outerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            outerRecyclerView.setAdapter(outerAdapter);
        }
        if (postData.isLastPage()) {
            smartRefreshLayout.setNoMoreData(true);
        } else {
            pageNum++;
            smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    displayManager.getPostListByPage(pageNum, pageSize, new PostDataListener<List<Post>>() {
                        @Override
                        public void onSuccess(HttpListData<Post> newPostData) {
                            System.out.println(newPostData.isLastPage() + "newPostData.isLastPage()-------------");
                            System.out.println(pageNum + "pageNum-------------");
                            pageNum++;
                            List<Post> newPosts = newPostData.getItems();
                            posts.addAll(newPosts);
                            int startInsertPosition = posts.size();
                            outerAdapter.notifyItemRangeInserted(startInsertPosition, newPosts.size());
                            refreshlayout.finishLoadMore();

                            if (newPostData.isLastPage()) {
                                refreshlayout.setNoMoreData(true);
                                System.out.println(newPostData.isLastPage() + "----------------");
                            }
                        }

                        @Override
                        public void onError(String error) {
                            refreshlayout.finishLoadMore(false);
                        }
                    });
                }
            });

        }
    }


    @Override
    public void onError(String errorMessage) {
        System.out.println(errorMessage);

    }
}
