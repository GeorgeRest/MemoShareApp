package com.george.memoshareapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.view.ProportionalImageView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.adapters
 * @className: LikeAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/5/31 11:09
 * @version: 1.0
 */
public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private LatLng currentLatLng;

    public LikeAdapter(Context context, List<Post> posts, LatLng currentLatLng) {
        this.context = context;
        this.posts = posts;
        this.currentLatLng = currentLatLng;
        System.out.println(currentLatLng + "-----------");
    }

    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_cardview_like, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        ImageParameters imageParameters = post.getImageParameters().get(0);
        String photoCachePath = imageParameters.getPhotoCachePath();
        int width = imageParameters.getWidth();
        int height = imageParameters.getHeight();
        System.out.println("--------------width:" + width + "height:" + height);
        String title = post.getPublishedText();
        double latitude = post.getLatitude();
        double longitude = post.getLongitude();
        if (latitude != 0.0 || longitude != 0.0) {
            LatLng latLng = new LatLng(latitude, longitude);
            System.out.println(latLng+"-----------"+currentLatLng);
            float distance = AMapUtils.calculateArea(latLng, currentLatLng);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedDistance = decimalFormat.format(distance / 1000.0f);
            float distanceKm = Float.parseFloat(formattedDistance);
            holder.distance.setText(distanceKm + "km");
        }

        holder.title.setText(title);

        // Calculate the ratio using the obtained width and height.
        double ratio = (double) height / width;

        // Set the height ratio before loading the image.
        holder.iv_photo.setHeightRatio(ratio);

        // Load the image using Glide, without obtaining the bitmap first.
        Glide.with(context)
                .load(photoCachePath)
                .placeholder(R.drawable.loading)
                .into(holder.iv_photo);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView distance;
        TextView title;
        TextView username;
        ProportionalImageView iv_photo;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.like_username);
            title = (TextView) view.findViewById(R.id.like_title);
            distance = (TextView) view.findViewById(R.id.like_distance);
            iv_photo = (ProportionalImageView) view.findViewById(R.id.iv_cardview_like);
            circleImageView = (CircleImageView) view.findViewById(R.id.iv_cardview_like_user);

        }
    }
}