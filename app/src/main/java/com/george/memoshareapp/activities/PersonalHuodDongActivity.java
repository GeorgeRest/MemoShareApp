package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.HuoDongAdapter;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.events.HuoDongDeleteEvent;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.interfaces.HuoDongItemClickListener;
import com.george.memoshareapp.interfaces.HuoDongSelectedListener;
import com.george.memoshareapp.interfaces.HuodongDataListener;
import com.george.memoshareapp.interfaces.HuodongDelListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalHuodDongActivity extends BaseActivity implements HuodongDataListener<List<InnerActivityBean>> {

    private StateLayout state;
    private RecyclerView outerRecyclerView;
    private HuodongManager huodongManager;
    private HuoDongAdapter huoDongAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private int pageSize = 10;
    private List<InnerActivityBean> huodongList;
    private List<Map<Integer, Boolean>> mapList = new ArrayList<>();
    private ImageView iv_my_activity_del;
    private ImageView iv_my_activity_edit;
    private int huodongPageNum = 1;
    private String phoneNumber;
    private List<Integer> delList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_huod_dong);
        huodongManager = new HuodongManager(this);
        phoneNumber = getSharedPreferences("User", MODE_PRIVATE).getString("phoneNumber", "");
        initView();

    }

    private void initView() {
        state = (StateLayout) findViewById(R.id.state);
//                正式
        state.setEmptyLayout(R.layout.layout_empty);
        state.setErrorLayout(R.layout.layout_error);
        state.setLoadingLayout(R.layout.layout_loading);
        state.showLoading(null, false, false);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        outerRecyclerView = (RecyclerView) findViewById(R.id.whole_recycler);
        List<InnerActivityBean> emptyList2 = new ArrayList<>();
        List<Map<Integer,Boolean>> emptyList3 = new ArrayList<>();
//                正式
        huoDongAdapter = new HuoDongAdapter(this, emptyList2,emptyList3);

        huodongManager.getPersonalHuoDongListByPage(phoneNumber,huodongPageNum, pageSize, huoDongAdapter.getItemCount(),this);

        iv_my_activity_edit = (ImageView) findViewById(R.id.iv_my_activity_edit);
        iv_my_activity_del = (ImageView) findViewById(R.id.iv_my_activity_del);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> finish());

        iv_my_activity_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_my_activity_edit.setVisibility(View.GONE);
                iv_my_activity_del.setVisibility(View.VISIBLE);
                huoDongAdapter.setVisible(true);
                huoDongAdapter.notifyItemRangeChanged(0,huoDongAdapter.getItemCount());
                //TODO
//                转为可编辑状态

            }
        });
        iv_my_activity_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(delList.size()>0){
                    new XPopup.Builder(PersonalHuodDongActivity.this).asConfirm("确定删除" + delList.size() + "项内容？", null, "取消", "确认", new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            iv_my_activity_del.setVisibility(View.GONE);
                            iv_my_activity_edit.setVisibility(View.VISIBLE);
                            huoDongAdapter.setVisible(false);
                            huodongPageNum = 1;
                            mapList = new ArrayList<>();
                            huodongManager.delPersonalHuoDongs(delList, new HuodongDelListener() {
                                @Override
                                public void onDeleteResult(boolean result) {
                                    initView();
                                    delList.clear();
                                    EventBus.getDefault().post(new HuoDongDeleteEvent());
                                }
                            });
                        }
                    }, new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    },false).show();
                }else {
                    iv_my_activity_del.setVisibility(View.GONE);
                    iv_my_activity_edit.setVisibility(View.VISIBLE);
                    huoDongAdapter.setVisible(false);
                    if(huoDongAdapter.getItemCount() > 0){
                        huoDongAdapter.notifyItemRangeChanged(0,huoDongAdapter.getItemCount());
                    }
                }
            }
        });

    }

    @Override
    public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
        state.showContent(null);

        huodongList = data.getItems();
        for(InnerActivityBean huodong : huodongList){
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            map.put(huodong.getActivityId(),false);
            mapList.add(map);
        }

        if (huodongList.size()==0) {
            state.showEmpty(null);
        } else {
            huoDongAdapter = new HuoDongAdapter(PersonalHuodDongActivity.this, huodongList,mapList);
            outerRecyclerView.setLayoutManager(new LinearLayoutManager(PersonalHuodDongActivity.this));
            outerRecyclerView.setAdapter(huoDongAdapter);
            huoDongAdapter.setOnHuodongClickListener(new HuoDongItemClickListener() {
                @Override
                public void onHuoDongClick(int position, InnerActivityBean huoDong) {
//                    TODO
//                    直接跳转到详情页

                    Intent intent = new Intent(PersonalHuodDongActivity.this, HuoDongDetailActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("activityId", huoDong.getActivityId());
                    intent.putExtra("activityInfo", huoDong);
                    startActivity(intent);

//                    Intent intent = new Intent(PersonalHuodDongActivity.this, HuodongGalleryActivity.class);
//                    intent.putExtra("phoneNumber",phoneNumber);
//                    intent.putExtra("firstHuoDong",huoDong);
//                    startActivity(intent);//后期尝试用共享控件
                }
            });

            huoDongAdapter.setHuoDongSelectedListener(new HuoDongSelectedListener() {
                @Override
                public void onSelectClick(boolean isSelected, int position, int huoDongId) {
                    Boolean check = mapList.get(position).get(huoDongId);
                    if (check != isSelected) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mapList.get(position).replace(huoDongId, isSelected);
                        }
                        if(isSelected){
                            delList.add(huoDongId);
                        }else {
                            delList.remove(delList.indexOf(huoDongId));
                        }
                        huoDongAdapter.notifyItemRangeChanged(0,huoDongAdapter.getItemCount());
                    }
                }
            });

        }
        if (data.isLastPage()) {
            smartRefreshLayout.setNoMoreData(true);
        } else {
            huodongPageNum++;
            smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    huodongManager.getPersonalHuoDongListByPage(phoneNumber,huodongPageNum, pageSize, huoDongAdapter.getItemCount(), new HuodongDataListener<List<InnerActivityBean>>() {
                        @Override
                        public void onLoadSuccess(HttpListData<InnerActivityBean> data, String type) {
                            System.out.println(data.isLastPage() + "data.isLastPage()-------------");
                            System.out.println(huodongPageNum + "huodongPageNum-------------");
                            huodongPageNum++;
                            List<InnerActivityBean> newActivitys = data.getItems();
                            for(InnerActivityBean huodong : newActivitys){
                                Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
                                map.put(huodong.getActivityId(),false);
                                mapList.add(map);
                            }
                            huodongList.addAll(newActivitys);
                            int startInsertPosition = huodongList.size();
                            huoDongAdapter.notifyItemRangeInserted(startInsertPosition, newActivitys.size());
                            refreshlayout.finishLoadMore();

                            if (data.isLastPage()) {
                                refreshlayout.setNoMoreData(true);
                                System.out.println(data.isLastPage() + "----------------");
                            }
                        }

                        @Override
                        public void onLoadError(String errorMessage) {
                            refreshlayout.finishLoadMore(false);
                            Log.e("打印测试", "onLoadError: "+errorMessage +"#380");
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onLoadError(String errorMessage) {

    }



}