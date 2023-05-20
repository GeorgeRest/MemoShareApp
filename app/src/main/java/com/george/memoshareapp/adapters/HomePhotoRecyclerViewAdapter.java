package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.DetailActivity;
import com.george.memoshareapp.beans.Post;

import java.util.List;

public class HomePhotoRecyclerViewAdapter extends RecyclerView.Adapter<HomePhotoRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private Post post;
    Context context;

    public HomePhotoRecyclerViewAdapter(List<String> data, Post post,Context context) {
        this.mData = data;
        this.post = post;
        this.context=context;
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
            if (position == 0) {
                layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large);
                layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large);
            }else {
                holder.imageView.setVisibility(View.GONE);
            }
        } else {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            if (position > 3){
                holder.imageView.setVisibility(View.GONE);
            }
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
        if (mData.size() <=4) {
            return mData.size();
        }else {
            return 4;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView extraImageCount;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            extraImageCount = itemView.findViewById(R.id.extra_image_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // 处理子项点击事件的逻辑
                    Toast.makeText(itemView.getContext(), "子项点击，位置：" + position, Toast.LENGTH_SHORT).show();
                    System.out.println("---------"+post);
//                    页面传值，更改键值对即可
                    SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("post", post);
//                    intent.putExtra("has_liked",sharedPreferences.getBoolean("has_liked",false));
                    context.startActivity(intent);
                }
            });
        }

    }
}
