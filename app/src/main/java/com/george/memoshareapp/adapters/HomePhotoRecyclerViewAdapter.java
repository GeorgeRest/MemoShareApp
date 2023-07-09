package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.DetailActivity;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;

import java.util.List;

public class HomePhotoRecyclerViewAdapter extends RecyclerView.Adapter<HomePhotoRecyclerViewAdapter.ViewHolder> {

    private List<ImageParameters> mData;
    private Post post;
    Context context;
    private List<Uri> photoPathList; //用户发布后的uri
    private int outerAdapterPosition;
    private String phoneNumber;

    public HomePhotoRecyclerViewAdapter(List<ImageParameters> data, Post post, Context context, int outerAdapterPosition) {
        this.mData = data;
        this.post = post;
        this.context = context;
        this.outerAdapterPosition=outerAdapterPosition;
    }

    public HomePhotoRecyclerViewAdapter(List<ImageParameters> data, Post post, Context context, List<Uri> photoPathList,int outerAdapterPosition) {
        this.mData = data;
        this.post = post;
        this.context = context;
        this.photoPathList = photoPathList;
        this.outerAdapterPosition=outerAdapterPosition;
        phoneNumber = context.getSharedPreferences("User", Context.MODE_PRIVATE).getString("phoneNumber", "");
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
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        } else {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            if (position > 3) {
                holder.imageView.setVisibility(View.GONE);
            }
        }
        holder.imageView.setLayoutParams(layoutParams);
        String url = mData.get(position).getPhotoCachePath();
        if (outerAdapterPosition==0&&photoPathList != null&&post.getPhoneNumber().equals(phoneNumber)) {
            if (position >= photoPathList.size()) {
                return;
            }
            Uri uri = photoPathList.get(position);
            Glide.with(holder.imageView.getContext())
                    .load(uri)
                    .thumbnail(Glide.with(context).load(R.drawable.photo_loading))
                    .into(holder.imageView);
            Log.d("TAG", "onBindViewHolder: "+uri.toString());
        } else if (url != null && !url.isEmpty()) {
            Glide.with(holder.imageView.getContext())
                    .load(url)
                    .thumbnail(Glide.with(context).load(R.drawable.photo_loading))
                    .error(R.drawable.ic_close)
                    .into(holder.imageView);
            Log.d("TAG", "onBindViewHolder: "+url);
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
        if (mData.size() <= 4) {
            return mData.size();
        } else {
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
//                    页面传值，更改键值对即可
                    SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("post", post);
                    context.startActivity(intent);
                }
            });
        }

    }
}