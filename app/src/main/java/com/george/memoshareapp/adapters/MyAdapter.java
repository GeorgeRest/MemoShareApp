package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.george.memoshareapp.R;

public class MyAdapter extends PagerAdapter {
 
    private int[] mData;
    private Context mContext;
 
    public MyAdapter(Context ctx, int[] data) {
        this.mContext = ctx;
        this.mData = data;
    }
 
    @Override
    public int getCount() {
        return mData.length;// 返回数据的个数
    }
 
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {//子View显示
        View view = View.inflate(container.getContext(), R.layout.item, null);
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(mData[position]);
 
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "当前条目：" + position, Toast.LENGTH_SHORT).show();
            }
        });
 
        container.addView(view);//添加到父控件
        return view;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;// 过滤和缓存的作用
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);//从viewpager中移除掉
    }
}