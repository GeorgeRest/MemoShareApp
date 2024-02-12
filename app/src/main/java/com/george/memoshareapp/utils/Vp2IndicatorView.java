package com.george.memoshareapp.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;

public class Vp2IndicatorView extends View {
    public static final int STYLE_CIRCLR_CIRCLE = 0;

    // 指示器之间的间距
    private int mIndicatorItemDistance;

    //选中与为选中的颜色
    private ColorStateList mColorSelected, mColorUnSelected;

    //指示器样式，默认都是圆点
    private int mIndicatorStyle = STYLE_CIRCLR_CIRCLE;

    // 圆点半径大小
    private int circleCircleRadius = 0;

    //画笔
    private Paint mUnSelectedPaint, mSelectedPaint;

    //指示器item的区域
    private RectF mIndicatorItemRectF;

    //指示器大小
    private int mIndicatorItemWidth, mIndicatorItemHeight;

    //指示器item个数
    private int mIndicatorItemCount = 0;

    //当前选中的位置
    private int mCurrentSeletedPosition = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Vp2IndicatorView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Vp2IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Vp2IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        intPaint();
        verifyItemCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        mColorSelected = getContext().getColorStateList(R.color.follow_btn);
        mColorUnSelected = getContext().getColorStateList(R.color.separator);
        mIndicatorItemDistance = dip2px(6);
        mIndicatorStyle = STYLE_CIRCLR_CIRCLE;
        circleCircleRadius = dip2px(3);
    }

    private void intPaint() {
        mUnSelectedPaint = new Paint();
        mUnSelectedPaint.setStyle(Paint.Style.FILL);
        mUnSelectedPaint.setAntiAlias(true);
        mUnSelectedPaint.setColor(mColorUnSelected == null ?
                Color.GRAY : mColorUnSelected.getDefaultColor());

        mSelectedPaint = new Paint();
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(mColorSelected == null ?
                Color.RED : mColorSelected.getDefaultColor());

        mIndicatorItemRectF = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        mIndicatorItemWidth = 2 * circleCircleRadius * mIndicatorItemCount
                + (mIndicatorItemCount - 1) * mIndicatorItemDistance;
        mIndicatorItemHeight = Math.max(heightSize, 2 * circleCircleRadius);

        setMeasuredDimension(mIndicatorItemWidth, mIndicatorItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cy = mIndicatorItemHeight / 2;
        for (int i = 0; i < mIndicatorItemCount; i++) {
            int cx = (i + 1) * circleCircleRadius + i * mIndicatorItemDistance;
            canvas.drawCircle(cx, cy, circleCircleRadius,
                    i == mCurrentSeletedPosition ? mSelectedPaint : mUnSelectedPaint);
        }
    }

    public void attachToViewPager2(ViewPager2 vp2) {
        RecyclerView.Adapter pagerAdapter = vp2.getAdapter();
        if (pagerAdapter != null) {
            mIndicatorItemCount = pagerAdapter.getItemCount();
            mCurrentSeletedPosition = vp2.getCurrentItem();
            verifyItemCount();
        }

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (vp2 != null && pagerAdapter != null) {
                    mCurrentSeletedPosition = vp2.getCurrentItem();
                }
                postInvalidate();
            }
        });
    }

    private void verifyItemCount() {
        if (mCurrentSeletedPosition >= mIndicatorItemCount) {
            mCurrentSeletedPosition = mIndicatorItemCount - 1;
        }
        setVisibility((mIndicatorItemCount <= 1) ? GONE : VISIBLE);
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

