package com.george.memoshareapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.george.memoshareapp.beans.HuoDong;
import com.george.memoshareapp.beans.OutterActivityBean;
import com.george.memoshareapp.interfaces.HuoDongItemClickListener;
import com.george.memoshareapp.properties.AppProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HuoDongAdapter extends RecyclerView.Adapter<HuoDongAdapter.ViewHolder>{
    private List<OutterActivityBean> huoDongList = new ArrayList<>();
    HuoDongItemClickListener huoDongItemClickListener;
    private Context context;
    private static final String TAG = "HuoDongAdapter";

    public HuoDongAdapter(Context context, List<OutterActivityBean> huoDongList) {
        this.context = context;
        this.huoDongList = huoDongList;
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
        OutterActivityBean huoDong = huoDongList.get(position);
        if (huoDong!= null) {
            holder.tv_name.setText(huoDong.getName());
            Date date = new Date();
            String publishedTime = huoDong.getPublishedTime();
            try {
                date = new SimpleDateFormat("yy-MM-dd hh:mm:ss").parse(publishedTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd hh:mm");
            String timeString = simpleDateFormat.format(date);
            holder.tv_date.setText(timeString);
            if (huoDong.getHeadPortraitPath() != null) {
//                Glide.with(context).load("http://82.156.138.114/files/media/1685011013.jpg").into(holder.iv_avatar);
                Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + huoDong.getHeadPortraitPath()).into(holder.iv_avatar);//正确地址
//                Glide.with(context).load("https://i1.hdslb.com/bfs/archive/f62d336ae2811fdab7a37e8d1ddf1850a9aed29f.jpg@672w_378h_1c_!web-home-common-cover.webp").into(holder.iv_avatar);
                Log.d(TAG, "onBindViewHolder: AvatarPath: "+AppProperties.SERVER_MEDIA_URL + huoDong.getHeadPortraitPath());//正确地址
            }
            if (huoDong.getFirstImagePath() != null) {
//                Glide.with(context).load("http://82.156.138.114/files/media/1685003784.jpg").into(holder.iv_image);//换成服务器就好使，本地主机就不行，tnnd
                Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + huoDong.getFirstImagePath()).into(holder.iv_image);//正确地址
//                Glide.with(context).load("https://i1.hdslb.com/bfs/archive/f62d336ae2811fdab7a37e8d1ddf1850a9aed29f.jpg@672w_378h_1c_!web-home-common-cover.webp").into(holder.iv_image);
                Log.d(TAG, "onBindViewHolder: FirstImagePath: "+AppProperties.SERVER_MEDIA_URL + huoDong.getFirstImagePath());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    huoDongItemClickListener.onItemClick(position, huoDong);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return huoDongList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_image;
        ImageView iv_avatar;
        TextView tv_name;
        TextView tv_date;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    private void setOnItemClickListener(HuoDongItemClickListener listener) {
        this.huoDongItemClickListener = listener;
    }
}
