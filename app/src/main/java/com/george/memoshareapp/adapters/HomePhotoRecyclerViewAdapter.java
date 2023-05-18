package com.george.memoshareapp.adapters;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;

import java.util.ArrayList;
import java.util.List;

public class HomePhotoRecyclerViewAdapter extends RecyclerView.Adapter<HomePhotoRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;


    public HomePhotoRecyclerViewAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_photo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int size = mData.size();
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        if (size == 1) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large);
        } else {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
        }
        holder.imageView.setLayoutParams(layoutParams);
        String url = mData.get(position);
        if (url != null && !url.isEmpty()) {
             Glide.with(holder.imageView.getContext()).load(url).into(holder.imageView);
        }

        if (position == 3 && size > 4) {
            holder.extraImageCount.setVisibility(View.VISIBLE);
            holder.extraImageCount.setText("+" + (size - 4));
        } else {
            holder.extraImageCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView extraImageCount;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            extraImageCount = itemView.findViewById(R.id.extra_image_count);
        }

    }
}