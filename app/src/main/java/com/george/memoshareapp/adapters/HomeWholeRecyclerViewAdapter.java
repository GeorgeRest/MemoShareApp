package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;

import java.util.List;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder>{
    private  TextView tv_time;
    private TextView tv_location;
    private TextView tv_content;
    private TextView tv_username;
    private Context mContext;
    private List<Post> mData;

    public HomeWholeRecyclerViewAdapter(Context context, List<Post> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mData.get(position);
        String phoneNumber = post.getPhoneNumber();
        String name = phoneNumber.substring(1, 5);
        String publishedTime = post.getPublishedTime();
        String location = post.getLocation();
        String publishedText = post.getPublishedText();
        List<String> photoCachePath = post.getPhotoCachePath();
        holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(getItemCount())));
        HomePhotoRecyclerViewAdapter innerAdapter = new HomePhotoRecyclerViewAdapter(photoCachePath);
        holder.innerRecyclerView.setAdapter(innerAdapter);
        tv_username.setText(name);
        tv_time.setText(publishedTime);
        tv_location.setText(location);
        tv_content.setText(publishedText);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.rv_images);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_content = itemView.findViewById(R.id.tv_content);


        }
    }

//    public List<String> getLastItem() {
//        if (!mData.isEmpty()) {
//            return mData.get(mData.size() - 1);
//        }
//        return null;
//    }

    public void addItem(List<Post> item) {
        mData.addAll(item);
    }

    private int calculateSpanCount(int itemCount) {
        return itemCount > 1 ? 2 : 1;
    }
}
