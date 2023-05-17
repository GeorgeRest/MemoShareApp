package com.george.memoshareapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.Fragment.AudioPlayerFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.activities.ReleaseActivity;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "HomeWholeRecyclerViewAd";

    private Context mContext;
    private List<Post> mData;
    public AudioPlayerFragment fragment;
    private Map<ImageView, Fragment> buttonFragmentMap = new HashMap<>();
    private static HomeWholeRecyclerViewAdapter instance;
    public HomeWholeRecyclerViewAdapter() {
    }

    public HomeWholeRecyclerViewAdapter(Context context, List<Post> data) {
        this.mContext = context;
        this.mData = data;
        instance = this;
    }
    public static HomeWholeRecyclerViewAdapter getInstance() {
        return instance;  // 创建一个静态方法来获取当前实例
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.record_one.setTag(holder);
        holder.record_two.setTag(holder);
        holder.record_three.setTag(holder);
        Post post = mData.get(position);
        String phoneNumber = post.getPhoneNumber();
        String name = phoneNumber.substring(0, 5);
        String publishedTime = post.getPublishedTime();
        String location = post.getLocation();
        String publishedText = post.getPublishedText();
        holder.recordings = post.getRecordings();
        List<String> photoCachePath = post.getPhotoCachePath();
        holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(photoCachePath.size())));
        HomePhotoRecyclerViewAdapter innerAdapter = new HomePhotoRecyclerViewAdapter(photoCachePath);
        holder.innerRecyclerView.setAdapter(innerAdapter);
        holder.tv_username.setText(name);
        holder.tv_time.setText(publishedTime);
        holder.tv_location.setText(location);
        holder.tv_content.setText(publishedText);

        if (holder.recordings != null) {
            holder.record_one.setVisibility(View.GONE);
            holder.record_two.setVisibility(View.GONE);
            holder.record_three.setVisibility(View.GONE);
            switch (holder.recordings.size()) {
                case 3:
                    holder.record_three.setVisibility(View.VISIBLE);
                case 2:
                    holder.record_two.setVisibility(View.VISIBLE);
                case 1:
                    holder.record_one.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        switch (v.getId()) {
            case R.id.record_one:
                handleClick(holder.recordings.get(0).getRecordCachePath(), holder.record_one);
                break;
            case R.id.record_two:
                handleClick(holder.recordings.get(1).getRecordCachePath(), holder.record_two);
                break;
            case R.id.record_three:
                handleClick(holder.recordings.get(2).getRecordCachePath(), holder.record_three);
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username;
        private TextView tv_time;
        private TextView tv_location;
        private TextView tv_content;
        RecyclerView innerRecyclerView;
        List<Recordings> recordings;
        ImageView record_one;
        ImageView record_two;
        ImageView record_three;
        public AudioPlayerFragment fragment;
        ViewHolder(View itemView) {
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.rv_images);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_content = itemView.findViewById(R.id.tv_content);
            record_one = itemView.findViewById(R.id.record_one);
            record_two = itemView.findViewById(R.id.record_two);
            record_three = itemView.findViewById(R.id.record_three);
            record_one.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_two.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_three.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
        }
    }

//    public List<String> getLastItem() {
//        if (!mData.isEmpty()) {
//            return mData.get(mData.size() - 1);
//        }
//        return null;
//    }


    private void handleClick(String recordPath, ImageView iv) {
        if (fragment == null) {
            addFragment(recordPath, iv);
        } else {
            AudioPlayerFragment bt_fragment = (AudioPlayerFragment) buttonFragmentMap.get(iv);
            if (fragment != bt_fragment) {
                fragment.stopPlayback();
                HomePageActivity mContext = (HomePageActivity) this.mContext;
                FragmentTransaction fragmentTransaction = ((HomePageActivity) this.mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragment = null;
                addFragment(recordPath, iv);
            } else {
                fragment.togglePlayback();
            }
        }
    }

    private void addFragment(String recordPath, ImageView iv) {
        AudioPlayerFragment.AUDIO_FILE_PATH = recordPath;
        fragment = new AudioPlayerFragment();
        buttonFragmentMap.put(iv, fragment);
        FragmentTransaction fragmentTransaction = ((HomePageActivity) this.mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.record_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    public void addItem(List<Post> item) {
        mData.addAll(item);
    }

    public void addData(Post newData) {
        int startPos = this.mData.size();  // 记录添加前的数据量，即新数据添加的起始位置
        this.mData.add(newData);  // 添加新的数据
        notifyItemRangeInserted(startPos, 1);  // 通知 adapter 在 startPos 位置插入了 newData.size() 条数据
    }

    private int calculateSpanCount(int itemCount) {
        return itemCount > 1 ? 2 : 1;
    }
    public void resetFragment() {
        if (fragment != null) {
            fragment.stopPlayback();
            HomePageActivity mContext = (HomePageActivity) this.mContext;
            FragmentTransaction fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            fragment = null;
        }
    }
}
