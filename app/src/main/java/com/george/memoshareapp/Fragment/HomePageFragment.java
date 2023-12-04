package com.george.memoshareapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
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
import com.george.memoshareapp.events.updateLikeState;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.manager.DisplayManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;

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
    private int likePostId;
    private String phoneNumber;
    private MMKV kv;

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
        phoneNumber = getContext().getSharedPreferences("User", MODE_PRIVATE).getString("phoneNumber", "");
        EventBus.getDefault().register(this);
        kv = MMKV.defaultMMKV();
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
                outerRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);
                List<Post> emptyList = new ArrayList<>();
                outerAdapter = new HomeWholeRecyclerViewAdapter(getActivity(), emptyList);
                displayManager.getPostListByPageFriend(pageNum, pageSize, outerAdapter.getItemCount(),phoneNumber,this);
                smartRefreshLayout = rootView.findViewById(R.id.refreshLayout);
                smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
                smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
                smartRefreshLayout.setEnableAutoLoadMore(true);
                smartRefreshLayout.setFooterHeight(80);
                smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        pageNum = 1;

                        displayManager.getPostListByPageFriend(pageNum, pageSize,outerAdapter.getItemCount(),phoneNumber, new PostDataListener<List<Post>>() {
                            @Override
                            public void onSuccess(HttpListData<Post> newPostData,String type) {

                                kv.clearAll();
                                posts.clear();
                                List<Post> newPosts = newPostData.getItems();
                                posts.addAll(newPosts);
                                outerAdapter.notifyDataSetChanged();
                                refreshLayout.finishRefresh();

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
                rootView = inflater.inflate(R.layout.fragment_home_friend, container, false);
                state = (StateLayout) rootView.findViewById(R.id.state);
                state.setEmptyLayout(R.layout.layout_empty);
                state.setErrorLayout(R.layout.layout_error);
                state.setLoadingLayout(R.layout.layout_loading);
                state.showLoading(null, false, false);
                displayManager = new DisplayManager();
                outerRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);
                List<Post> emptyList1 = new ArrayList<>();
                outerAdapter = new HomeWholeRecyclerViewAdapter(getActivity(), emptyList1);
                displayManager.getPostListByPage(pageNum, pageSize, outerAdapter.getItemCount(),phoneNumber,this);
                smartRefreshLayout = rootView.findViewById(R.id.refreshLayout);
                smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
                smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
                smartRefreshLayout.setEnableAutoLoadMore(true);
                smartRefreshLayout.setFooterHeight(80);
                smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        pageNum = 1;
                        displayManager.getPostListByPage(pageNum, pageSize,outerAdapter.getItemCount(),phoneNumber, new PostDataListener<List<Post>>() {
                            @Override
                            public void onSuccess(HttpListData<Post> newPostData,String type) {
                                kv.clearAll();
                                posts.clear();
                                List<Post> newPosts = newPostData.getItems();
                                posts.addAll(newPosts);

                                outerAdapter.notifyDataSetChanged();
                                refreshLayout.finishRefresh();
                                if (newPostData.isLastPage()) {
                                    refreshLayout.setNoMoreData(true);
                                } else {
                                    pageNum++;
                                    refreshLayout.setNoMoreData(false);
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
    public void updateLikeState(updateLikeState event) {
        likePostId = event.getPostId();
        String key = "post_position_" + likePostId;
        int position = kv.decodeInt(key, -1);

        if (position != -1) {
            // 更新 Adapter 中对应位置的点赞状态
            outerAdapter.notifyItemChanged(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollToTopEvent(ScrollToTopEvent event) {

        if (outerRecyclerView != null) {
            outerRecyclerView.scrollToPosition(0);
        }
    }


    @Override
    public void onSuccess(HttpListData<Post> postData,String type) {
        state.showContent(null);

        posts = postData.getItems();

        if (posts.size()==0) {
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
                    displayManager.getPostListByPage(pageNum, pageSize,outerAdapter.getItemCount(),phoneNumber , new PostDataListener<List<Post>>() {
                        @Override
                        public void onSuccess(HttpListData<Post> newPostData,String type) {
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
