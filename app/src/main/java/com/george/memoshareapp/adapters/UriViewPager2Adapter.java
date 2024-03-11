package com.george.memoshareapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;

import java.util.List;

public class UriViewPager2Adapter extends RecyclerView.Adapter<UriViewPager2Adapter.ViewHolder> {
    private List<Uri> mData;
    private Context context;


    public UriViewPager2Adapter(List<Uri> photoPath,Context context) {
        mData = photoPath;
        this.context=context;
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
        if (position == mData.size() - 1) {
            Toast.makeText(context, "已滑动到最后一张", Toast.LENGTH_SHORT).show();
        }

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

