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
import com.george.memoshareapp.beans.ImageParameters;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoRecycleViewAdapter extends RecyclerView.Adapter<DetailPhotoRecycleViewAdapter.ViewHolder> {

    private final Context context;
    private List<ImageParameters> mData;

    public DetailPhotoRecycleViewAdapter(Context context,List<ImageParameters> photoPath) {
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
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large1);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_large1);
        }
        if (size == 2 || size == 4) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small);
        }
        if(size == 3 || size == 5 || size == 6 || size == 7 || size == 8 || size == 9) {
            layoutParams.width = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small1);
            layoutParams.height = holder.imageView.getResources().getDimensionPixelSize(R.dimen.image_size_small1);
        }
        holder.imageView.setLayoutParams(layoutParams);
        String url = mData.get(position).getPhotoCachePath();
        if (url != null && !url.isEmpty()) {
            Glide.with(holder.imageView.getContext())
                    .load(url)
                    .thumbnail(Glide.with(context).load(R.drawable.photo_loading))
                    .error(R.drawable.ic_close).into(holder.imageView);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ArrayList<String> list = new ArrayList<>();
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    for (ImageParameters imageParameters:mData) {
                        list.add(imageParameters.getPhotoCachePath());
                    }
                    intent.putStringArrayListExtra("photoPath", list);
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
