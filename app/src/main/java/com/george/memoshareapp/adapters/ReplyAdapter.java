package com.george.memoshareapp.adapters;

import android.content.Context;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReplyAdapter extends BaseAdapter {

    private int resourceId;
    private List<ReplyBean> list;
    private LayoutInflater inflater;
    private SpannableString ss;
    private Context context;
    private Handler handler;
    private ReplyBean bean;
    private int replayId;
    private int commentbeanId;
    private int position;

    public ReplyAdapter(Context context, List<ReplyBean> list, int resourceId, Handler handler,int position) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
        this.handler = handler;
        this.position = position;
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
        bean = list.get(position);
        replayId = bean.getId();
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            holder.replyPhoto = (ImageView) convertView.findViewById(R.id.iv_reply_photo);
            holder.replyName = (TextView) convertView.findViewById(R.id.tv_reply_name);
            holder.replyTextAndTime = (TextView) convertView.findViewById(R.id.replyContentAndTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserManager userManager = new UserManager(context);
        //user1:回复人 user2:被回复人
        User user1 = userManager.findUserByPhoneNumber(bean.getReplyPhoneNumber());
        User user2 = userManager.findUserByPhoneNumber(bean.getCommentPhoneNumber());
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+user1.getHeadPortraitPath()).into(holder.replyPhoto);
        holder.replyName.setText(user1.getName());

        String content = bean.getReplyContent();
        String time = getTimeFormatText(bean.getReplyTime());
        SpannableString spannableString = new SpannableString(content + "   " + time);
        String commentNickName = user2.getName();
        ss = new SpannableString("回复" + commentNickName
                + "：" + spannableString);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#80202025")), 2,
                commentNickName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置评论时间的字体大小和颜色
        ss.setSpan(new RelativeSizeSpan(0.75f), commentNickName.length() + 2 + content.length() + 3, commentNickName.length() + 3 + content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 75% of original size
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#66202025")), commentNickName.length() + 2 + content.length() + 3, commentNickName.length() + 3 + content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // set color
        holder.replyTextAndTime.setText(ss);
        //添加点击事件时，必须设置
        holder.replyTextAndTime.setMovementMethod(LinkMovementMethod.getInstance());

        TextviewClickListener tcl = new TextviewClickListener(position);
        holder.replyTextAndTime.setOnClickListener(tcl);
        return convertView;
    }

    private final class ViewHolder {
        public ImageView replyPhoto;
        public TextView replyName;
        public TextView replyTextAndTime;
    }

    /**
     * 事件点击监听器
     */
    private class TextviewClickListener implements View.OnClickListener {
        private int replyPosition;

        public TextviewClickListener(int position) {
            this.replyPosition = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.replyContentAndTime:
//                    Cursor cursor = LitePal.findBySQL("select commentbean_id from replybean where id=?", String.valueOf(replayId));
//                    if (cursor != null && cursor.moveToFirst()) {
//                        int columnIndex = cursor.getColumnIndex("commentbean_id");
//                        commentbeanId = cursor.getInt(columnIndex);
//                        cursor.close();
//                    }
                    handler.sendMessage(handler.obtainMessage(1, replyPosition,position));
                    break;
            }
        }
    }

    /**
     * 时间差
     */
    public String getTimeFormatText(Date date) {
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
            if (r == 1) {
                return "昨天" + dateString1;
            } else {
                return dateString2;
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
    }

}
