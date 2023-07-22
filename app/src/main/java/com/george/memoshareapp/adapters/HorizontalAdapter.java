package com.george.memoshareapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.ContactInfo;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private List<ContactInfo> contacts;

    public HorizontalAdapter(List<ContactInfo> contacts) {
        this.contacts = contacts;
    }

    public void addContact(ContactInfo contact) {
        contacts.add(contact);
        notifyDataSetChanged();

    }
    public void removeContact(ContactInfo contact) {
        contacts.remove(contact);
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_view, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public List<ContactInfo> getContacts() {
        return contacts;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactInfo contact = contacts.get(position);
        holder.ivPhoto.setImageResource(contact.getPicture()); // 这里是假设你的图片是一个资源ID
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;  // 这个ImageView用来显示图片

        public ViewHolder(View view) {
            super(view);
            ivPhoto = view.findViewById(R.id.horizontal_recycler_view_iv_photo); // 假设你的ImageView的ID是iv_photo
        }
    }


}
