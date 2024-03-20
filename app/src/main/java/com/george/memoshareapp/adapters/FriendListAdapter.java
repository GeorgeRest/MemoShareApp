package com.george.memoshareapp.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.ChinesetoPinyin;
import com.george.memoshareapp.utils.PinyinFirstLetter;
import com.george.memoshareapp.view.AlbumFriendCheckBox;
import com.george.memoshareapp.view.NiceImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<User> contactList;
    private List<User> alreadyExitUserList;
    private HorizontalAdapter horizontalAdapter;
    private boolean[] checkedItems;
    private RecyclerView horizontalRecyclerView;
    private User contact;
    private OnContactsSelectedListener contactsSelectedListener;
    private NiceImageView iv_photo;


    public FriendListAdapter(Context context, List<User> contactList) {
        this.context = context;
        this.contactList = contactList;
    }
    public FriendListAdapter(Context context, List<User> contactList, HorizontalAdapter horizontalAdapter, RecyclerView horizontalRecyclerView) {
        this.context = context;
        this.contactList = contactList;
        this.checkedItems = new boolean[contactList.size()];
        Arrays.fill(checkedItems, false);
        this.horizontalAdapter = horizontalAdapter;
        this.horizontalRecyclerView = horizontalRecyclerView;
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
        this.checkedItems = new boolean[data.size()];
        Arrays.fill(checkedItems, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_invite_friend_list,null);
            holder = new ViewHolder();
            holder.iv_photo =  convertView.findViewById(R.id.iv_photo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_show_letter = convertView.findViewById(R.id.tv_show_letter);
            holder.checkbox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        contact = contactList.get(position);


        String imagePath = AppProperties.SERVER_MEDIA_URL+ contact.getHeadPortraitPath();
        if(imagePath != null && !imagePath.isEmpty()) {
            int cornerRadius = 20;
            Glide.with(context)
                    .load(imagePath) // 图片的 URL
                    .transform(new RoundedCorners(cornerRadius)) // 设置圆角
                    .into(holder.iv_photo); // 要加载到的 ImageView

//            Glide.with(context).load(imagePath).into(holder.iv_photo);
        }




        holder.tv_name.setText(contact.getName());
        holder.checkbox.setChecked(checkedItems[position]);

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

        holder.checkbox.setOnCheckedChangeListener(new AlbumFriendCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AlbumFriendCheckBox checkBox, boolean isChecked) {
                checkedItems[position] = isChecked;
                if (contactsSelectedListener != null) {
                    contactsSelectedListener.onContactsSelected(checkedItems);
                }
                updateHorizontalAdapter(); // 调用更新方法
                if (horizontalAdapter.getItemCount() > 5) {
                    horizontalRecyclerView.scrollToPosition(horizontalAdapter.getItemCount() - 1);  // 滚动到最后一个item
                }
            }
        });
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

    public void updateHorizontalAdapter() {
        horizontalAdapter.setContacts(getSelectedContacts());
        horizontalAdapter.notifyDataSetChanged();
    }
    public void checkUsers(List<User> userList) {
        for (User u:contactList) {
            for (User exit:userList) {
                if ((u.getPhoneNumber()).equals(exit.getPhoneNumber())){
                    int index = contactList.indexOf(userList);
                    if (index != -1) {
                        checkedItems[index] = true;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }



    private List<User> getSelectedContacts() {
        List<User> selectedContacts = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            if (checkedItems[i]) {
                selectedContacts.add(contactList.get(i));
            }
        }
        return selectedContacts;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (contactList != null && position < contactList.size()) {
            return PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contactList.get(position).getName())).charAt(0);
        }
        return -1; // or other value that makes sense in your context
        //return PinyinFirstLetter.getFirstLetter(ChinesetoPinyin.getPinyin(contactList.get(position).getName())).charAt(0);
    }
    public interface OnContactsSelectedListener {
        void onContactsSelected(boolean[] selectedItems);
    }
    public void setOnContactsSelectedListener(OnContactsSelectedListener listener) {
        this.contactsSelectedListener = listener;
    }
    private static class ViewHolder{
        ImageView iv_photo;
        TextView tv_name;
        TextView tv_show_letter;
        AlbumFriendCheckBox checkbox;
    }
}