package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;

import java.util.List;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<List<String>> mData;

    public HomeWholeRecyclerViewAdapter(Context context, List<List<String>> data) {
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
        List<String> innerData = mData.get(position);
        holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(innerData.size())));
        HomePhotoRecyclerViewAdapter innerAdapter = new HomePhotoRecyclerViewAdapter(innerData);
        holder.innerRecyclerView.setAdapter(innerAdapter);
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
        }
    }

    public List<String> getLastItem() {
        if (!mData.isEmpty()) {
            return mData.get(mData.size() - 1);
        }
        return null;
    }

    public void addItem(List<String> item) {
        mData.add(item);
    }

    private int calculateSpanCount(int itemCount) {
        return itemCount > 1 ? 2 : 1;
    }
}
