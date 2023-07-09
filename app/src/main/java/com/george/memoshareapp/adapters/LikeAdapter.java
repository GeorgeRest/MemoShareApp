package com.george.memoshareapp.adapters;

import android.content.Context;

import android.content.Intent;
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
import com.george.memoshareapp.activities.DetailActivity;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.view.ProportionalImageView;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.List;

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
public class LikeAdapter extends BaseDifferAdapter<Post, LikeAdapter.ViewHolder> implements View.OnClickListener{
    private Context context;
    private LatLng currentLatLng;
    private double maxRatio = 1.33;

    public LikeAdapter(Context context, LatLng currentLatLng) {
        super(new DiffUtil.ItemCallback<Post>() {
            @Override
            public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
                return oldItem.getId() == (newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
        this.currentLatLng = currentLatLng;
    }

    @Override
    protected void onBindViewHolder(@NonNull LikeAdapter.ViewHolder viewHolder, int i, @Nullable Post post) {
        if (post == null) {
            return;
        }
        viewHolder.iv_photo.setTag(viewHolder);
        ImageParameters imageParameters = post.getImageParameters().get(0);
        String photoCachePath = imageParameters.getPhotoCachePath();
        int width = imageParameters.getWidth();
        int height = imageParameters.getHeight();
        System.out.println("--------------width:" + width + "height:" + height);
        String title = post.getPublishedText();
        double latitude = post.getLatitude();
        double longitude = post.getLongitude();
        if (latitude != 0 || longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            float distance = AMapUtils.calculateLineDistance(latLng, currentLatLng);
            Logger.d("distance", distance);
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            if (distance < 1000) {
                viewHolder.tv_distance.setText(decimalFormat.format(distance) + "m");
            } else {
                String formattedDistance = decimalFormat.format(distance / 1000.0f);
                viewHolder.tv_distance.setText(formattedDistance + "km");
            }
        }
        viewHolder.tv_title.setText(title);
        viewHolder.username.setText(post.getUser().get(0).getName());
        double ratio = (double) height / width;
        if (ratio > maxRatio) {
            ratio = maxRatio;
        }
        viewHolder.iv_photo.setHeightRatio(ratio);
        Glide.with(context)
                .load(photoCachePath)
                .thumbnail(Glide.with(context).load(photoCachePath))
                .error(R.drawable.ic_close)
                .into(viewHolder.iv_photo);
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+post.getUser().get(0).getHeadPortraitPath()).into(viewHolder.circleImageView);
    }

    @NonNull
    @Override
    protected LikeAdapter.ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_cardview_like, null);
        return new ViewHolder(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cardview_like:
                ViewHolder holder = (ViewHolder) view.getTag();
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("post", getItem(position));
                context.startActivity(intent);
                break;
        }

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
            iv_photo.setOnClickListener(LikeAdapter.this);

        }
    }
}
