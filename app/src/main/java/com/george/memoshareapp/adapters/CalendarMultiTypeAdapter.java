package com.george.memoshareapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Remind;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.manager.UserManager;

import java.util.List;

public class CalendarMultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private  List<Remind> remindList;
    private String myPhoneNumber;
    private int type;
    public CalendarMultiTypeAdapter(Context context, List<Remind> remindList,String myPhoneNumber,int type){
        this.context = context;
        this.remindList = remindList;
        this.myPhoneNumber = myPhoneNumber;
        this.type = type;
    };

    @Override
    public int getItemViewType(int position) {
        // 根据position或数据源来判断当前位置对应的Item类型，并返回相应的类型标识
        if (type == 1) {
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
            Remind remind = remindList.get(position);
            if(remind.getReminderUserPhoneNumber().equals(myPhoneNumber)){
                UserManager userManager = new UserManager(context);
                User remindedUser= userManager.findUserByPhoneNumber(remind.getRemindedUserPhoneNumber());
                String name = remindedUser.getName();
                remindHolder.person.setText("(提醒"+name+")");
            }
            if(remind.getRemindedUserPhoneNumber().equals(myPhoneNumber)){
                UserManager userManager = new UserManager(context);
                User reminderUser= userManager.findUserByPhoneNumber(remind.getReminderUserPhoneNumber());
                String name = reminderUser.getName();
                remindHolder.person.setText("(来自"+name+")");
            }
            remindHolder.content.setText(remind.getRemindContent());
            remindHolder.time.setText(remind.getRemindTime());
            remindHolder.interval.setText(remind.getRemindInterval());
            remindHolder.note.setText(remind.getRemindNote());


        } else if (holder instanceof Type2ViewHolder) { // 类型2的ViewHolder
            Type2ViewHolder type2Holder = (Type2ViewHolder) holder;
            // 绑定类型2的数据

        }
    }

    @Override
    public int getItemCount() {
        return remindList.size();
    }

    public class RemindViewHolder extends RecyclerView.ViewHolder {

        TextView person;
        TextView content;
        TextView time;
        TextView interval;
        TextView note;
        public RemindViewHolder(@NonNull View itemView) {
            super(itemView);
            person = itemView.findViewById(R.id.person);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            interval = itemView.findViewById(R.id.interval);
            note = itemView.findViewById(R.id.note);
        }

    }

    public class Type2ViewHolder extends RecyclerView.ViewHolder {

        public Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
