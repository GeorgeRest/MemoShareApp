package com.george.memoshareapp.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int dividerHeight;
    private final int dividerWidth;
    private final int offsetLeft;
    private final int offsetRight;

    public CustomDividerItemDecoration(int color, int height, int width, int leftOffset, int rightOffset) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        dividerHeight = height;
        dividerWidth = width;
        offsetLeft = leftOffset;
        offsetRight = rightOffset;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft() + offsetLeft;
        int right = parent.getWidth() - parent.getPaddingRight() - offsetRight;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // Skip the first item to not draw a divider above the first item
            if (i == 0) continue;

            int top = child.getTop() - params.topMargin - dividerHeight;
            int bottom = top + dividerHeight;

            // Draw the divider
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Skip the first item to not draw a divider above the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, dividerHeight, 0, 0);
        }
    }
}
