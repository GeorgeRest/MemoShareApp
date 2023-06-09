package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.statelayout.StateLayout;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.FriendBaseQuickAdapter;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.UserServiceApi;
import com.george.memoshareapp.http.response.HttpListData;
import com.george.memoshareapp.manager.RetrofitManager;
import com.george.memoshareapp.manager.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    private RecyclerView rv_friend;
    private List<User> userList = null;
    private int choice;
    private String initiator_phoneNumber;
    private boolean isMe;
    private SmartRefreshLayout refreshLayout;
    private FriendBaseQuickAdapter adapter;
    private int position;
    private UserServiceApi apiService;
    private List<User> userList1;
    private StateLayout state;

    public FriendFragment() {
    }

    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        state = (StateLayout) view.findViewById(R.id.state);
        state.setEmptyLayout(R.layout.layout_empty);
        state.setErrorLayout(R.layout.layout_error);
        state.setLoadingLayout(R.layout.layout_loading);
        state.showLoading(null, false, false);

        // 从Arguments中获取Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            choice = bundle.getInt("choice");
            initiator_phoneNumber = bundle.getString("phoneNumber", "0");
            isMe = bundle.getBoolean("isMe");
        }

        rv_friend = (RecyclerView) view.findViewById(R.id.rv_friend);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_friend.setLayoutManager(manager);
        adapter = new FriendBaseQuickAdapter(getContext(),choice,initiator_phoneNumber,isMe);

        apiService = RetrofitManager.getInstance().create(UserServiceApi.class);
        getData(initiator_phoneNumber,choice);


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.setNoMoreData(false);
                List<User> newUserList = null;
                if(position == 0){
                    getFollowedUserList(initiator_phoneNumber);
                } else if (position == 1) {
                    getFansUserList(initiator_phoneNumber);
                } else if (position == 2) {
                    getFriendUserList(initiator_phoneNumber);
                }
                refreshlayout.finishRefresh();
            }
        });
    }

    private void getData(String phoneNumber,int choice) {
        if(choice == 0){
            getFollowedUserList(phoneNumber);
        } else if (choice == 1) {
            getFansUserList(phoneNumber);
        } else if (choice ==2) {
            getFriendUserList(phoneNumber);
        }

    }

    private void getFriendUserList(String phoneNumber) {
        Call<HttpListData<User>> friendUserCall = apiService.getFriendUser(phoneNumber);
        friendUserCall.enqueue(new Callback<HttpListData<User>>() {
            @Override
            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
                state.showContent(null);
                userList1 = response.body().getItems();
                adapter.submitList(userList1);
                rv_friend.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<HttpListData<User>> call, Throwable t) {
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFansUserList(String phoneNumber) {
        Call<HttpListData<User>> fansUserCall = apiService.getFansUser(phoneNumber);
        fansUserCall.enqueue(new Callback<HttpListData<User>>() {
            @Override
            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
                state.showContent(null);
                userList1 = response.body().getItems();
                adapter.submitList(userList1);
                rv_friend.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HttpListData<User>> call, Throwable t) {
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFollowedUserList(String phoneNumber) {
        Call<HttpListData<User>> followedUserCall = apiService.getFollowedUser(phoneNumber);
        followedUserCall.enqueue(new Callback<HttpListData<User>>() {
            @Override
            public void onResponse(Call<HttpListData<User>> call, Response<HttpListData<User>> response) {
                userList1 = response.body().getItems();
                state.showContent(null);
                adapter.submitList(userList1);
                rv_friend.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<HttpListData<User>> call, Throwable t) {
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}