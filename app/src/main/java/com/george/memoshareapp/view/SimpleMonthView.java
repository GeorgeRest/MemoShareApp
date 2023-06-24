package com.george.memoshareapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.george.memoshareapp.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.orhanobut.logger.Logger;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class SimpleMonthView extends MonthView {

    private int mRadius;
    private Paint mTodayTextPaint;
    private Drawable mCustomDrawable;

    public SimpleMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;

        mTodayTextPaint = new Paint();
        mTodayTextPaint.setAntiAlias(true);
        mTodayTextPaint.setTextAlign(Paint.Align.CENTER);
        mTodayTextPaint.setColor(Color.RED);
        mTodayTextPaint.setTextSize(mCurDayTextPaint.getTextSize());

        // Load your custom drawable here
        mCustomDrawable = getResources().getDrawable(R.drawable.purple_circle);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        if (hasScheme) {
            int left = cx - mCustomDrawable.getIntrinsicWidth() / 4;
            int top = y + mItemHeight - mRadius - mCustomDrawable.getIntrinsicHeight() / 2 + 70; // 使这行代码与onDrawScheme方法中的相同
            int right = left + mCustomDrawable.getIntrinsicWidth() / 2;
            int bottom = top + mCustomDrawable.getIntrinsicHeight() / 2;
            mCustomDrawable.setBounds(left, top, right, bottom);
            mCustomDrawable.draw(canvas);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        // 计算图片的位置
        int left = x + mItemWidth / 2 - mCustomDrawable.getIntrinsicWidth() / 4;
        int top = y + mItemHeight - mRadius - mCustomDrawable.getIntrinsicHeight() / 2 + 70;

        // 设置图片的边界
        mCustomDrawable.setBounds(left, top, left + mCustomDrawable.getIntrinsicWidth() / 2, top + mCustomDrawable.getIntrinsicHeight() / 2);

        // 绘制自定义图片
        mCustomDrawable.draw(canvas);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (calendar.isCurrentDay()) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mTodayTextPaint);
//            if (mCustomDrawable != null) {
//                int drawableWidth = mCustomDrawable.getIntrinsicWidth();
//                int drawableHeight = mCustomDrawable.getIntrinsicHeight();
//                int left = cx - drawableWidth / 4;
//                int top = (int) (baselineY + mTodayTextPaint.descent()) - drawableHeight / 2;
//                int right = left + drawableWidth / 2;
//                int bottom = top + drawableHeight / 2;
//                mCustomDrawable.setBounds(left, top, right, bottom);
//                mCustomDrawable.draw(canvas);
//            }
        } else if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
