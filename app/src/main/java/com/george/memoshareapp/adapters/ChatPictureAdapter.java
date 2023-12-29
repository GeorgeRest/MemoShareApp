package com.george.memoshareapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ChatPictureAdapter extends RecyclerView.Adapter {

    private final static int TYPE_DATE = 0;
    private final static int TYPE_PICTURE = 1;
    private final List<Object> mPhotoList = new ArrayList<>();
//    private OnItemClickListener mOnItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PICTURE) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_picture, parent, false);
            return new PictureViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_picture_date, parent, false);
            return new DateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mPhotoList.size() <= 0) {
            return;
        }
        if (holder instanceof PictureViewHolder) {
            String photoFileName =(String) mPhotoList.get(position);
            Glide.with(((PictureViewHolder) holder).iv_chat_photo.getContext()).load(AppProperties.SERVER_MEDIA_URL+photoFileName).into(((PictureViewHolder) holder).iv_chat_photo);

            ((PictureViewHolder) holder).setPosition(position);
//            ((PictureViewHolder) holder).iv_chat_photo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnItemClickListener != null) {
//                        List<String> pictureList = new ArrayList<>();
//                        for (Object obj : mPhotoList) {
//                            if (obj instanceof String) {
//                                pictureList.add((String) obj);
//                            }
//                        }
//                        int clickPhotoPosition = findPositionByName(pictureList, photoFileName);
//                        mOnItemClickListener.onPhotoClick(v,pictureList, clickPhotoPosition);
//                    }
//                }
//            });
        } else if (holder instanceof DateViewHolder) {
            Date date = new Date((long) mPhotoList.get(position));
            ((DateViewHolder) holder).tv_chat_picture_date.setText(TimeUtil.getTimeString(date, "yyyy/MM"));
        }
    }

    private int findPositionByName(List<String> pictureList, String photoFileName) {
        Iterator<String> iterator = pictureList.listIterator();
        int index = -1;
        boolean isExist = false;
        while (iterator.hasNext()) {
            index += 1;
            String name = iterator.next();
            if (Objects.equals(name, photoFileName)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            index = -1;
        }
        return index;
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public void setData(List<Object> list) {
        this.mPhotoList.clear();
        this.mPhotoList.addAll(list);
    }


    @Override
    public int getItemViewType(int position) {
        if (mPhotoList.get(position) instanceof String) {
            return TYPE_PICTURE;
        } else {
            return TYPE_DATE;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_DATE ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private static class PictureViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_chat_photo;
        private int position;

        private PictureViewHolder(View itemView) {
            super(itemView);
            iv_chat_photo = (ImageView) itemView.findViewById(R.id.iv_chat_picture);
        }

        private void setPosition(int position) {
            this.position = position;
        }
    }

    private static class DateViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_chat_picture_date;

        private DateViewHolder(View itemView) {
            super(itemView);
            tv_chat_picture_date = (TextView) itemView.findViewById(R.id.tv_chat_picture_date);
        }
    }
}
