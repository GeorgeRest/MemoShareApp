package com.george.memoshareapp;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        // 禁止 RecyclerView 垂直滚动，这样它只会占据其内容所需的空间
        return false;
    }
}
