package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.properties.AppProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InformAdapter extends BaseAdapter {
    private Context context;
    private List<CommentBean> list;
    private LayoutInflater inflater;

    public InformAdapter(Context context, List<CommentBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InformAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_inform,null);
            holder = new ViewHolder();
            holder.niv_photo = convertView.findViewById(R.id.niv_photo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_inform_time = convertView.findViewById(R.id.tv_inform_time);
            holder.tv_inform_content = convertView.findViewById(R.id.tv_inform_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        CommentBean commentBean = list.get(position);
        User contact = commentBean.getUser();
        // 获得头像
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+contact.getHeadPortraitPath()).into(holder.niv_photo);    // 获取头像
        holder.tv_name.setText(contact.getName());
        String time =getTimeFormatText(commentBean.getCommentTime());
        holder.tv_inform_time.setText(time);
        holder.tv_inform_content.setText(commentBean.getCommentContent());
        return convertView;
    }

    private static class ViewHolder{
        ImageView niv_photo;
        TextView tv_name;
        TextView tv_inform_time;
        TextView tv_inform_content;
    }
    /**
     * 时间差
     *
     */
    public String getTimeFormatText(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            Date date = formatter.parse(dateString);

            long minute = 60 * 1000;// 1分钟
            long hour = 60 * minute;// 1小时
            long day = 24 * hour;// 1天
            long month = 31 * day;// 月
            long year = 12 * month;// 年

            // 创建一个新的SimpleDateFormat实例，指定所需的格式
            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd", Locale.getDefault());
            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // 使用formatter.format(currentDate)方法将日期对象转换为字符串
            String dateString1 = formatter1.format(date);
            String dateString2 = formatter2.format(date);
            String dateString3 = formatter3.format(date);

            if (date == null) {
                return null;
            }
            long diff = new Date().getTime() - date.getTime();
            long r = 0;
            if (diff > year) {
                return dateString3;
            }

            if (diff > day) {
                r = (diff / day);
                if(r == 1) {
                    return "昨天" +dateString1;
                }else{
                    return dateString2 ;
                }
            }
            if (diff > hour) {
                r = (diff / hour);
                return r + "小时前";
            }
            if (diff > minute) {
                r = (diff / minute);
                return r + "分钟前";
            }
            return "刚刚";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
