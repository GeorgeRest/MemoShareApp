package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.InnerActivityBean;
import com.george.memoshareapp.properties.AppProperties;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {
    List<InnerActivityBean> huodonglist = new ArrayList<>();//正式
    int selectItem;

    Context context;
//    int[] images = new int[0];
    private int position;

    public GalleryAdapter(List<InnerActivityBean> huodonglist, Context context) {//正式
        this.huodonglist = huodonglist;
        this.context = context;
    }

//    public GalleryAdapter(int[] images,Context context) {//测试
//        this.images = images;
//        this.context = context;
//    }

    @Override
    public int getCount() {
        return huodonglist.size();//正式
//        return images.length;//测试
    }

    @Override
    public Object getItem(int position) {
        return huodonglist.get(position);//正式
//        return position;//测试
    }

    @Override
    public long getItemId(int position) {
        this.position = position;
        return position;
    }

    public void setSelectItem(int selectItem) {

        if (this.selectItem != selectItem) {

            this.selectItem = selectItem;

            notifyDataSetChanged();

        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);


        if(selectItem == position){
            imageView.setLayoutParams(new Gallery.LayoutParams(140, 210));
        }else {
            imageView.setLayoutParams(new Gallery.LayoutParams(80, 120));
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


//        imageView.setImageResource(images[position]);//测试

//        正式
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL + huodonglist.get(position).getFirstImagePath()).into(imageView);//测试

//          测试
//        Glide.with(context).load(huodonglist.get(position).getFirstImagePath()).into(imageView);//测试

        return imageView;
    }


}
