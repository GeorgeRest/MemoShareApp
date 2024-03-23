package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.HuodongGalleryActivity;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.interfaces.OuterHuodongClickListener;
import com.george.memoshareapp.properties.AppProperties;
import com.luck.picture.lib.photoview.PhotoView;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class OutHuodongViewPagerAdapter extends RecyclerView.Adapter<OutHuodongViewPagerAdapter.ViewHolder> {

    Context context;
    List<InnerActivityBean> huodonglist = new ArrayList<>();
    OuterHuodongClickListener outerHuodongClickListener;


    public void setOuterHuodongClickListener(OuterHuodongClickListener outerHuodongClickListener) {
        this.outerHuodongClickListener = outerHuodongClickListener;
    }

    public OutHuodongViewPagerAdapter(Context context, List<InnerActivityBean> huodonglist) {
        this.context = context;
        this.huodonglist = huodonglist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_out_huodong_view_pager, parent, false);
        OutHuodongViewPagerAdapter.ViewHolder viewHolder = new OutHuodongViewPagerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InnerActivityBean innerActivityBean = huodonglist.get(position);

        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + innerActivityBean
                .getHeadPortraitPath())
                .placeholder(R.drawable.huodong_pic_default).into(holder.iv_avatar);
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + innerActivityBean
                .getFirstImagePath())
                .placeholder(R.drawable.huodong_pic_default).into(holder.iv_activity_image);

        holder.tv_publisher.setText(innerActivityBean.getName());

        if (innerActivityBean.getPublishText() != null) {
            holder.tv_publish_text.setText(innerActivityBean.getPublishText());
        } else {
            holder.tv_publish_text.setText("");
        }

        holder.tv_publish_time.setText(innerActivityBean.getPublishedTime());

        holder.tv_publish_location.setText(innerActivityBean.getLocation());

        if(innerActivityBean.getTag() != null && innerActivityBean.getTag().length() > 0){
            holder.tv_publish_tag.setText("#" + innerActivityBean.getTag());
        } else {
            holder.tv_publish_tag.setText("#无标签");
        }

        holder.iv_activity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerHuodongClickListener.onHuodongClick(innerActivityBean);
            }
        });

        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                outerHuodongClickListener.onAvatarClick(innerActivityBean.get);//考虑是否跳转到个人用户界面
            }
        });

    }

    @Override
    public int getItemCount() {
        return huodonglist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar;
        ImageView iv_activity_image;
        TextView tv_publisher;
        TextView tv_publish_text;
        TextView tv_publish_time;
        TextView tv_publish_location;
        TextView tv_publish_tag;

        ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_activity_image = itemView.findViewById(R.id.iv_activity_image);
            tv_publisher = itemView.findViewById(R.id.tv_publisher);
            tv_publish_text = itemView.findViewById(R.id.tv_publish_text);
            tv_publish_time = itemView.findViewById(R.id.tv_publish_time);
            tv_publish_location = itemView.findViewById(R.id.tv_publish_location);
            tv_publish_tag = itemView.findViewById(R.id.tv_publish_tag);

        }
    }
}
