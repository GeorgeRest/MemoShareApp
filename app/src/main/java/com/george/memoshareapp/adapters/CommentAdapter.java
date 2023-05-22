package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.ReplyBean;
import com.george.memoshareapp.view.NoScrollListView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private int resourceId;
    private Context context;
    private List<CommentBean> list;
    private LayoutInflater inflater;
    public CommentAdapter(Context context, List<CommentBean> list, int resourceId){
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
        CommentBean bean = list.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            holder.commentItemImg = (ImageView)
                    convertView.findViewById(R.id.commentItemImg);
            holder.commentNickname = (TextView)
                    convertView.findViewById(R.id.commentNickname);
            holder.commentItemTime = (TextView)
                    convertView.findViewById(R.id.commentItemTime);
            holder.commentItemContent = (TextView)
                    convertView.findViewById(R.id.commentItemContent);
            holder.replyList = (NoScrollListView)
                    convertView.findViewById(R.id.replyList);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.commentItemImg.setImageResource(bean.getCommentUserPhoto());
        holder.commentNickname.setText(bean.getCommentUserName());
        holder.commentItemTime.setText(bean.getCommentTime());
        holder.commentItemContent.setText(bean.getCommentContent());

        ReplyAdapter adapter = new ReplyAdapter(context, bean.getReplyList(), R.layout.reply_item);
        holder.replyList.setAdapter(adapter);

        return convertView;
    }

    private final class ViewHolder{
        public ImageView commentItemImg;			//评论人图片
        public TextView commentNickname;			//评论人昵称
        public TextView commentItemTime;			//评论时间
        public TextView commentItemContent;			//评论内容
        public NoScrollListView replyList;			//评论回复列表
    }

    /**
     * 获取回复评论
     */
    public void getReplyComment(ReplyBean bean, int position){
        List<ReplyBean> rList = list.get(position).getReplyList();
        rList.add(rList.size(), bean);
    }

}
