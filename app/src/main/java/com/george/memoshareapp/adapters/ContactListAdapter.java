package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ContactInfo;

import java.util.List;

public class ContactListAdapter extends BaseAdapter {

    private Context context;
    private List<ContactInfo> contactList;

    public ContactListAdapter(Context context, List<ContactInfo> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.contact_list_item,null);
            holder = new ViewHolder();
            holder.iv_photo = convertView.findViewById(R.id.iv_photo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 图片绑定 假数据
        holder.iv_photo.setImageResource(this.contactList.get(position).getPicture());
        holder.tv_name.setText(this.contactList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder{
        ImageView iv_photo;
        TextView tv_name;
    }
}


