package com.george.memoshareapp.adapters;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseDifferAdapter;
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.view.ProportionalImageView;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.adapters
 * @className: testLikeAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/6/10 16:34
 * @version: 1.0
 */
public class LikeAdapter extends BaseDifferAdapter<Post, LikeAdapter.ViewHolder>implements DragAndSwipeDataCallback {
    private Context context;
    private LatLng currentLatLng;
    private double maxRatio = 1.33;


    public LikeAdapter(Context context, LatLng currentLatLng) {
        super(new DiffUtil.ItemCallback<Post>() {
            @Override
            public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
                // You need to implement an appropriate comparison here.
                // This assumes that Post has an getId method that provides a unique identifier.
                return oldItem.getId()==(newItem.getId());
            }
            @Override
            public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
                // You need to implement the equals method in Post class.
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
        this.currentLatLng = currentLatLng;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @Nullable Post post) {
        if (post == null) {
            return;
        }
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
            float distance = AMapUtils.calculateArea(latLng, currentLatLng);
            System.out.println("--------------distance:" + distance);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedDistance = decimalFormat.format(distance / 1000.0f);
            float distanceKm = Float.parseFloat(formattedDistance);
            viewHolder.tv_distance.setText(distanceKm + "km");
        }
        viewHolder.tv_title.setText(title);
        double ratio = (double) height / width;
        if (ratio > maxRatio) {
            ratio = maxRatio;
        }
        viewHolder.  iv_photo.setHeightRatio(ratio);
        Glide.with(context)
                .load(photoCachePath)
                .placeholder(R.drawable.loading)
                .into(viewHolder.iv_photo);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_cardview_like, null);
        return new ViewHolder(view);
    }

    @Override
    public void dataSwap(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
    }

    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ProportionalImageView iv_photo;
        TextView tv_distance;
        TextView tv_title;
        TextView username;
        public ViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.iv_cardview_like_user);
            iv_photo = view.findViewById(R.id.iv_cardview_like);
            tv_distance = view.findViewById(R.id.like_distance);
            tv_title = view.findViewById(R.id.like_title);
            username = view.findViewById(R.id.like_username);
        }
    }
}
