package com.george.memoshareapp.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe;
import com.george.memoshareapp.adapters.ImageAdapter;

public class ImageDragAndSwipe extends QuickDragAndSwipe {
    private ImageAdapter imageAdapter;

    public ImageDragAndSwipe(ImageAdapter adapter) {
        this.imageAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int viewType = imageAdapter.getItemViewType(viewHolder.getAdapterPosition());
        if (viewType == ImageAdapter.VIEW_TYPE_IMAGE) {
            // 对于 NEW_RECORD 类型的 itemView，允许拖拽和滑动
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            // 对于 RECORD 类型的 itemView，禁止拖拽和滑动
            return makeMovementFlags(0, 0);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 获取拖动的视图类型
        int draggedViewType = imageAdapter.getItemViewType(viewHolder.getAdapterPosition());
        // 获取目标视图类型
        int targetViewType = imageAdapter.getItemViewType(target.getAdapterPosition());

        // 如果两者都是 NEW_RECORD，则允许交换
        if (draggedViewType == ImageAdapter.VIEW_TYPE_IMAGE && targetViewType == ImageAdapter.VIEW_TYPE_IMAGE) {
            return true;
        } else {
            // 否则阻止交换
            return false;
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

}