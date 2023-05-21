package com.george.memoshareapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.Fragment.AudioPlayerFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.utils.DateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "HomeWholeRecyclerViewAd";

    private Context mContext;
    private List<Post> mData;
    public AudioPlayerFragment fragment;
    private Map<ImageView, Fragment> buttonFragmentMap = new HashMap<>();
    private static HomeWholeRecyclerViewAdapter instance;
    private SharedPreferences.Editor editor;
    private ImageView currentPlayingImageView = null;
    private SharedPreferences sp;
    private boolean isLike;
    private String phoneNumber;

    public HomeWholeRecyclerViewAdapter() {
    }

    private List<Post> treePosition;

    public HomeWholeRecyclerViewAdapter(Context context, List<Post> data) {
        this.mContext = context;
        this.mData = data;
        instance = this;
    }

    public static HomeWholeRecyclerViewAdapter getInstance() {
        return instance;  // 创建一个静态方法来获取当前实例
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        sp = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");
        editor = sp.edit();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.record_one.setTag(holder);
        holder.record_two.setTag(holder);
        holder.record_three.setTag(holder);
        holder.like.setTag(holder);
        Post post = mData.get(position);
        holder.bind(post);
        String phoneNumber = post.getPhoneNumber();
        String name = phoneNumber.substring(0, 5);
        String publishedTime = post.getPublishedTime();
        String location = post.getLocation();
        String publishedText = post.getPublishedText();
        holder.recordings = post.getRecordings();

        List<String> photoCachePath = post.getPhotoCachePath();

        holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(photoCachePath.size())));
        HomePhotoRecyclerViewAdapter innerAdapter = new HomePhotoRecyclerViewAdapter(photoCachePath, post, mContext);
        holder.innerRecyclerView.setAdapter(innerAdapter);
        holder.tv_username.setText(name);
        holder.tv_time.setText(DateFormat.getMessageDate(publishedTime));
        holder.tv_location.setText(location);
        holder.tv_content.setText(publishedText);
        if (holder.recordings != null && !holder.recordings.isEmpty()) {
            holder.record_one.setVisibility(View.GONE);
            holder.record_two.setVisibility(View.GONE);
            holder.record_three.setVisibility(View.GONE);
            switch (holder.recordings.size()) {
                case 3:
                    holder.record_three.setVisibility(View.VISIBLE);
                    // no break here
                case 2:
                    holder.record_two.setVisibility(View.VISIBLE);
                    // no break here
                case 1:
                    holder.record_one.setVisibility(View.VISIBLE);
            }
        } else {
            holder.record_one.setVisibility(View.GONE);
            holder.record_two.setVisibility(View.GONE);
            holder.record_three.setVisibility(View.GONE);
        }
        holder.innerRecyclerView.setAdapter(innerAdapter);

//        设置头像，需要contact（@人名所对应的图片，相关adapter未完成）
//        List<String> contacts = post.getContacts();
//        holder.rv_head_image.setLayoutManager(new GridLayoutManager(mContext, 1));
//        HomePageHeadImageAdapter headImageAdapter = new HomePageHeadImageAdapter(contacts);
//        holder.rv_head_image.setAdapter(headImageAdapter);


        treePosition = new DisplayManager(mContext).showMemoryTree(post.getLatitude(), post.getLongitude());

        if (treePosition != null && treePosition.size() > 0) {

            if (post.getPhoneNumber().equals(phoneNumber)) {
                holder.rl_layout.setBackgroundResource(R.drawable.cardview_bg);
//            holder.rv_myself_image.setLayoutManager(new GridLayoutManager(mContext, 10));
                holder.rv_myself_image.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomePageBottomAdapter homePageBottomAdapter = new HomePageBottomAdapter(mContext,treePosition);
                holder.rv_myself_image.setAdapter(homePageBottomAdapter);
                holder.image_view1.setVisibility(View.VISIBLE);
                holder.rv_myself_image.setVisibility(View.VISIBLE);
                holder.image_view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.rv_myself_image.getVisibility() == View.GONE) {
                            holder.rv_myself_image.setVisibility(View.VISIBLE);
                            holder.image_view1.setText("那时那刻");
                        } else {
                            holder.rv_myself_image.setVisibility(View.GONE);
                            holder.image_view1.setText("收起");
                        }
                    }
                });
            } else {
                holder.rl_layout.setBackgroundColor(Color.WHITE);
                holder.image_view1.setVisibility(View.GONE);
                holder.rv_myself_image.setVisibility(View.GONE);
//                holder.sv_bottom.setVisibility(View.GONE);

            }

        } else {
            holder.rl_layout.setBackgroundColor(Color.WHITE);
//            holder.sv_bottom.setVisibility(View.GONE);
            holder.image_view1.setVisibility(View.GONE);
            holder.rv_myself_image.setVisibility(View.GONE);
        }


        holder.ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        Post post = mData.get(holder.getAdapterPosition());
        switch (v.getId()) {
            case R.id.like:
                isLike = sp.getBoolean(post.getId() + ":" + phoneNumber, false);
                isLike = !isLike;
                if (isLike) {

                    holder.like.setBackground(mContext.getResources().getDrawable(R.drawable.like_click));
                } else {
                    holder.like.setBackground(mContext.getResources().getDrawable(R.drawable.like));
                }
                editor.putBoolean(post.getId() + ":" + phoneNumber, isLike);
                editor.apply();
                break;
        }
        if (holder.recordings != null && !holder.recordings.isEmpty()) {
            switch (v.getId()) {
                case R.id.record_one:
                    if (holder.recordings.size() > 0) {
                        handleClick(holder.recordings.get(0).getRecordCachePath(), holder.record_one);
                        holder.record_one.setImageResource(R.drawable.record_bg_click);
                    }
                    break;
                case R.id.record_two:
                    if (holder.recordings.size() > 1) {
                        handleClick(holder.recordings.get(1).getRecordCachePath(), holder.record_two);
                        holder.record_two.setImageResource(R.drawable.record_bg_click);
                    }
                    break;
                case R.id.record_three:
                    if (holder.recordings.size() > 2) {
                        handleClick(holder.recordings.get(2).getRecordCachePath(), holder.record_three);
                        holder.record_three.setImageResource(R.drawable.record_bg_click);
                    }
                    break;

            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView like;
        TextView tv_username;
        TextView tv_time;
        TextView tv_location;
        TextView tv_content;
        RecyclerView innerRecyclerView;
        List<Recordings> recordings;
        ImageView record_one;
        ImageView record_two;
        ImageView record_three;
        public AudioPlayerFragment fragment;

        RelativeLayout rl_layout;
        LinearLayout ll_head;
        RecyclerView rv_myself_image;
        RecyclerView rv_head_image;
        TextView image_view1;
        ScrollView sv_bottom;
        CardView cv_layout;

        ViewHolder(View itemView) {
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.rv_images);
            rv_head_image = itemView.findViewById(R.id.rv_head_image);
            rv_myself_image = itemView.findViewById(R.id.image_recycler_view);
            cv_layout = itemView.findViewById(R.id.cv_layout);
            sv_bottom = itemView.findViewById(R.id.sv_bottom);

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_content = itemView.findViewById(R.id.tv_content);
            image_view1 = itemView.findViewById(R.id.image_view1);
            record_one = itemView.findViewById(R.id.record_one);
            record_two = itemView.findViewById(R.id.record_two);
            record_three = itemView.findViewById(R.id.record_three);
            like = itemView.findViewById(R.id.like);
            like.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_one.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_two.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_three.setOnClickListener(HomeWholeRecyclerViewAdapter.this);

            ll_head = itemView.findViewById(R.id.ll_head);
            rl_layout = itemView.findViewById(R.id.rl_layout);
        }

        void bind(Post post) {
            isLike = sp.getBoolean(post.getId() + ":" + phoneNumber, false);
            if (isLike) {
                like.setBackground(mContext.getResources().getDrawable(R.drawable.like_click));
            } else {
                like.setBackground(mContext.getResources().getDrawable(R.drawable.like));
            }
        }
    }


    private void handleClick(String recordPath, ImageView iv) {
        if (currentPlayingImageView != null && currentPlayingImageView != iv) {
            currentPlayingImageView.setImageResource(R.drawable.record_homepage);
        }
        if (fragment == null) {
            currentPlayingImageView = iv;
            addFragment(recordPath, iv);
        } else {
            AudioPlayerFragment bt_fragment = (AudioPlayerFragment) buttonFragmentMap.get(iv);
            if (fragment != bt_fragment) {
                fragment.stopPlayback();
                FragmentTransaction fragmentTransaction = ((HomePageActivity) this.mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragment = null;
                currentPlayingImageView = iv;
                addFragment(recordPath, iv);
            } else {
                fragment.togglePlayback();
            }
        }
    }

    private void addFragment(String recordPath, ImageView iv) {
        AudioPlayerFragment.AUDIO_FILE_PATH = recordPath;
        fragment = new AudioPlayerFragment();
        buttonFragmentMap.put(iv, fragment);
        FragmentTransaction fragmentTransaction = ((HomePageActivity) this.mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.record_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    public void addData(Post newData) {
        this.mData.add(0, newData);  // 添加新的数据到列表的最前面
        notifyItemInserted(0);  // 通知 adapter 在位置 0 插入了一条数据

    }

    private int calculateSpanCount(int itemCount) {
        return itemCount > 1 ? 2 : 1;
    }

    public void resetFragment() {
        if (fragment != null) {
            fragment.stopPlayback();
            HomePageActivity mContext = (HomePageActivity) this.mContext;
            FragmentTransaction fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            fragment = null;
        }
    }
    public void resetPlayingButton() {
        if (currentPlayingImageView != null) {
            currentPlayingImageView.setImageResource(R.drawable.record_homepage);  // Use your own default image here
            currentPlayingImageView = null;
        }
    }
}
