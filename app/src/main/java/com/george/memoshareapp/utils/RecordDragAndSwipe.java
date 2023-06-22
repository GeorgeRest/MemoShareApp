package com.george.memoshareapp.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe;
import com.george.memoshareapp.adapters.RecordAdapter;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.utils
 * @className: RecordDragAndSwipe
 * @author: George
 * @description: TODO
 * @date: 2023/6/14 15:17
 * @version: 1.0
 */
public class RecordDragAndSwipe extends QuickDragAndSwipe {
    private RecordAdapter recordAdapter;

    public RecordDragAndSwipe(RecordAdapter adapter) {
        this.recordAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int viewType = recordAdapter.getItemViewType(viewHolder.getAdapterPosition());
        if (viewType == RecordAdapter.NEW_RECORD) {
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
        int draggedViewType = recordAdapter.getItemViewType(viewHolder.getAdapterPosition());
        // 获取目标视图类型
        int targetViewType = recordAdapter.getItemViewType(target.getAdapterPosition());

        // 如果两者都是 NEW_RECORD，则允许交换
        if (draggedViewType == RecordAdapter.NEW_RECORD && targetViewType == RecordAdapter.NEW_RECORD) {
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


