package com.george.memoshareapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ReplyBean;

import java.util.Date;
import java.util.List;

public class ReplyAdapter extends BaseAdapter {

    private int resourceId;
    private List<ReplyBean> list;
    private LayoutInflater inflater;
    private SpannableString ss;
    private Context context;

    public ReplyAdapter(Context context, List<ReplyBean> list, int resourceId){
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
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
        ReplyBean bean = list.get(position);
        ViewHolder holder = null ;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            holder.replyPhoto= (ImageView) convertView.findViewById(R.id.iv_reply_photo);
            holder.replyName= (TextView)convertView.findViewById(R.id.tv_reply_name);
            holder.replyTime= (TextView)convertView.findViewById(R.id.tv_replyTime);
            holder.replyText= (TextView)convertView.findViewById(R.id.replyContent);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
    }
        holder.replyPhoto.setImageResource(bean.getReplyUserPhoto());
        holder.replyName.setText(bean.getReplyNickname());
        holder.replyTime.setText(getTimeFormatText(bean.getReplyTime()));
        holder.replyText.setText(bean.getReplyContent());

        final String commentNickName = bean.getCommentNickname();
        String replyContentStr = bean.getReplyContent();
        ss = new SpannableString("回复"+commentNickName
                +"："+replyContentStr);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#80202025")),2,
                commentNickName.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.replyText.setText(ss);
        //添加点击事件时，必须设置
        holder.replyText.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView;
    }

    private final class ViewHolder{
        public ImageView replyPhoto;
        public TextView replyName;
        public TextView replyText;
        public TextView replyTime;
    }

    /**
     * 时间差
     *
     */
    public  String getTimeFormatText(Date date) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年

        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
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
    }

}
