package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.FullScreenImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailPhotoRecycleViewAdapter extends RecyclerView.Adapter<DetailPhotoRecycleViewAdapter.ViewHolder> {

    private final Context context;
    private List<String> mData;

    public DetailPhotoRecycleViewAdapter(Context context,List<String> photoPath) {
        this.mData = photoPath;
        this.context = context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_photo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int size = mData.size();
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        if (size == 1) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_max);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_max);
        }
        if (size == 2 || size == 4) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_mid);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_mid);
        }
        if(size == 3 || size == 5 || size == 6 || size == 7 || size == 8 || size == 9) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_min);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.detail_image_min);
        }
        holder.imageView.setLayoutParams(layoutParams);
        String url = mData.get(position);
        if (url != null && !url.isEmpty()) {
            Glide.with(holder.imageView.getContext()).load(new File(url)).into(holder.imageView);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putStringArrayListExtra("photoPath", new ArrayList<>(mData));
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }

    }
}
