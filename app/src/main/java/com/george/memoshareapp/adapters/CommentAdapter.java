package com.george.memoshareapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.view.NoScrollListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends BaseAdapter {

    private int resourceId;
    private Context context;
    private List<CommentBean> list;
    private LayoutInflater inflater;
    private Handler handler;

    public CommentAdapter(Context context, List<CommentBean> list, int resourceId, Handler handler){
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
        CommentBean bean = list.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            holder.commentItemImg = (ImageView)
                    convertView.findViewById(R.id.commentItemImg);
            holder.commentNickname = (TextView)
                    convertView.findViewById(R.id.commentNickname);
            holder.commentItemContentAndTime = (TextView)
                    convertView.findViewById(R.id.commentItemContentAndTime);
            holder.replyList = (NoScrollListView)
                    convertView.findViewById(R.id.replyList);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        UserManager userManager = new UserManager(context);
        User user = userManager.findUserByPhoneNumber(bean.getCommentUserPhoneNumber());
        // 设置头像
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+user.getHeadPortraitPath()).into(holder.commentItemImg);
        //设置评论人名字
        holder.commentNickname.setText(user.getName());

        String content = bean.getCommentContent();
        String time = getTimeFormatText(bean.getCommentTime());
        SpannableString spannableString = new SpannableString(content + "   " + time);

        // 设置评论时间的字体大小和颜色
        spannableString.setSpan(new RelativeSizeSpan(0.75f), content.length() + 3, content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 75% of original size
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#66202025")), content.length() + 3, content.length() + 3 + time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // set color
        holder.commentItemContentAndTime.setText(spannableString);


        ReplyAdapter adapter = new ReplyAdapter(context, bean.getReplyList(), R.layout.item_reply,handler,position);

        holder.replyList.setAdapter(adapter);

        //设置点击事件
        TextviewClickListener tcl = new TextviewClickListener(position);
        holder.commentItemContentAndTime.setOnClickListener(tcl);
        return convertView;
    }

    private final class ViewHolder{
        public ImageView commentItemImg;			//评论人图片
        public TextView commentNickname;			//评论人昵称
        public TextView commentItemContentAndTime;	//评论内容和时间
        public NoScrollListView replyList;			//评论回复列表
    }

    /**
     * 获取回复评论
     */
    public void getReplyComment(ReplyBean bean, int position){
        List<ReplyBean> rList = list.get(position).getReplyList();
        rList.add(rList.size(), bean);
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
                case R.id.commentItemContentAndTime:
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
    }
}
