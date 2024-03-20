package com.george.memoshareapp.adapters;


import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.Recordings;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.adapters
 * @className: RecordAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/6/12 22:02
 * @version: 1.0
 */
public class RecordAdapter extends BaseMultiItemAdapter<Recordings>implements DragAndSwipeDataCallback {
    public static final int RECORD = 0;
    public static final int NEW_RECORD = 1;
    private RecordListener recordListener;
    private Context context;
    private boolean isCountdownRunning = false;
    private MediaPlayer mediaPlayer;
    private long totalRecordTime = 0;
    private HashMap<Recordings, MediaPlayer> mediaPlayerMap = new HashMap<>();

    public RecordAdapter(Context context, List<Recordings> data) {
        super(data);
        addItemType(RECORD, new OnMultiItemAdapterListener<Recordings, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecordAdapter.RecordViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(context, R.layout.item_voice_recorder, null);
                return new RecordViewHolder(view);
            }

            @Override
            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Recordings recordings) {
                RecordViewHolder holder = (RecordViewHolder) viewHolder;
                holder.record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recordListener.onRecordClicked();
                    }
                });
            }
        });
        addItemType(NEW_RECORD, new OnMultiItemAdapterListener<Recordings, RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecordAdapter.NewRecordViewHolder onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                View view = View.inflate(context, R.layout.item_new_recording, null);
                return new NewRecordViewHolder(view);

            }

            public void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int i, @Nullable Recordings recordings) {
                NewRecordViewHolder holder = (NewRecordViewHolder) viewHolder;
                String recordCachePath = recordings.getRecordCachePath();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(recordCachePath);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long totalDurationMs = Long.parseLong(time);  // 总时长，单位为毫秒
                recordings.setOriginalDuration(totalDurationMs);  // Store original duration
                int countDown = formatElapsedTime(totalDurationMs);
                holder.countDown.setText(countDown + "“");

                holder.newRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Before we start or resume any recording, we first pause any recording that is currently playing
                        for (Recordings recording : mediaPlayerMap.keySet()) {
                            MediaPlayer mediaPlayer = mediaPlayerMap.get(recording);
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                long currentDuration = mediaPlayer.getDuration();
                                recording.setCurrentDuration(currentDuration); // Store current duration

                                if (recording.getCountDownTimer() != null) {
                                    recording.getCountDownTimer().cancel();
                                }
                            }
                        }

                        // Access to MediaPlayer and CountDownTimer from the Recordings object
                        MediaPlayer mediaPlayer = recordings.getMediaPlayer();
                        CountDownTimer countDownTimer = recordings.getCountDownTimer();

                        if (mediaPlayer == null) {
                            mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(recordings.getRecordCachePath()); // 设置音频文件的路径
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            recordings.setMediaPlayer(mediaPlayer); // Store the mediaPlayer
                        }

                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            long currentDuration = mediaPlayer.getCurrentPosition();
                            recordings.setCurrentDuration(currentDuration); // Store current duration

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            // Create a new CountDownTimer with remaining duration
                            long remainingDuration = recordings.getOriginalDuration() - currentDuration;
                            countDownTimer = new CountDownTimer(remainingDuration, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    holder.countDown.setText(String.valueOf(formatElapsedTime(millisUntilFinished)) + "\"");
                                }

                                @Override
                                public void onFinish() {
                                    holder.countDown.setText(String.valueOf(formatElapsedTime(recordings.getOriginalDuration())) + "“");
                                    recordings.setCurrentDuration(0); // Reset current duration when finished
                                }
                            };

                            recordings.setCountDownTimer(countDownTimer); // Store the new CountDownTimer
                        } else {
                            try {
                                mediaPlayer.seekTo((int) recordings.getCurrentDuration());
                                mediaPlayer.start();

                                // If CountDownTimer does not exist, create it
                                if (countDownTimer == null) {
                                    long remainingDuration = recordings.getOriginalDuration() - recordings.getCurrentDuration();
                                    countDownTimer = new CountDownTimer(remainingDuration, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            holder.countDown.setText(String.valueOf(formatElapsedTime(millisUntilFinished)) + "“");
                                        }

                                        @Override
                                        public void onFinish() {
                                            holder.countDown.setText(String.valueOf(formatElapsedTime(recordings.getOriginalDuration())) + "“");
                                            recordings.setCurrentDuration(0); // Reset current duration when finished
                                        }
                                    };

                                    recordings.setCountDownTimer(countDownTimer); // Store the CountDownTimer
                                } else {
                                    // If CountDownTimer exists, reset it with the current duration
                                    long remainingDuration = recordings.getOriginalDuration() - recordings.getCurrentDuration();
                                    countDownTimer.cancel();
                                    countDownTimer = new CountDownTimer(remainingDuration, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            holder.countDown.setText(String.valueOf(formatElapsedTime(millisUntilFinished)) + "“");
                                        }

                                        @Override
                                        public void onFinish() {
                                            holder.countDown.setText(String.valueOf(formatElapsedTime(recordings.getOriginalDuration())) + "“");
                                            recordings.setCurrentDuration(0); // Reset current duration when finished
                                        }
                                    };

                                    recordings.setCountDownTimer(countDownTimer);
                                }

                                countDownTimer.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            }
        });


        onItemViewType(new OnItemViewTypeListener<Recordings>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends Recordings> list) {
                return list.get(i) == null ? RECORD : NEW_RECORD;
            }
        });
        this.context = context;
    }

    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }

    @Override
    public void dataSwap(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
    }

    public interface RecordListener {
        void onRecordClicked();
    }


    public void setRecordListener(RecordListener listener) {
        this.recordListener = listener;
    }


    public class RecordViewHolder extends RecyclerView.ViewHolder {

        ImageView record;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record = (ImageView) itemView.findViewById(R.id.voice_record);
        }
    }

    public int formatElapsedTime(long elapsedMillis) {
        int elapsedSeconds = (int) elapsedMillis / 1000; // 转换为秒
        return elapsedSeconds;
    }

    class NewRecordViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_newRecord;
        TextView countDown;
        ImageView newRecord;

        public NewRecordViewHolder(View itemView) {
            super(itemView);
            newRecord = (ImageView) itemView.findViewById(R.id.new_voice_record);
            countDown = (TextView) itemView.findViewById(R.id.record_count_down);
            rl_newRecord = (RelativeLayout) itemView.findViewById(R.id.rl_new_record);

        }
    }
}