package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.utils.PinyinFirstLetter;

import java.util.List;

public class ContactListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<User> contactList;

    public ContactListAdapter(Context context, List<User> contactList) {
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

    public void setData(List<User> data) {
        this.contactList = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_contact_list,null);
            holder = new ViewHolder();
            holder.iv_photo = convertView.findViewById(R.id.niv_photo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_show_letter = convertView.findViewById(R.id.tv_show_letter);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        User contact = contactList.get(position);
        // 获得头像
        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+contact.getHeadPortraitPath()).into(holder.iv_photo);    // 获取头像
        holder.tv_name.setText(contact.getName());

        //获得当前position是属于哪个分组
        int sectionForPosition = getSectionForPosition(position);
        //获得该分组第一项的position
        int positionForSection = getPositionForSection(sectionForPosition);
        //查看当前position是不是当前item所在分组的第一个item
        //如果是，则显示showLetter，否则隐藏
        if (position == positionForSection) {
            holder.tv_show_letter.setVisibility(View.VISIBLE);
            holder.tv_show_letter.setText(PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contact.getName())));
        } else {
            holder.tv_show_letter.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < contactList.size(); i++) {
            if (PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contactList.get(i).getName())).charAt(0) == sectionIndex) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public int getSectionForPosition(int position) {
        if (contactList != null && position < contactList.size()) {
            return PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contactList.get(position).getName())).charAt(0);
        }
        return -1; // or other value that makes sense in your context
        //return PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contactList.get(position).getName())).charAt(0);
    }

    private static class ViewHolder{
        ImageView iv_photo;
        TextView tv_name;
        TextView tv_show_letter;
    }
}