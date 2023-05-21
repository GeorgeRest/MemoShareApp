package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.DetailActivity;
import com.george.memoshareapp.beans.Post;

import java.util.List;

public class HomePageBottomAdapter extends RecyclerView.Adapter<HomePageBottomAdapter.ViewHolder> {
    private List<Post> posts;
    private Context context;
    public HomePageBottomAdapter(Context context,List<Post> posts) {
        this.posts = posts;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_bottom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> photoPaths = posts.get(position).getPhotoCachePath();
        if (photoPaths != null && !photoPaths.isEmpty()) {
            Glide.with(holder.imageView.getContext())
                    .load(photoPaths.get(0))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}


