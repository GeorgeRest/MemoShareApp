package com.george.memoshareapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;

public class CalendarMultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @Override
    public int getItemViewType(int position) {
        // 根据position或数据源来判断当前位置对应的Item类型，并返回相应的类型标识
        if (position == 0) {
            return 0; // 类型1
        } else {
            return 1; // 类型2
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) { // 类型1的布局
            view = inflater.inflate(R.layout.item_event_reminder, parent, false);
            return new RemindViewHolder(view);

        } else {            // 类型2的布局
            //view = inflater.inflate(R.layout.item_type2, parent, false);
            view = null;
            return new Type2ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RemindViewHolder) { // 类型1的ViewHolder
            RemindViewHolder remindHolder = (RemindViewHolder) holder;
            // 绑定类型1的数据


        } else if (holder instanceof Type2ViewHolder) { // 类型2的ViewHolder
            Type2ViewHolder type2Holder = (Type2ViewHolder) holder;
            // 绑定类型2的数据

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RemindViewHolder extends RecyclerView.ViewHolder {

        public RemindViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class Type2ViewHolder extends RecyclerView.ViewHolder {

        public Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
