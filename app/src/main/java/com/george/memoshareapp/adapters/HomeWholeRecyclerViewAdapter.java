package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.test.CardAdapter;
import com.george.memoshareapp.test.CardItem;

import java.util.ArrayList;
import java.util.List;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<Post> mData;

    public HomeWholeRecyclerViewAdapter(Context context, List<Post> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // 设置点击事件监听器
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                // 处理子项点击事件的逻辑
                Toast.makeText(mContext, "子项点击，位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return viewHolder;
//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mData.get(position);
        String phoneNumber = post.getPhoneNumber();
        String name = phoneNumber.substring(1, 5);
        String publishedTime = post.getPublishedTime();
        String location = post.getLocation();
        String publishedText = post.getPublishedText();

        holder.tv_username.setText(name);
        holder.tv_time.setText(publishedTime);
        holder.tv_location.setText(location);
        holder.tv_content.setText(publishedText);

        List<String> photoCachePath = post.getPhotoCachePath();
        holder.rv_images.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(photoCachePath.size())));
        HomePhotoRecyclerViewAdapter innerAdapter = new HomePhotoRecyclerViewAdapter(photoCachePath);
        holder.rv_images.setAdapter(innerAdapter);

//        设置头像，需要contact（@人名所对应的图片，相关adapter未完成）
//        List<String> contacts = post.getContacts();
//        holder.rv_head_image.setLayoutManager(new GridLayoutManager(mContext, 1));
//        HomePageHeadImageAdapter headImageAdapter = new HomePageHeadImageAdapter(contacts);
//        holder.rv_head_image.setAdapter(headImageAdapter);


//        if (发布人 == 自己) {
//            holder.itemView.setBackgroundColor(Color.WHITE);
//        }  else {
//            holder.itemView.setBackgroundColor(Color.Purple);
//        }

        List<CardItem> cardItemList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            cardItemList.add(new CardItem("Title " + i, "Description " + i));
        }
        CardAdapter cardAdapter = new CardAdapter(cardItemList);
        holder.rv_myself_image.setAdapter(cardAdapter);

        holder.image_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rv_myself_image.getVisibility() == View.GONE) {
                    holder.rv_myself_image.setVisibility(View.VISIBLE);
                } else {
                    holder.rv_myself_image.setVisibility(View.GONE);
                }
            }
        });

        holder.ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_layout;
        LinearLayout ll_head;
        TextView tv_time;
        TextView tv_location;
        TextView tv_content;
        TextView tv_username;
        RecyclerView rv_myself_image;
        RecyclerView rv_head_image;
        RecyclerView rv_images;
        ImageView image_view1;

        ViewHolder(View itemView) {
            super(itemView);
            rv_images = itemView.findViewById(R.id.rv_images);
            rv_head_image = itemView.findViewById(R.id.rv_head_image);
            rv_myself_image = itemView.findViewById(R.id.image_recycler_view);

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_content = itemView.findViewById(R.id.tv_content);
            image_view1 = itemView.findViewById(R.id.image_view1);

            ll_head = itemView.findViewById(R.id.ll_head);
            rl_layout = itemView.findViewById(R.id.rl_layout);
        }
    }

//    public List<String> getLastItem() {
//        if (!mData.isEmpty()) {
//            return mData.get(mData.size() - 1);
//        }
//        return null;
//    }

//    public void addItem(List<Post> item) {
//        mData.addAll(item);
//    }

    private int calculateSpanCount(int itemCount) {
        return itemCount > 1 ? 2 : 1;
    }
}
