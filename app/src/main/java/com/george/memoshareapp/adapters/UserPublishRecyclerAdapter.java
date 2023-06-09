package com.george.memoshareapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.george.memoshareapp.Fragment.AudioPublishPlayerFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.utils.UserDateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPublishRecyclerAdapter extends RecyclerView.Adapter<UserPublishRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private List<Post> postList;
    private Context context;
    private Post post;
    private ImageView currentPlayingImageView = null;
    public AudioPublishPlayerFragment fragment;
    private Map<ImageView, Fragment> buttonFragmentMap = new HashMap<>();
    private static UserPublishRecyclerAdapter instance;

    public UserPublishRecyclerAdapter(Context context, List<Post> postList) {
        this.postList = postList;
        this.context = context;
        instance = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_publish_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_user_publish_recorder.setTag(holder);
        Post post = postList.get(position);
        holder.recordings = post.getRecordings();

        String formattedDate = UserDateFormat.formatMessageDate(post.getPublishedTime());

        holder.tv_user_publish_date.setVisibility(View.GONE);
        holder.tv_user_publish_month.setVisibility(View.GONE);
        holder.tv_user_publish_twoWord.setVisibility(View.GONE);
        holder.rl_user_publish_time.setBackground(null);

        if (position > 0) {
            Post postBefore = postList.get(position - 1);
            String formattedDateBefore = UserDateFormat.formatMessageDate(postBefore.getPublishedTime());

            if (formattedDate.equals(formattedDateBefore)) {
                holder.tv_user_publish_date.setVisibility(View.GONE);
                holder.tv_user_publish_month.setVisibility(View.GONE);
                holder.tv_user_publish_twoWord.setVisibility(View.GONE);
                holder.rl_user_publish_time.setBackground(null);
            } else {
                if (formattedDate.equals("今天")) {
                    holder.tv_user_publish_twoWord.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_twoWord.setText("今天");
                } else if (formattedDate.equals("昨天")) {
                    holder.tv_user_publish_twoWord.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_twoWord.setText("昨天");
                } else {
                    holder.tv_user_publish_date.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_month.setVisibility(View.VISIBLE);
                    holder.rl_user_publish_time.setBackground(context.getResources().getDrawable(R.drawable.user_date_line));
                    String[] dateParts = formattedDate.split("月");
                    String month = dateParts[0];
                    String day = dateParts[1].replace("日", "");

                    holder.tv_user_publish_date.setText(day);
                    holder.tv_user_publish_month.setText(String.valueOf(month + "月"));
                }
            }
        } else {
            if (formattedDate.equals("今天")) {
                holder.tv_user_publish_twoWord.setVisibility(View.VISIBLE);
                holder.tv_user_publish_twoWord.setText("今天");
            } else if (formattedDate.equals("昨天")) {
                holder.tv_user_publish_twoWord.setVisibility(View.VISIBLE);
                holder.tv_user_publish_twoWord.setText("昨天");
            } else {
                holder.tv_user_publish_date.setVisibility(View.VISIBLE);
                holder.tv_user_publish_month.setVisibility(View.VISIBLE);
                holder.rl_user_publish_time.setBackground(context.getResources().getDrawable(R.drawable.user_date_line));
                String[] dateParts = formattedDate.split("月");
                String month = dateParts[0];
                String day = dateParts[1].replace("日", "");

                holder.tv_user_publish_date.setText(day);
                holder.tv_user_publish_month.setText(String.valueOf(month + "月"));
            }
        }

        if (post.getPhotoCachePath().size() == 1) {
            holder.cv_user_publish_image_count.setVisibility(View.GONE);
        } else {
            holder.tv_user_publish_image_count.setVisibility(View.VISIBLE);
            holder.tv_user_publish_image_count.setText(String.valueOf(post.getPhotoCachePath().size()));
        }
        Glide.with(context)
                .load(post.getPhotoCachePath().get(0))
                .into(holder.iv_user_publish_image);
        holder.tv_user_publish_content.setText(post.getPublishedText());
        holder.tv_user_publish_location.setText(post.getLocation());

        if (holder.recordings != null && !holder.recordings.isEmpty()) {
            holder.tv_user_publish_recorder_count.setVisibility(View.GONE);
            holder.iv_user_publish_recorder.setVisibility(View.GONE);
            holder.cv_user_publish_recorder_count.setVisibility(View.GONE);
            switch (holder.recordings.size()) {
                case 3:
                    holder.iv_user_publish_recorder.setVisibility(View.VISIBLE);
                    holder.cv_user_publish_recorder_count.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_recorder_count.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_recorder_count.setText(String.valueOf(3));
                    break;
                case 2:
                    holder.iv_user_publish_recorder.setVisibility(View.VISIBLE);
                    holder.cv_user_publish_recorder_count.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_recorder_count.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_recorder_count.setText(String.valueOf(2));
                    break;
                case 1:
                    holder.iv_user_publish_recorder.setVisibility(View.VISIBLE);
                    holder.tv_user_publish_recorder_count.setVisibility(View.GONE);
                    holder.cv_user_publish_recorder_count.setVisibility(View.GONE);
                    break;
            }
        } else {
            holder.cv_user_publish_recorder_count.setVisibility(View.GONE);
            holder.tv_user_publish_recorder_count.setVisibility(View.GONE);
            holder.iv_user_publish_recorder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder.recordings != null && !holder.recordings.isEmpty()) {
            switch (v.getId()) {
                case R.id.iv_user_publish_recorder:
                    if (holder.recordings.size() > 0) {
                        handleClick(holder.recordings, holder.iv_user_publish_recorder);
                        holder.iv_user_publish_recorder.setImageResource(R.drawable.record_bg_click);
                    }
                    break;
            }
        }
    }

    private void handleClick(List<Recordings> recordings, ImageView iv) {
        if (currentPlayingImageView != null && currentPlayingImageView != iv) {
            currentPlayingImageView.setImageResource(R.drawable.record_homepage);
        }
        if (fragment == null) {
            currentPlayingImageView = iv;
            addFragment(recordings, iv);
        } else {
            AudioPublishPlayerFragment bt_fragment = (AudioPublishPlayerFragment) buttonFragmentMap.get(iv);
            if (fragment != bt_fragment) {
                fragment.stopPlayback();
                FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragment = null;
                currentPlayingImageView = iv;
                addFragment(recordings, iv);
            } else {
                fragment.togglePlayback();
            }
        }
    }

    private void addFragment(List<Recordings> recordings, ImageView iv) {
        if (!recordings.isEmpty()) {
            AudioPublishPlayerFragment.AUDIO_FILE_PATHS.clear();
            for (Recordings recording : recordings) {
                AudioPublishPlayerFragment.AUDIO_FILE_PATHS.add(recording.getRecordCachePath());
            }
            fragment = new AudioPublishPlayerFragment();
            buttonFragmentMap.put(iv, fragment);
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.publish_record_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }


    public void resetFragment() {
        if (fragment != null) {
            fragment.stopPlayback();
            FragmentActivity mContext = (FragmentActivity) context;
            FragmentTransaction fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            fragment = null;
        }
    }

    public void resetPlayingButton() {
        if (currentPlayingImageView != null) {
            currentPlayingImageView.setImageResource(R.drawable.record_homepage);
            currentPlayingImageView = null;
        }
    }

    public static UserPublishRecyclerAdapter getInstance() {
        return instance;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public AudioPublishPlayerFragment fragment;
        CardView cv_user_publish_recorder_count;
        TextView tv_user_publish_recorder_count;
        ImageView iv_user_publish_recorder;
        List<Recordings> recordings;
        RelativeLayout rl_user_publish_time;
        TextView tv_user_publish_twoWord;
        CardView cv_user_publish_image_count;
        TextView tv_user_publish_date;
        TextView tv_user_publish_month;
        TextView tv_user_publish_image_count;
        ImageView iv_user_publish_image;
        TextView tv_user_publish_content;
        TextView tv_user_publish_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_publish_date = itemView.findViewById(R.id.tv_user_publish_date);
            tv_user_publish_month = itemView.findViewById(R.id.tv_user_publish_month);
            tv_user_publish_image_count = itemView.findViewById(R.id.tv_user_publish_image_count);
            iv_user_publish_image = itemView.findViewById(R.id.iv_user_publish_image);
            tv_user_publish_content = itemView.findViewById(R.id.tv_user_publish_content);
            tv_user_publish_location = itemView.findViewById(R.id.tv_user_publish_location);
            cv_user_publish_image_count = itemView.findViewById(R.id.cv_user_publish_image_count);
            tv_user_publish_twoWord = itemView.findViewById(R.id.tv_user_publish_twoWord);
            rl_user_publish_time = itemView.findViewById(R.id.rl_user_publish_time);

            tv_user_publish_recorder_count = itemView.findViewById(R.id.tv_user_publish_recorder_count);
            iv_user_publish_recorder = itemView.findViewById(R.id.iv_user_publish_recorder);
            iv_user_publish_recorder.setOnClickListener(UserPublishRecyclerAdapter.this);
            cv_user_publish_recorder_count = itemView.findViewById(R.id.cv_user_publish_recorder_count);
        }
    }
}
