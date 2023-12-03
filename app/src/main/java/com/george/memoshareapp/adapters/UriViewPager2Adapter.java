package com.george.memoshareapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;

import java.util.List;

public class UriViewPager2Adapter extends RecyclerView.Adapter<UriViewPager2Adapter.ViewHolder> {
    private List<Uri> mData;


    public UriViewPager2Adapter(List<Uri> photoPath) {
        mData = photoPath;
    }
    @NonNull
    @Override
    public UriViewPager2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_item, parent, false);
        return new UriViewPager2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = mData.get(position);
        Glide.with(holder.imageView.getContext())
                .load(uri)
                .into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fullscreen_image_view);
        }
    }
}

