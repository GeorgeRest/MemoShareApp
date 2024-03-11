package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.DateFormat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
        Post post = posts.get(position);
        List<String> photoPaths = post.getPhotoCachePath();
        String time = post.getPublishedTime();

        String formatTime = DateFormat.convertToTime(time);
        holder.bottomTime.setText(formatTime);
        if (photoPaths != null && !photoPaths.isEmpty()) {
            Glide.with(holder.imageView.getContext())
                    .load(photoPaths.get(0))
                    .thumbnail(Glide.with(context).load(R.drawable.photo_loading))
                    .error(R.drawable.ic_close)
                    .into(holder.imageView);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                List<ImageParameters> photoCachePath = post.getImageParameters();
                for (ImageParameters imageParameters:photoCachePath) {
                    imageParameters.setPhotoCachePath(AppProperties.SERVER_MEDIA_URL+imageParameters.getPhotoCachePath());
                }
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bottomTime;
        CircleImageView circleImageView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bottom_image_view);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_image);
            bottomTime = (TextView) itemView.findViewById(R.id.bottom_time);
        }
    }
}


