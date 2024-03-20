package com.george.memoshareapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
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
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.interfaces.HuoDongItemClickListener;
import com.george.memoshareapp.interfaces.HuoDongSelectedListener;
import com.george.memoshareapp.properties.AppProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HuoDongAdapter extends RecyclerView.Adapter<HuoDongAdapter.ViewHolder>{
    private List<InnerActivityBean> huoDongList = new ArrayList<>();
    private List<Map<Integer, Boolean>> chooseList = new ArrayList<>();
    HuoDongItemClickListener huoDongItemClickListener;
    HuoDongSelectedListener huoDongSelectedListener;

    public void setHuoDongSelectedListener(HuoDongSelectedListener huoDongSelectedListener) {
        this.huoDongSelectedListener = huoDongSelectedListener;
    }

    private Context context;

    private boolean isVisible = false;
    private boolean isCheck = false;
    private static final String TAG = "HuoDongAdapter";

    public HuoDongAdapter(Context context, List<InnerActivityBean> huoDongList) {
        this.context = context;
        this.huoDongList = huoDongList;
    }

    public HuoDongAdapter(Context context, List<InnerActivityBean> huoDongList,List<Map<Integer, Boolean>> chooseList) {
        this.context = context;
        this.huoDongList = huoDongList;
        this.chooseList = chooseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new HuoDongAdapter.ViewHolder(view);
    }



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InnerActivityBean huoDong = huoDongList.get(position);
        if (huoDong!= null) {
//            holder.tv_name.setText(huoDong.getName());
            Date date = new Date();
            String publishedTime = huoDong.getPublishedTime();
            try {
                date = new SimpleDateFormat("yy-MM-dd hh:mm:ss").parse(publishedTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
            String timeString = simpleDateFormat.format(date);
            holder.tv_date.setText(timeString);
            holder.tv_location.setText(huoDong.getLocation());
            if (huoDong.getFirstImagePath() != null) {
                Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + huoDong.getFirstImagePath()).placeholder(R.drawable.huodong_pic_default).into(holder.iv_image);//正确地址
                Log.d(TAG, "onBindViewHolder: FirstImagePath: "+AppProperties.SERVER_MEDIA_URL + huoDong.getFirstImagePath());
            }

            if (isVisible) {
                isCheck = chooseList.get(position).get(huoDong.getActivityId());
                holder.iv_check_btn.setVisibility(View.VISIBLE);
                holder.iv_checked_btn.setVisibility(View.GONE);
                Log.d(TAG, "onBindViewHolder: isVisible" + isVisible);

                if (isCheck) {
                    holder.iv_check_btn.setVisibility(View.GONE);
                    holder.iv_checked_btn.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_check_btn.setVisibility(View.VISIBLE);
                    holder.iv_checked_btn.setVisibility(View.GONE);
                }

            } else {
                holder.iv_check_btn.setVisibility(View.GONE);
                holder.iv_checked_btn.setVisibility(View.GONE);
            }
            holder.iv_check_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCheck = chooseList.get(position).get(huoDong.getActivityId());
                    Log.d(TAG, "onClick: iv_check_btn : \nchooseList.get(position) = " + chooseList.get(position).toString() +
                            "\nposition = " + position + "\nhuoDong.getActivityId() = " + huoDong.getActivityId());
                    if(!isCheck){
                        if(huoDongSelectedListener == null){
                            return;
                        }
                        huoDongSelectedListener.onSelectClick(true,position,huoDong.getActivityId());
                    }
                }
            });

            holder.iv_checked_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCheck = chooseList.get(position).get(huoDong.getActivityId());
                    Log.d(TAG, "onClick: iv_checked_btn : \nchooseList.get(position) = " + chooseList.get(position).toString() +
                            "\nposition = " + position + "\nhuoDong.getActivityId() = " + huoDong.getActivityId());
                    if(isCheck){
                        if(huoDongSelectedListener == null){
                            return;
                        }
                        huoDongSelectedListener.onSelectClick(false,position,huoDong.getActivityId());
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(huoDongItemClickListener != null){
                        huoDongItemClickListener.onHuoDongClick(position, huoDong);
                    }
                }
            });
        }
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    @Override
    public int getItemCount() {
        return huoDongList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_image;
//        ImageView iv_avatar;
//        TextView tv_name;
        TextView tv_date;
        TextView tv_location;
        ImageView iv_check_btn;
        ImageView iv_checked_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
//            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            iv_check_btn = (ImageView) itemView.findViewById(R.id.iv_check_btn);
            iv_checked_btn = (ImageView) itemView.findViewById(R.id.iv_checked_btn);
        }
    }

    public void setOnHuodongClickListener(HuoDongItemClickListener huoDongItemClickListener) {
        this.huoDongItemClickListener = huoDongItemClickListener;
    }
}
