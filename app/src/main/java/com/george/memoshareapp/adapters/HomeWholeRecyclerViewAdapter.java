package com.george.memoshareapp.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.Fragment.AudioPlayerFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.DetailActivity;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.activities.NewPersonPageActivity;
import com.george.memoshareapp.beans.ContactInfo;
import com.george.memoshareapp.beans.ImageParameters;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.events.PostLikeUpdateEvent;
import com.george.memoshareapp.manager.DisplayManager;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.utils.DateFormat;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWholeRecyclerViewAdapter extends RecyclerView.Adapter<HomeWholeRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Post> mData;
    private List<Uri> photoUri;
    public AudioPlayerFragment fragment;
    private Map<ImageView, Fragment> buttonFragmentMap = new HashMap<>();
    private static HomeWholeRecyclerViewAdapter instance;
    private SharedPreferences.Editor editor;
    private ImageView currentPlayingImageView = null;
    private SharedPreferences sp;
    private boolean isLike;
    private String phoneNumber;
    private List<ContactInfo> contactPicture = new ArrayList<>();
    private Map<String, Integer> nameToPictureMap = new HashMap<>();
    private List<String> contactName = new ArrayList<>();
    private Post post;
    private List<Post> treePosition;
    private User postUser;

    public HomeWholeRecyclerViewAdapter(Context context, List<Post> data) {
        this.mContext = context;
        this.mData = data;
        instance = this;
        sp = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");
        editor = sp.edit();
        initDate();
    }

    public static HomeWholeRecyclerViewAdapter getInstance() {
        return instance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_whole_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        post = mData.get(position);

        setUpHolderFields(holder, post);
        setUpHolderRecyclerView(holder, post, position);
        setUpRecordings(holder);
        setUpContactNameAndPicture(holder, post);
        adjustLayout(holder, position);
        nameTimeLocationContent(holder, post);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        post = mData.get(holder.getAdapterPosition());
        switch (v.getId()) {
            case R.id.like:

                int postId = (int) post.getId();
                isLike = sp.getBoolean(post.getId() + ":" + phoneNumber, false);
                isLike = !isLike;
                if (isLike) {
                    holder.like.setImageResource(R.drawable.like_press);
                    new DisplayManager().updateLikeCount(postId, phoneNumber, true);
                    PostLikeUpdateEvent event = new PostLikeUpdateEvent(post, isLike);
                    EventBus.getDefault().post(event);
                } else {
                    holder.like.setImageResource(R.drawable.like);
                    new DisplayManager().updateLikeCount(postId, phoneNumber, false);

                    PostLikeUpdateEvent event = new PostLikeUpdateEvent(post, isLike);
                    EventBus.getDefault().post(event);
                }
                editor.putBoolean(post.getId() + ":" + phoneNumber, isLike);
                editor.apply();
                break;
            case R.id.chat:
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("shouldCheckComments", true);
                intent.putExtra("post", post);
                mContext.startActivity(intent);
                break;
            case R.id.homewhole_iv_head_image_1:
                Intent intent1 = new Intent(mContext, NewPersonPageActivity.class);

                intent1.putExtra("user", post.getUser());
                mContext.startActivity(intent1);
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

    private void nameTimeLocationContent(ViewHolder holder, Post post) {
        postUser = post.getUser();
        if (postUser.getHeadPortraitPath() != null) {
            Glide.with(mContext).load(postUser.getHeadPortraitPath()).into(holder.iv_head_image_1);
        } else {
            holder.iv_head_image_1.setImageResource(R.mipmap.app_icon);
        }
        String name = postUser.getName();
        String publishedTime = post.getPublishedTime();
        String location = post.getLocation();
        String publishedText = post.getPublishedText();
        holder.tv_username.setText(name);
        holder.tv_time.setText(DateFormat.getMessageDate(publishedTime));
        holder.tv_location.setText(location);
        holder.tv_content.setText(publishedText);
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


    private void setUpHolderFields(ViewHolder holder, Post post) {
        holder.bind(post);
        holder.record_one.setTag(holder);
        holder.record_two.setTag(holder);
        holder.record_three.setTag(holder);
        holder.like.setTag(holder);
        holder.chat.setTag(holder);
        holder.iv_head_image_1.setTag(holder);

        holder.bind(post);
    }

    private void setUpHolderRecyclerView(ViewHolder holder, Post post, int position) {
        List<ImageParameters> imageParameters = post.getImageParameters();
        if (imageParameters == null || imageParameters.size() == 0) {
            holder.innerRecyclerView.setVisibility(View.GONE);
        } else {
            holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(mContext, calculateSpanCount(imageParameters.size())));
        }

        HomePhotoRecyclerViewAdapter innerAdapter;
        if (photoUri != null) {
            innerAdapter = new HomePhotoRecyclerViewAdapter(imageParameters, post, mContext, photoUri, position);
        } else {
            innerAdapter = new HomePhotoRecyclerViewAdapter(imageParameters, post, mContext, position);
        }

        holder.innerRecyclerView.setAdapter(innerAdapter);
    }

    private void setUpRecordings(ViewHolder holder) {
        holder.bind(post);
        holder.recordings = post.getRecordings();
        if (holder.recordings != null && !holder.recordings.isEmpty()) {
            holder.record_one.setVisibility(View.GONE);
            holder.record_two.setVisibility(View.GONE);
            holder.record_three.setVisibility(View.GONE);
            switch (holder.recordings.size()) {
                case 3:
                    holder.record_three.setVisibility(View.VISIBLE);
                case 2:
                    holder.record_two.setVisibility(View.VISIBLE);
                case 1:
                    holder.record_one.setVisibility(View.VISIBLE);
            }
        } else {
            holder.record_one.setVisibility(View.GONE);
            holder.record_two.setVisibility(View.GONE);
            holder.record_three.setVisibility(View.GONE);
        }
    }

    private void setUpContactNameAndPicture(ViewHolder holder, Post post) {
        holder.bind(post);
        contactName = post.getContacts();
        for (ContactInfo info : contactPicture) {
            nameToPictureMap.put(info.getName(), info.getPicture());
        }
        handleContacts(holder);
    }

    private void handleContacts(ViewHolder holder) {
        holder.bind(post);
        holder.iv_head_image_2.setVisibility(View.GONE);
        holder.iv_head_image_3.setVisibility(View.GONE);
        holder.tv_head_out_number.setVisibility(View.GONE);
        if (contactName != null && contactName.size() > 0) {
            switch (contactName.size()) {
                default:
                    holder.tv_head_out_number.setVisibility(View.VISIBLE);
                    holder.tv_head_out_number.setText("+" + (contactName.size() - 2));
                case 2:
                    if (contactName.get(1) != null) {
                        holder.iv_head_image_3.setVisibility(View.VISIBLE);
                        holder.iv_head_image_3.setImageResource(nameToPictureMap.get(contactName.get(1)));
                    }
                case 1:
                    if (contactName.get(0) != null) {
                        holder.iv_head_image_2.setVisibility(View.VISIBLE);
                        holder.iv_head_image_2.setImageResource(nameToPictureMap.get(contactName.get(0)));
                    }
            }
        } else {
            holder.iv_head_image_2.setVisibility(View.GONE);
            holder.iv_head_image_3.setVisibility(View.GONE);
        }
    }

    private void adjustLayout(ViewHolder holder, int position) {
        holder.bind(post);
        treePosition = new DisplayManager(mContext).showMemoryTree(post.getLatitude(), post.getLongitude());
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.cv_layout.getLayoutParams();
        layoutParams.bottomMargin = 0;
        holder.cv_layout.setLayoutParams(layoutParams);

        if (treePosition != null && treePosition.size() > 0) {
            setUpMemoryTree(holder, position, layoutParams);
        } else {
            holder.rl_layout.setBackgroundColor(Color.WHITE);
            holder.image_view1.setVisibility(View.GONE);
            holder.rv_myself_image.setVisibility(View.GONE);
            layoutParams.bottomMargin = 5;
            holder.cv_layout.setLayoutParams(layoutParams);
        }

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpMemoryTree(ViewHolder holder, int position, ViewGroup.MarginLayoutParams layoutParams) {
        holder.bind(post);
        if (post.getPhoneNumber().equals(phoneNumber)) {
            layoutParams.bottomMargin = 0;
            holder.cv_layout.setLayoutParams(layoutParams);
            holder.rl_layout.setBackgroundResource(R.drawable.cardview_bg);
            holder.rv_myself_image.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            HomePageBottomAdapter homePageBottomAdapter = new HomePageBottomAdapter(mContext, treePosition);
            holder.rv_myself_image.setAdapter(homePageBottomAdapter);
            holder.image_view1.setVisibility(View.VISIBLE);
            holder.image_view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.rv_myself_image.getVisibility() == View.GONE) {
                        holder.rv_myself_image.setVisibility(View.VISIBLE);
                        holder.image_view1.setText("收起");
                    } else {
                        holder.rv_myself_image.setVisibility(View.GONE);
                        holder.image_view1.setText("那时那刻");
                    }
                }
            });
        } else {
            holder.rl_layout.setBackgroundColor(Color.WHITE);
            holder.image_view1.setVisibility(View.GONE);
            holder.rv_myself_image.setVisibility(View.GONE);
            layoutParams.bottomMargin = 5;
            holder.cv_layout.setLayoutParams(layoutParams);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView chat;
        ImageView like;
        TextView tv_username;
        TextView tv_time;
        TextView tv_location;
        TextView tv_content;
        LinearLayout ll_head_images;
        RelativeLayout rl_head_images;
        TextView tv_head_out_number;
        ImageView iv_head_image_3;
        ImageView iv_head_image_2;
        ImageView iv_head_image_1;

        RecyclerView innerRecyclerView;
        List<Recordings> recordings;
        ImageView record_one;
        ImageView record_two;
        ImageView record_three;
        public AudioPlayerFragment fragment;

        RelativeLayout rl_layout;
        ConstraintLayout ll_head;
        RecyclerView rv_myself_image;
        RecyclerView rv_head_image;
        TextView image_view1;
        ScrollView sv_bottom;
        CardView cv_layout;
        CardView card_view_head;

        ViewHolder(View itemView) {
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.rv_images);
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
            chat = itemView.findViewById(R.id.chat);
            chat.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            like.setOnClickListener(HomeWholeRecyclerViewAdapter.this);

            record_one.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_two.setOnClickListener(HomeWholeRecyclerViewAdapter.this);
            record_three.setOnClickListener(HomeWholeRecyclerViewAdapter.this);

            card_view_head = (CardView) itemView.findViewById(R.id.card_view_head);
            iv_head_image_1 = (ImageView) itemView.findViewById(R.id.homewhole_iv_head_image_1);
            iv_head_image_2 = (ImageView) itemView.findViewById(R.id.iv_head_image_2);
            iv_head_image_3 = (ImageView) itemView.findViewById(R.id.iv_head_image_3);
            tv_head_out_number = (TextView) itemView.findViewById(R.id.tv_head_out_number);
            rl_head_images = (RelativeLayout) itemView.findViewById(R.id.rl_head_images);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            ll_head_images = (LinearLayout) itemView.findViewById(R.id.ll_head_images);
            iv_head_image_1.setOnClickListener(HomeWholeRecyclerViewAdapter.this);

        }

        void bind(Post post) {
            isLike = sp.getBoolean(post.getId() + ":" + phoneNumber, false);
            if (isLike) {
                like.setImageResource(R.drawable.like_press);
            } else {
                like.setImageResource(R.drawable.like);
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


    public void addData(Post newData, List<Uri> photoUri) {
        this.mData.add(0, newData);  // 添加新的数据到列表的最前面
        this.photoUri = photoUri;
        notifyItemInserted(0);
        // 通知 adapter 在位置 0 插入了一条数据
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

    private void initDate() {
        contactPicture.add(new ContactInfo("张三", R.mipmap.photo_1));
        contactPicture.add(new ContactInfo("李潇", R.mipmap.photo_2));
        contactPicture.add(new ContactInfo("唐莉", R.mipmap.photo_3));
        contactPicture.add(new ContactInfo("程思迪", R.mipmap.photo_4));
        contactPicture.add(new ContactInfo("Audss", R.mipmap.photo_5));
        contactPicture.add(new ContactInfo("王五", R.mipmap.photo_6));
        contactPicture.add(new ContactInfo("CC", R.mipmap.photo_7));
        contactPicture.add(new ContactInfo("张明敏", R.mipmap.photo_8));
        contactPicture.add(new ContactInfo("lilies", R.mipmap.photo_9));
        contactPicture.add(new ContactInfo("大师", R.mipmap.photo_10));
        contactPicture.add(new ContactInfo("历史老师", R.mipmap.photo_2));
        contactPicture.add(new ContactInfo("Kato", R.mipmap.photo_7));
        contactPicture.add(new ContactInfo("seven", R.mipmap.photo_5));
        contactPicture.add(new ContactInfo("吴仪", R.mipmap.photo_1));
        contactPicture.add(new ContactInfo("李宏", R.mipmap.photo_3));
        contactPicture.add(new ContactInfo("高倩倩", R.mipmap.photo_10));
        contactPicture.add(new ContactInfo("福福", R.mipmap.photo_4));
        contactPicture.add(new ContactInfo("小庞", R.mipmap.photo_9));
        contactPicture.add(new ContactInfo("***", R.mipmap.photo_6));
    }

}