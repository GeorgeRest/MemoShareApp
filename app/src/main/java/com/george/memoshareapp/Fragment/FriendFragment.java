package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.FriendBaseQuickAdapter;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class FriendFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rv_friend;
    private List<User> userList = null;
    private int choice;
    private String initiator_phoneNumber;
    private boolean isMe;
    private SmartRefreshLayout refreshLayout;
    private FriendBaseQuickAdapter adapter;
    private int position;

    public FriendFragment() {
    }

    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void getPosition(int position){
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // 从Arguments中获取Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 从Bundle中提取自定义类型的List
            userList = (List<User>) bundle.getSerializable("userList");
            choice = bundle.getInt("choice");
            initiator_phoneNumber = bundle.getString("phoneNumber", "0");
            isMe = bundle.getBoolean("isMe");
        }

        rv_friend = (RecyclerView) view.findViewById(R.id.rv_friend);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_friend.setLayoutManager(manager);
        adapter = new FriendBaseQuickAdapter(getContext(),choice,initiator_phoneNumber,isMe);
        adapter.submitList(userList);
        rv_friend.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.setNoMoreData(false);
                userList.clear();  // 清空likePost列表
                UserManager userManager = new UserManager(getContext());
                List<User> newUserList = null;
                if(position == 0){
                    newUserList = userManager.getFollowedUser(initiator_phoneNumber);
                } else if (position == 1) {
                    newUserList = userManager.getFansUser(initiator_phoneNumber);
                } else if (position == 2) {
                    newUserList = userManager.getFriendUser(initiator_phoneNumber);
                }
                userList.addAll(newUserList);
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
            }
        });

    }




}