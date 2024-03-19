package com.george.memoshareapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.interfaces.OnDelPicClickListener;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;

import java.util.List;

public class AddActivityPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Uri> imagePaths;
    private static final int VIEW_TYPE_PIC = 0;
    private static final int VIEW_TYPE_ADD = 1;
    public static final int REQUEST_CODE_CHOOSE = 2;
    private OnDelPicClickListener onDelPicClickListener;

    public void setOnDelPicClickListener(OnDelPicClickListener onDelPicClickListener) {
        this.onDelPicClickListener = onDelPicClickListener;
    }

    public AddActivityPicAdapter(Context context, List<Uri> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_PIC:
                return new PicViewHolder(View.inflate(context, R.layout.item_huodong_pic,null));
            case VIEW_TYPE_ADD:
                return new AddViewHolder(View.inflate(context, R.layout.item_add_huodong_pic,null));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(imagePaths.size() < 9){
            return position + 1 == getItemCount() ? VIEW_TYPE_ADD : VIEW_TYPE_PIC;
        }else {
            return VIEW_TYPE_PIC;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PicViewHolder) {
            Glide.with(context)
                    .load(imagePaths.get(position))
                    .centerCrop()
                    .into(((PicViewHolder)holder).iv_pic);
            ((PicViewHolder)holder).iv_del.setOnClickListener(v -> {onDelPicClickListener.onPicDelete(position);});
        } else if (holder instanceof AddViewHolder) {
            if (imagePaths.size() >= 9) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPictureSelector();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (imagePaths.size() < 9) {
            return imagePaths.size() + 1;
        } else {
            return 9;
        }

    }
    private void startPictureSelector(){
        int selectImageSize = 0;
        if (imagePaths.size() > 0 && imagePaths.size() < 9) {
            selectImageSize = 9 - imagePaths.size();
        }else {
            selectImageSize = 9;
        }
        PictureSelector.create(context)
                .openGallery(SelectMimeType.ofImage())
                .isWithSelectVideoImage(false)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(selectImageSize)
                .isEmptyResultReturn(true)
                .isMaxSelectEnabledMask(true)
                .forResult(REQUEST_CODE_CHOOSE);
    }
}

class PicViewHolder extends RecyclerView.ViewHolder{
    ImageView iv_pic;
    ImageView iv_del;
    public PicViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_pic = (ImageView) itemView.findViewById(R.id.iv_huodong_pic);
        iv_del = (ImageView) itemView.findViewById(R.id.iv_del_huodong_pic);
    }
}

class AddViewHolder extends RecyclerView.ViewHolder{

    public AddViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}