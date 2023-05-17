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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.DateFormat;

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

    private String mParam1;
    private HomeWholeRecyclerViewAdapter outerAdapter;
    private List<Post> displayPostList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DisplayManager displayManager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        switch (mParam1) {
            case "好友":
                rootView = inflater.inflate(R.layout.fragment_home_friend, container, false);
                RecyclerView outerRecyclerView = (RecyclerView) rootView.findViewById(R.id.whole_recycler);
                swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
                displayPostList = new ArrayList<>();
                List<Post> postList = new DisplayManager(getActivity()).getPostList();
                if (postList != null) {
                    for (Post post : postList) {
                        String messageDate = DateFormat.getMessageDate(post.getPublishedTime());
                        post.setPublishedTime(messageDate);
                    }
                }
                if (postList != null) {
                    displayPostList.addAll(postList);
                    Log.d("TAG", displayPostList.get(0).getRecordings().get(0).getRecordCachePath());
                    outerAdapter = new HomeWholeRecyclerViewAdapter(getActivity(), displayPostList);
                    outerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    outerRecyclerView.setAdapter(outerAdapter);
                }



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



}
