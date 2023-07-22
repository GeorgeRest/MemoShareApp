package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ContactInfo;
import com.george.memoshareapp.view.AlbumFriendCheckBox;

import java.util.Arrays;
import java.util.List;
public class ContactListAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<ContactInfo> contactList;
    private boolean[] checkedItems;
    private OnContactsSelectedListener contactsSelectedListener;
    private HorizontalAdapter horizontalAdapter;

    private RecyclerView horizontalRecyclerView;

    public ContactListAdapter(Context context, List<ContactInfo> contactList, HorizontalAdapter horizontalAdapter, RecyclerView horizontalRecyclerView) {
        this.context = context;
        this.contactList = contactList;
        this.checkedItems = new boolean[contactList.size()];
        Arrays.fill(checkedItems, false);
        this.horizontalAdapter = horizontalAdapter;
        this.horizontalRecyclerView = horizontalRecyclerView;  // 添加这一行
    }

    public ContactListAdapter(Context context, List<ContactInfo> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.checkedItems = new boolean[contactList.size()];
        Arrays.fill(checkedItems, false);
    }
    public ContactListAdapter(Context context, List<ContactInfo> contactList, HorizontalAdapter horizontalAdapter) {
        this.context = context;
        this.contactList = contactList;
        this.checkedItems = new boolean[contactList.size()];
        Arrays.fill(checkedItems, false);
        this.horizontalAdapter = horizontalAdapter;  // 添加这一行
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

    public void setData(List<ContactInfo> data) {
        this.contactList = data;
        this.checkedItems = new boolean[contactList.size()];
        Arrays.fill(checkedItems, false);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contact_list, null);
            holder = new ViewHolder();
            holder.iv_photo = convertView.findViewById(R.id.iv_photo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_show_letter = convertView.findViewById(R.id.tv_show_letter);
            holder.checkbox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactInfo contact = contactList.get(position);
        holder.iv_photo.setImageResource(contact.getPicture());
        holder.tv_name.setText(contact.getName());
        holder.checkbox.setChecked(checkedItems[position]);

        int sectionForPosition = getSectionForPosition(position);
        int positionForSection = getPositionForSection(sectionForPosition);
        if (position == positionForSection) {
            holder.tv_show_letter.setVisibility(View.VISIBLE);
            holder.tv_show_letter.setText(contact.getFirstLetter());
        } else {
            holder.tv_show_letter.setVisibility(View.GONE);
        }



        holder.checkbox.setOnCheckedChangeListener(new AlbumFriendCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AlbumFriendCheckBox checkBox, boolean isChecked) {
                checkedItems[position] = isChecked;
                if (isChecked) {
                    horizontalAdapter.addContact(contact);  // 将选中的 contact 添加到 HorizontalAdapter 中
                } else {
                    horizontalAdapter.removeContact(contact);  // 将取消选中的 contact 从 HorizontalAdapter 中移除
                }
                if (contactsSelectedListener != null) {
                    contactsSelectedListener.onContactsSelected(checkedItems);
                }

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
            if (contactList.get(i).getFirstLetter().charAt(0) == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return contactList.get(position).getFirstLetter().charAt(0);
    }
    public interface OnContactsSelectedListener {
        void onContactsSelected(boolean[] selectedItems);
    }

    public void setOnContactsSelectedListener(OnContactsSelectedListener listener) {
        this.contactsSelectedListener = listener;
    }

    private static class ViewHolder {
        ImageView iv_photo;
        TextView tv_name;
        TextView tv_show_letter;
        AlbumFriendCheckBox checkbox;
    }
}
