package com.george.memoshareapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.george.memoshareapp.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class SimpleMonthView extends MonthView {

    private int mRadius;
    private Paint mTodayTextPaint;
    private Drawable mPurpleCircleDrawable;
    private Drawable mRedDrawable;
    private Drawable mRedPurpleDrawable;
    private Paint mOtherDayTextPaint;

    public SimpleMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;

        mTodayTextPaint = new Paint();
        mTodayTextPaint.setAntiAlias(true);
        mTodayTextPaint.setTextAlign(Paint.Align.CENTER);
        mTodayTextPaint.setColor(Color.BLACK); // 设置颜色为黑色
        mTodayTextPaint.setTextSize(mCurDayTextPaint.getTextSize() * 1.2f); // 字体大一些
        mTodayTextPaint.setTypeface(Typeface.DEFAULT_BOLD); // 字体加粗


        mOtherDayTextPaint = new Paint();
        mOtherDayTextPaint.setAntiAlias(true);
        mOtherDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherDayTextPaint.setColor(Color.parseColor("#685c97")); // 设置为你指定的颜色
        mOtherDayTextPaint.setTextSize(mCurDayTextPaint.getTextSize());
        mOtherDayTextPaint.setTypeface(Typeface.DEFAULT);


        // Load your custom drawable here
        mPurpleCircleDrawable = getResources().getDrawable(R.drawable.purple_circle);
        mRedDrawable = getResources().getDrawable(R.drawable.red_circle);
        mRedPurpleDrawable = getResources().getDrawable(R.drawable.red_purple_circle);
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
            int schemeColor = calendar.getSchemeColor();
            Drawable drawable = getSchemeColor(schemeColor);
            int left = cx - drawable.getIntrinsicWidth() / 4;
            int top = y + mItemHeight - mRadius - drawable.getIntrinsicHeight() / 2 + 70;
            int right = left + drawable.getIntrinsicWidth() / 2;
            int bottom = top + drawable.getIntrinsicHeight() / 2;
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int schemeColor = calendar.getSchemeColor();
        Drawable drawable = getSchemeColor(schemeColor);
        int left = x + mItemWidth / 2 - drawable.getIntrinsicWidth() / 4;
        int top = y + mItemHeight - mRadius - drawable.getIntrinsicHeight() / 2 + 70;

        // 设置图片的边界
        drawable.setBounds(left, top, left + drawable.getIntrinsicWidth() / 2, top + drawable.getIntrinsicHeight() / 2);

        // 绘制自定义图片
        drawable.draw(canvas);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (calendar.isCurrentDay()) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mTodayTextPaint);
        } else if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentMonth() ? mSelectTextPaint : mOtherMonthTextPaint);
        }
    }


    private Drawable getSchemeColor(int color) {
        switch (color) {
            case Color.RED:
                return mRedDrawable;

            case Color.GREEN:
                return mPurpleCircleDrawable;

            case Color.GRAY:
                return mRedPurpleDrawable;

            default:
                return mRedDrawable;
        }
    }
}
