package com.george.memoshareapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
    private Handler handler;

    public ReplyAdapter(Context context, List<ReplyBean> list, int resourceId,Handler handler){
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
        this.handler = handler;
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
//            holder.replyTime= (TextView)convertView.findViewById(R.id.tv_replyTime);
            holder.replyTextAndTime= (TextView)convertView.findViewById(R.id.replyContentAndTime);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
    }
        holder.replyPhoto.setImageResource(bean.getReplyUserPhoto());
        holder.replyName.setText(bean.getReplyNickname());
//        holder.replyTime.setText(getTimeFormatText(bean.getReplyTime()));

        String content = bean.getReplyContent();
        String time = getTimeFormatText(bean.getReplyTime());
        SpannableString spannableString = new SpannableString(content + "   " + time);
        String commentNickName = bean.getCommentNickname();
        ss = new SpannableString("回复"+commentNickName
                +"："+spannableString);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#80202025")),2,
                commentNickName.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置评论时间的字体大小和颜色
        ss.setSpan(new RelativeSizeSpan(0.75f), commentNickName.length()+2+content.length() + 3, commentNickName.length()+3+content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 75% of original size
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#66202025")), commentNickName.length()+2+content.length() + 3, commentNickName.length()+3+content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // set color
        holder.replyTextAndTime.setText(ss);
        //添加点击事件时，必须设置
        holder.replyTextAndTime.setMovementMethod(LinkMovementMethod.getInstance());

        TextviewClickListener tcl = new TextviewClickListener(position);
        holder.replyTextAndTime.setOnClickListener(tcl);
        return convertView;
    }

    private final class ViewHolder{
        public ImageView replyPhoto;
        public TextView replyName;
        public TextView replyTextAndTime;
//        public TextView replyTime;
    }

    /**
     * 事件点击监听器
     */
    private final class TextviewClickListener implements View.OnClickListener {
        private int position;
        public TextviewClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.replyContentAndTime:
                    handler.sendMessage(handler.obtainMessage(10, position));
                    break;
            }
        }
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
