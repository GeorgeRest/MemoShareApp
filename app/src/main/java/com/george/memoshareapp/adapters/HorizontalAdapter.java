package com.george.memoshareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {
    private Context context;

    private List<User> contacts;

    public HorizontalAdapter(List<User> contacts) {
        this.contacts = contacts;
    }

    public void addContact(User contact) {
        contacts.add(contact);
        notifyDataSetChanged();

    }
    public void removeContact(User contact) {
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

    public List<User> getContacts() {
        return contacts;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User contact = contacts.get(position);
        Glide.with(context)
                .load(contact.getHeadPortraitPath()) // 图片的 URL
                .into(holder.ivPhoto); // 要加载到的 ImageView


    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;  // 这个ImageView用来显示图片

        public ViewHolder(View view) {
            super(view);
            ivPhoto = view.findViewById(R.id.horizontal_recycler_view_iv_photo); // 假设你的ImageView的ID是iv_photo
        }
    }


}
