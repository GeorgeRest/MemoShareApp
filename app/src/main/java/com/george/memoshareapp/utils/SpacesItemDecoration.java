package com.george.memoshareapp.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.test
 * @className: SpacesItemDecoration
 * @author: George
 * @description: TODO
 * @date: 2023/5/31 18:29
 * @version: 1.0
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int topSpace;
    private int rightSpace;
    private int bottomSpace;

    public SpacesItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = topSpace;
        } else {
            outRect.top = 0;
        }
    }
}
