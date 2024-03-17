package com.george.memoshareapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.AddHuoDongActivity;
import com.george.memoshareapp.activities.HuoDongDetailActivity;
import com.george.memoshareapp.activities.HuodongGalleryActivity;
import com.george.memoshareapp.activities.PersonalHuodDongActivity;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.adapters.HuoDongAdapter;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.events.HuoDongReleaseEvent;
import com.george.memoshareapp.events.updateLikeState;
import com.george.memoshareapp.events.ScrollToTopEvent;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.HuoDongItemClickListener;
import com.george.memoshareapp.interfaces.HuodongDataListener;
import com.george.memoshareapp.interfaces.PostDataListener;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.manager.HuodongManager;
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
public class HomePageFragment extends Fragment implements PostDataListener<List<Post>>, HuodongDataListener<List<InnerActivityBean>> {//内部

    private static final String ARG_PARAM1 = "param1";
    private static final int PAGE_SIZE = 10;
    private static final String TAG = "HomePageFragment";
    private int currentPage = 0;
    private String mParam1;
    private HomeWholeRecyclerViewAdapter outerAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private SmartRefreshLayout huodongSmartRefreshLayout;
    private DisplayManager displayManager;
    private RecyclerView outerRecyclerView;
    private RecyclerView outerHuoDongRecyclerView;
    public static int lastClickedPosition = -1;
    private StateLayout state;
    private StateLayout huoDongState;
    private View rootView;
    private int pageNum = 1;
    private int huodongPageNum = 1;
    private int pageSize = 10;
    private List<Post> posts;
    private List<InnerActivityBean> huodongList;
    private int likePostId;
    private String phoneNumber;
    private MMKV kv;
    private ImageView btn_add_activity;
    private HuodongManager huodongManager;
    private HuoDongAdapter huoDongAdapter;
    private ImageView btn_my_activity;
    private boolean isEntryBtnVisible = true;;
    private LinearLayout ll_activity_ctrl_entry_zone;
    private ImageView btn_back_to_top;

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
                huoDongState = (StateLayout) rootView.findViewById(R.id.state);
                huoDongState.setEmptyLayout(R.layout.layout_empty);
                huoDongState.setErrorLayout(R.layout.layout_error);
                huoDongState.setLoadingLayout(R.layout.layout_loading);
                huoDongState.showLoading(null, false, false);
                huodongManager = new HuodongManager(getContext());
                outerHuoDongRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);
//                outerRecyclerView.addItemDecoration(new MyBottomDecoration(outerRecyclerView));
                ll_activity_ctrl_entry_zone = (LinearLayout) rootView.findViewById(R.id.ll_activity_ctrl_entry_zone);
                outerHuoDongRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.d(TAG, "onScrolled: dy打印结果：" + dy);
                        if(dy>0&&isEntryBtnVisible){
//                            上滑不可见
                            ll_activity_ctrl_entry_zone.setVisibility(View.GONE);
                            ll_activity_ctrl_entry_zone.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_detail_photo_exit));
                            isEntryBtnVisible = false;
                        } else if(dy<0&&!isEntryBtnVisible){
//                            下滑可见
                            ll_activity_ctrl_entry_zone.setVisibility(View.VISIBLE);
                            ll_activity_ctrl_entry_zone.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_detail_photo_enter));
                            isEntryBtnVisible = true;
                        }
                    }
                });

                initHuoDongData();

                btn_add_activity = (ImageView) rootView.findViewById(R.id.btn_add_activity);

                btn_my_activity = (ImageView) rootView.findViewById(R.id.btn_my_activity);

                btn_back_to_top = (ImageView) rootView.findViewById(R.id.btn_back_to_top);

                btn_add_activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), AddHuoDongActivity.class);
                        intent.putExtra("phoneNumber",phoneNumber);
                        intent.putExtra("isFollowing",false);
                        getContext().startActivity(intent);
                    }
                });

                btn_my_activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PersonalHuodDongActivity.class);
                        intent.putExtra("phoneNumber",phoneNumber);
                        getContext().startActivity(intent);
                    }
                });

                btn_back_to_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(outerRecyclerView.getChildCount()>0){
                            outerRecyclerView.scrollToPosition(0);
                        }
                    }
                });

                break;
            default:
                break;
        }
        return rootView;
    }

    private void initHuoDongData() {
        List<InnerActivityBean> emptyList2 = new ArrayList<>();
        huoDongAdapter = new HuoDongAdapter(getActivity(), emptyList2);
        outerHuoDongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        outerHuoDongRecyclerView.setAdapter(huoDongAdapter);
        huodongPageNum = 1;

        huodongManager.getHuoDongListByPage(huodongPageNum, pageSize, huoDongAdapter.getItemCount(),this);
        huodongSmartRefreshLayout = rootView.findViewById(R.id.refreshLayout);
        huodongSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        huodongSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        huodongSmartRefreshLayout.setEnableAutoLoadMore(true);
        huodongSmartRefreshLayout.setFooterHeight(80);

        huodongSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                huodongPageNum = 1;
                huodongManager.getHuoDongListByPage(huodongPageNum, pageSize,huoDongAdapter.getItemCount(), new HuodongDataListener<List<InnerActivityBean>>() {
                    @Override
                    public void onLoadSuccess(HttpListData<InnerActivityBean> newHuodongData, String type) {
                        kv.clearAll();
                        huodongList.clear();
                        List<InnerActivityBean> newActivitys = newHuodongData.getItems();
                        huodongList.addAll(newActivitys);
                        huoDongAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();

                        refreshLayout.setNoMoreData(newHuodongData.isLastPage());
                        if (!newHuodongData.isLastPage()) {
                            huodongPageNum++;
                        }
                    }
                    @Override
                    public void onLoadError(String error) {
                        refreshLayout.finishRefresh(false); // 结束刷新，但没有收到数据
                    }
                });
            }
        });
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
    public void onHuoDongReleaseEvent(HuoDongReleaseEvent event) {
        if(!event.getHuoDongEvent()){
            if(mParam1 == "活动"){
                initHuoDongData();
            }
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

    }

    @Override
    public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
        huoDongState.showContent(null);
        huodongPageNum++;
        huodongList = data.getItems();

        if (huodongList.size()==0) {
            huoDongState.showEmpty(null);
        } else {
            huoDongAdapter = new HuoDongAdapter(getActivity(), huodongList);
//            Log.d(TAG, "zxtest onLoadSuccess: huodongList:" + huodongList.size());
            outerHuoDongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            outerHuoDongRecyclerView.setAdapter(huoDongAdapter);
            huoDongAdapter.setOnHuodongClickListener(new HuoDongItemClickListener() {
                @Override
                public void onHuoDongClick(int position, InnerActivityBean huoDong) {
                    Intent intent = new Intent(getContext(), HuodongGalleryActivity.class);
                    intent.putExtra("phoneNumber",phoneNumber);
                    intent.putExtra("firstHuoDong",huoDong);
                    startActivity(intent);//后期尝试用共享控件
                }
            });
        }
        Log.d(TAG, "onLoadSuccess: isLastPage = " + data.isLastPage());
        if (data.isLastPage()) {
            huodongSmartRefreshLayout.setNoMoreData(data.isLastPage());
        } else {
            huodongSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    huodongManager.getHuoDongListByPage(huodongPageNum, pageSize, huoDongAdapter.getItemCount(), new HuodongDataListener<List<InnerActivityBean>>() {
                        @Override
                        public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
                            huodongPageNum++;
                            List<InnerActivityBean> newActivitys = data.getItems();
                            huodongList.addAll(newActivitys);
                            int startInsertPosition = huodongList.size();
                            huoDongAdapter.notifyItemRangeInserted(startInsertPosition, newActivitys.size());
                            refreshlayout.finishLoadMore();
                            refreshlayout.setNoMoreData(data.isLastPage());
                        }

                        @Override
                        public void onLoadError(String errorMessage) {
                            refreshlayout.finishLoadMore(false);
                            Log.e(TAG, "onLoadError: "+errorMessage +"#492");
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onLoadError(String errorMessage) {
        Log.e(TAG, "onLoadError: "+errorMessage);
    }

}

//class MyBottomDecoration extends RecyclerView.ItemDecoration {
//    RecyclerView recyclerView;
//    public MyBottomDecoration(RecyclerView recyclerView) {
//        this.recyclerView = recyclerView;
//    }
//
//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        if(parent.getChildCount()>0){
//            if(parent.getChildAdapterPosition(view) == recyclerView.getAdapter().getItemCount() - 1){
//                outRect.bottom = 200;
//            }
//        }
//    }
//}
