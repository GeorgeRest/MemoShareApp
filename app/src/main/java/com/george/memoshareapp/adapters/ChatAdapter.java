package com.george.memoshareapp.adapters;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.facebook.imagepipeline.common.SourceUriType;
import com.george.memoshareapp.R;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.properties.MessageType;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.orhanobut.logger.Logger;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatAdapter extends BaseMultiItemAdapter<MultiItemEntity> {
    private static final String TAG="ChatAdapter";
    private Context context;
    private List<MultiItemEntity> multiItemEntities;
    private TextChatViewHolder textChatViewHolder;
    private PicChatViewHolder picChatViewHolder;
    private VoiceChatViewHolder voiceChatViewHolder;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;

    public ChatAdapter(Context context, List<MultiItemEntity> multiItemEntities) {
        super(multiItemEntities);
        this.context = context;
        this.multiItemEntities = multiItemEntities;
        addItemType(MessageType.TEXT, new OnMultiItemAdapterListener<MultiItemEntity, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public TextChatViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(getContext(), R.layout.chat_text_item, null);
                return new TextChatViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable MultiItemEntity multiItemEntity) {
                textChatViewHolder = (TextChatViewHolder) viewHolder;
                switch (multiItemEntity.getUserSideType()) {
                    case MultiItemEntity.SELF:
                        textChatViewHolder.ll_hold_other_text_chat.setVisibility(View.VISIBLE);
                        textChatViewHolder.ll_hold_self_text_chat.setVisibility(View.VISIBLE);
                        textChatViewHolder.ll_hold_other_text_chat.setVisibility(View.GONE);
                        textChatViewHolder.tv_text_chat_self_name.setText(multiItemEntity.getUserInfo().getName());
                        textChatViewHolder.tv_text_chat_self_content.setText(multiItemEntity.getItemContent());
                        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItemEntity.getUserInfo().getHeadPortraitPath()).into(textChatViewHolder.iv_text_chat_self_profile);

                        Date date = multiItemEntity.getItemDate();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format.setTimeZone(TimeZone.getDefault());
                        String time = format.format(date);
                        textChatViewHolder.tv_text_chat_self_time.setText(time);


                        break;
                    case MultiItemEntity.OTHER:
                        textChatViewHolder.ll_hold_other_text_chat.setVisibility(View.VISIBLE);
                        textChatViewHolder.ll_hold_self_text_chat.setVisibility(View.VISIBLE);

                        textChatViewHolder.ll_hold_self_text_chat.setVisibility(View.GONE);
                        textChatViewHolder.tv_text_chat_other_name.setText(multiItemEntity.getUserInfo().getName());
                        textChatViewHolder.tv_text_chat_other_content.setText(multiItemEntity.getItemContent());
                        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItemEntity.getUserInfo().getHeadPortraitPath()).into(textChatViewHolder.iv_text_chat_other_profile);
                        Date date1 = multiItemEntity.getItemDate();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format1.setTimeZone(TimeZone.getDefault());
                        String time1 = format1.format(date1);
                        textChatViewHolder.tv_text_chat_other_time.setText(time1);
                        break;
                    default:
                        break;
                }
            }
        });
        addItemType(MessageType.IMAGE, new OnMultiItemAdapterListener<MultiItemEntity, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(getContext(), R.layout.chat_pic_item, null);
                return new PicChatViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable MultiItemEntity multiItem) {
                picChatViewHolder = (PicChatViewHolder) viewHolder;
                switch (multiItem.getUserSideType()) {
                    case MultiItemEntity.SELF:
                        picChatViewHolder.ll_hold_self_pic_chat.setVisibility(View.VISIBLE);
                        picChatViewHolder.ll_hold_other_pic_chat.setVisibility(View.VISIBLE);
                        picChatViewHolder.ll_hold_other_pic_chat.setVisibility(View.GONE);

                        picChatViewHolder.tv_pic_chat_self_name.setText(multiItem.getUserInfo().getName());
                        Date date = multiItem.getItemDate();
                        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItem.getUserInfo().getHeadPortraitPath()).into(picChatViewHolder.iv_text_chat_self_profile);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format.setTimeZone(TimeZone.getDefault());
                        String time = format.format(date);
                        picChatViewHolder.tv_pic_chat_self_time.setText(time);
                        setSelfImage(multiItem.getItemContent());
                        picChatViewHolder.circularProgressBar.setProgress(multiItem.getProgress());
                        if (multiItem.getProgress() < 100 && multiItem.getProgress() > 0) {
                            picChatViewHolder.image_gray.setVisibility(View.VISIBLE);
                            picChatViewHolder.circularProgressBar.setVisibility(View.VISIBLE);
                        } else {
                            picChatViewHolder.image_gray.setVisibility(View.GONE);
                            picChatViewHolder.circularProgressBar.setVisibility(View.GONE);
                        }

                        break;
                    case MultiItemEntity.OTHER:
                        picChatViewHolder.ll_hold_self_pic_chat.setVisibility(View.VISIBLE);
                        picChatViewHolder.ll_hold_other_pic_chat.setVisibility(View.VISIBLE);
                        picChatViewHolder.ll_hold_self_pic_chat.setVisibility(View.GONE);
                        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItem.getUserInfo().getHeadPortraitPath()).into(picChatViewHolder.iv_pic_chat_other_profile);
                        picChatViewHolder.tv_pic_chat_other_name.setText(multiItem.getUserInfo().getName());
                        Date date1 = multiItem.getItemDate();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format1.setTimeZone(TimeZone.getDefault());
                        String time1 = format1.format(date1);
                        picChatViewHolder.tv_pic_chat_other_time.setText(time1);
                        setOtherImage(multiItem.getItemContent());
                        break;
                }


            }
        });
        addItemType(MessageType.VOICE, new OnMultiItemAdapterListener<MultiItemEntity, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                } else {
                    mediaPlayer.reset();
                }

                View view = View.inflate(getContext(), R.layout.chat_voice_item, null);
                return new VoiceChatViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable MultiItemEntity multiItem) {

                voiceChatViewHolder = (VoiceChatViewHolder) viewHolder;
                switch (multiItem.getUserSideType()) {
                    case MultiItemEntity.SELF:
                        voiceChatViewHolder.ll_hold_other_voice_chat.setVisibility(View.VISIBLE);
                        voiceChatViewHolder.ll_hold_self_voice_chat.setVisibility(View.VISIBLE);

                        voiceChatViewHolder.ll_hold_other_voice_chat.setVisibility(View.GONE);

                        voiceChatViewHolder.tv_voice_chat_self_name.setText(multiItem.getUserInfo().getName());
                       Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItem.getUserInfo().getHeadPortraitPath()).into(voiceChatViewHolder.iv_voice_chat_self_profile);
                        Date date = multiItem.getItemDate();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format.setTimeZone(TimeZone.getDefault());
                        String time = format.format(date);
                        voiceChatViewHolder.tv_voice_chat_self_time.setText(time);

                        String voicePath = multiItem.getItemContent();
                        int durationInSeconds = getAudioTime(voicePath);

                        voiceChatViewHolder.chat_self_voice_count.setText(durationInSeconds + "“");
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) voiceChatViewHolder.rl_self_chat_rec_voice_bg.getLayoutParams();

                        if (multiItem.getProgress() < 100&&multiItem.getProgress()>0) {
                            voiceChatViewHolder.circularProgressBar.setVisibility(View.VISIBLE);
                        } else {
                            voiceChatViewHolder.circularProgressBar.setVisibility(View.GONE);
                        }
                        if (durationInSeconds <= 5) {
                            layoutParams.width = 99 * 4;
                        } else if (durationInSeconds > 5 && durationInSeconds <= 10) {
                            layoutParams.width = 120 * 4;
                        } else if (durationInSeconds > 10 && durationInSeconds <= 20) {
                            layoutParams.width = 137 * 4;
                        } else if (durationInSeconds > 20 && durationInSeconds <= 30) {
                            layoutParams.width = 153 * 4;
                        } else if (durationInSeconds > 30 && durationInSeconds <= 40) {
                            layoutParams.width = 166 * 4;
                        } else if (durationInSeconds > 40 && durationInSeconds <= 50) {
                            layoutParams.width = 178 * 4;
                        } else if (durationInSeconds > 50) {
                            layoutParams.width = 189 * 4;
                        } else {
                            Toast.makeText(context, "出错了", Toast.LENGTH_SHORT).show();
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        voiceChatViewHolder.rl_self_chat_rec_voice_bg.setLayoutParams(layoutParams);
                        voiceChatViewHolder.rl_self_chat_rec_voice_bg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playAudio(voicePath);
                                startSelfCountdown(durationInSeconds);
                            }
                        });


                        break;
                    case MultiItemEntity.OTHER:

                        voiceChatViewHolder.ll_hold_other_voice_chat.setVisibility(View.VISIBLE);
                        voiceChatViewHolder.ll_hold_self_voice_chat.setVisibility(View.VISIBLE);

                        voiceChatViewHolder.ll_hold_self_voice_chat.setVisibility(View.GONE);

                        voiceChatViewHolder.tv_voice_chat_other_name.setText(multiItem.getUserInfo().getName());
                        Glide.with(context).load(AppProperties.SERVER_MEDIA_URL+multiItem.getUserInfo().getHeadPortraitPath()).into(voiceChatViewHolder.iv_voice_chat_other_profile);
                        Date date1 = multiItem.getItemDate();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        format1.setTimeZone(TimeZone.getDefault());
                        String time1 = format1.format(date1);
                        voiceChatViewHolder.tv_voice_chat_other_time.setText(time1);

                        String voicePath1 = multiItem.getItemContent();
                        int durationInSeconds1 = getAudioTime(voicePath1);


                        voiceChatViewHolder.chat_other_voice_count.setText(durationInSeconds1 + "“");

                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) voiceChatViewHolder.rl_other_chat_rec_voice_bg.getLayoutParams();
                        if (durationInSeconds1 <= 5) {
                            layoutParams1.width = 99 * 4;
                        } else if (durationInSeconds1 > 5 && durationInSeconds1 <= 10) {
                            layoutParams1.width = 120 * 4;
                        } else if (durationInSeconds1 > 10 && durationInSeconds1 <= 20) {
                            layoutParams1.width = 137 * 4;
                        } else if (durationInSeconds1 > 20 && durationInSeconds1 <= 30) {
                            layoutParams1.width = 153 * 4;
                        } else if (durationInSeconds1 > 30 && durationInSeconds1 <= 40) {
                            layoutParams1.width = 166 * 4;
                        } else if (durationInSeconds1 > 40 && durationInSeconds1 <= 50) {
                            layoutParams1.width = 178 * 4;
                        } else if (durationInSeconds1 > 50) {
                            layoutParams1.width = 189 * 4;
                        } else {
                            Toast.makeText(context, "出错了", Toast.LENGTH_SHORT).show();
                        }

                        voiceChatViewHolder.rl_other_chat_rec_voice_bg.setLayoutParams(layoutParams1);

                        voiceChatViewHolder.rl_other_chat_rec_voice_bg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playAudio(voicePath1);
                                startOtherCountdown(durationInSeconds1);
                            }
                        });


                        break;
                    default:
                        break;
                }
            }
        });

        onItemViewType(new OnItemViewTypeListener<MultiItemEntity>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends MultiItemEntity> list) {

                return list.get(i).getItemShowType();
            }
        });

    }

    private void setSelfImage(String filePath) {
        Glide.with(getContext())
                .load(filePath)
                .centerCrop()
                .into(picChatViewHolder.iv_chat_self_image);
    }

    private void setOtherImage(String filePath) {
        Glide.with(getContext())
                .load(filePath)
                .centerCrop()
                .into(picChatViewHolder.iv_chat_other_image);
    }


    public class TextChatViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_hold_other_text_chat;
        private LinearLayout ll_hold_self_text_chat;
        private ImageView iv_text_chat_other_profile;
        private ImageView iv_text_chat_self_profile;
        private TextView tv_text_chat_other_name;
        private TextView tv_text_chat_self_name;
        private TextView tv_text_chat_other_time;
        private TextView tv_text_chat_self_time;
        private RelativeLayout rl_text_chat_other_detail;
        private RelativeLayout rl_text_chat_self_detail;
        private TextView tv_text_chat_other_content;
        private TextView tv_text_chat_self_content;


        public TextChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_hold_other_text_chat = itemView.findViewById(R.id.ll_hold_other_text_chat);
            ll_hold_self_text_chat = itemView.findViewById(R.id.ll_hold_self_text_chat);
            iv_text_chat_other_profile = itemView.findViewById(R.id.iv_text_chat_other_profile);
            iv_text_chat_self_profile = itemView.findViewById(R.id.iv_text_chat_self_profile);
            tv_text_chat_other_name = itemView.findViewById(R.id.tv_text_chat_other_name);
            tv_text_chat_self_name = itemView.findViewById(R.id.tv_voice_chat_self_name);
            tv_text_chat_other_time = itemView.findViewById(R.id.tv_text_chat_other_time);
            tv_text_chat_self_time = itemView.findViewById(R.id.tv_text_chat_self_time);
            rl_text_chat_other_detail = itemView.findViewById(R.id.rl_text_chat_other_detail);
            rl_text_chat_self_detail = itemView.findViewById(R.id.rl_text_chat_self_detail);
            tv_text_chat_other_content = itemView.findViewById(R.id.tv_text_chat_other_content);
            tv_text_chat_self_content = itemView.findViewById(R.id.tv_text_chat_self_content);

        }
    }

    public class PicChatViewHolder extends RecyclerView.ViewHolder {
        private final View image_gray;
        private CircularProgressBar circularProgressBar;
        private LinearLayout ll_hold_other_pic_chat;
        private LinearLayout ll_hold_self_pic_chat;
        private ImageView iv_pic_chat_other_profile;
        private ImageView iv_text_chat_self_profile;
        private TextView tv_pic_chat_other_name;
        private TextView tv_pic_chat_self_name;
        private TextView tv_pic_chat_other_time;
        private TextView tv_pic_chat_self_time;
        private RelativeLayout rl_pic_chat_other_detail;
        private RelativeLayout rl_pic_chat_self_detail;
        private ImageView iv_chat_other_image;
        private ImageView iv_chat_self_image;


        public PicChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_hold_other_pic_chat = itemView.findViewById(R.id.ll_hold_other_pic_chat);
            ll_hold_self_pic_chat = itemView.findViewById(R.id.ll_hold_self_pic_chat);
            iv_pic_chat_other_profile = itemView.findViewById(R.id.iv_pic_chat_other_profile);
            iv_text_chat_self_profile = itemView.findViewById(R.id.iv_text_chat_self_profile);
            tv_pic_chat_other_name = itemView.findViewById(R.id.tv_pic_chat_other_name);
            tv_pic_chat_self_name = itemView.findViewById(R.id.tv_pic_chat_self_name);
            tv_pic_chat_other_time = itemView.findViewById(R.id.tv_pic_chat_other_time);
            tv_pic_chat_self_time = itemView.findViewById(R.id.tv_pic_chat_self_time);
            rl_pic_chat_other_detail = itemView.findViewById(R.id.rl_pic_chat_other_detail);
            rl_pic_chat_self_detail = itemView.findViewById(R.id.rl_pic_chat_self_detail);
            iv_chat_other_image = itemView.findViewById(R.id.iv_chat_other_image);
            iv_chat_self_image = itemView.findViewById(R.id.iv_chat_self_image);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
            image_gray = itemView.findViewById(R.id.chat_self_image_gray);

        }
    }


    public class VoiceChatViewHolder extends RecyclerView.ViewHolder {

        private CircularProgressBar circularProgressBar;
        private RelativeLayout rl_other_chat_rec_voice_bg;
        private RelativeLayout rl_self_chat_rec_voice_bg;
        private LinearLayout ll_hold_other_voice_chat;
        private LinearLayout ll_hold_self_voice_chat;
        private ImageView iv_voice_chat_other_profile;
        private ImageView iv_voice_chat_self_profile;
        private TextView tv_voice_chat_other_name;
        private TextView tv_voice_chat_self_name;
        private TextView tv_voice_chat_other_time;
        private TextView tv_voice_chat_self_time;
        private RelativeLayout rl_voice_chat_other_detail;
        private RelativeLayout rl_voice_chat_self_detail;
        private TextView chat_other_voice_count;
        private TextView chat_self_voice_count;


        public VoiceChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_hold_other_voice_chat = itemView.findViewById(R.id.ll_hold_other_voice_chat);
            ll_hold_self_voice_chat = itemView.findViewById(R.id.ll_hold_self_voice_chat);
            iv_voice_chat_other_profile = itemView.findViewById(R.id.iv_voice_chat_other_profile);
            iv_voice_chat_self_profile = itemView.findViewById(R.id.iv_voice_chat_self_profile);
            tv_voice_chat_other_name = itemView.findViewById(R.id.tv_voice_chat_other_name);
            tv_voice_chat_self_name = itemView.findViewById(R.id.tv_voice_chat_self_name);
            tv_voice_chat_other_time = itemView.findViewById(R.id.tv_voice_chat_other_time);
            tv_voice_chat_self_time = itemView.findViewById(R.id.tv_voice_chat_self_time);
            rl_voice_chat_other_detail = itemView.findViewById(R.id.rl_voice_chat_other_detail);
            rl_voice_chat_self_detail = itemView.findViewById(R.id.rl_voice_chat_self_detail);
            chat_other_voice_count = itemView.findViewById(R.id.chat_other_voice_count);
            chat_self_voice_count = itemView.findViewById(R.id.chat_self_voice_count);
            rl_other_chat_rec_voice_bg = itemView.findViewById(R.id.rl_other_chat_rec_voice_bg);
            rl_self_chat_rec_voice_bg = itemView.findViewById(R.id.rl_self_chat_rec_voice_bg);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
        }
    }

    private void startSelfCountdown(int startTime) {
        countDownTimer = new CountDownTimer(startTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新 TextView 的文本
                int secondsRemaining = (int) millisUntilFinished / 1000;
                voiceChatViewHolder.chat_self_voice_count.setText(secondsRemaining + "“");
            }

            @Override
            public void onFinish() {
                // 倒计时结束后执行的操作
                voiceChatViewHolder.chat_self_voice_count.setText(startTime + "“");
            }
        };

        countDownTimer.start();
    }

    private void startOtherCountdown(int startTime) {
        countDownTimer = new CountDownTimer(startTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新 TextView 的文本
                int secondsRemaining = (int) millisUntilFinished / 1000;
                voiceChatViewHolder.chat_other_voice_count.setText(secondsRemaining + "“");
            }

            @Override
            public void onFinish() {
                // 倒计时结束后执行的操作
                voiceChatViewHolder.chat_other_voice_count.setText(startTime + "“");
            }
        };

        countDownTimer.start();
    }

    private void playAudio(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        Toast.makeText(getContext(), "正在播放", Toast.LENGTH_SHORT).show();
        try {

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    // 在播放完成后释放 MediaPlayer 资源
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
            // 获取录音文件的时长（以毫秒为单位）
            int durationInMillis = mediaPlayer.getDuration();
            // 将毫秒转换为秒
            int durationInSeconds = durationInMillis / 1000;
            Toast.makeText(getContext(), "录音文件时长：" + durationInSeconds + " 秒", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private int getAudioTime(String filePath) {
        int durationInSeconds = 0;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            // 获取录音文件的时长（以毫秒为单位）
            int durationInMillis = mediaPlayer.getDuration();
            // 将毫秒转换为秒
            durationInSeconds = durationInMillis / 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return durationInSeconds;
    }

}
