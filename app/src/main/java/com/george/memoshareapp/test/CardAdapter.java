package com.george.memoshareapp.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: recyclerView
 * @package: com.george.recyclerview
 * @className: CardAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/5/16 17:31
 * @version: 1.0
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<CardItem> cardItemList;

    public CardAdapter(List<CardItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardItem item = cardItemList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.mipmap.myself_big_img);
        imageList.add(R.mipmap.myself_big_img);
        imageList.add(R.mipmap.myself_circle_image);
        imageList.add(R.mipmap.myself_circle_image);
        imageList.add(R.mipmap.myself_circle_image);
        imageList.add(R.mipmap.myself_circle_image);
        // 创建并设置ImageAdapter
        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.imageRecyclerView.setLayoutManager(linearLayoutManager);

        holder.imageRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        ImageView imageView;
        RecyclerView imageRecyclerView;

        CardViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.image_view1);
            imageRecyclerView = itemView.findViewById(R.id.image_recycler_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageRecyclerView.getVisibility() == View.GONE) {
                        imageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        imageRecyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
