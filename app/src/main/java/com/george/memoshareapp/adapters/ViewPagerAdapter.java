package com.george.memoshareapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.interfaces.HuoDongImageClickListener;
import com.george.memoshareapp.properties.AppProperties;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<String> mData;
    private Context mContext;
    private HuoDongImageClickListener huoDongImageClickListener;

    public ViewPagerAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_huodong_viewpager, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = mData.get(position);
        Glide.with(holder.mPhotoView.getContext())
                .load(AppProperties.SERVER_MEDIA_URL + imagePath)
                .into(holder.mPhotoView);
        holder.mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("adapter点击测试", "onClick: ");
                huoDongImageClickListener.onImageClick();
            }
        });
        holder.mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                huoDongImageClickListener.onImageLongClick(AppProperties.SERVER_MEDIA_URL + imagePath);
                return true;
            }
        });
    }

    public void setHuoDongImageClickListener(HuoDongImageClickListener huoDongImageClickListener) {
        this.huoDongImageClickListener = huoDongImageClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView mPhotoView;

        ViewHolder(View itemView) {
            super(itemView);
            mPhotoView = itemView.findViewById(R.id.pv_activity);

        }
    }
}
