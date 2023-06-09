package com.george.memoshareapp.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.UserPublishRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioPublishPlayerFragment extends Fragment {
    public static List<String> AUDIO_FILE_PATHS = new ArrayList<>();
    private MediaPlayer mediaPlayer = null;
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;
    private TextView currentProgressTextView;
    private TextView fileLengthTextView;
    private ImageView cancelRecord;
    private ImageView iv_record_pause;
    private ImageView iv_record_next;
    private ImageView iv_record_back;
    private int currentFileIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_playback_publish, container, false);
        handler = new Handler();
        seekBar = view.findViewById(R.id.seekbar);
        currentProgressTextView = view.findViewById(R.id.current_progress_text_view);
        fileLengthTextView = view.findViewById(R.id.file_length_text_view);
        cancelRecord = view.findViewById(R.id.cancel_record);

        iv_record_back = view.findViewById(R.id.iv_record_back);
        iv_record_next = view.findViewById(R.id.iv_record_next);
        iv_record_pause = view.findViewById(R.id.iv_record_pause);

        initMediaPlayer();
        cancelRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRecord();
            }
        });

        iv_record_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextFile();
            }
        });

        iv_record_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousFile();
            }
        });

        iv_record_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayback();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(AUDIO_FILE_PATHS.get(currentFileIndex));
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            fileLengthTextView.setText(getTime(mediaPlayer.getDuration()));
            mediaPlayer.start();
            updateSeekBar();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (currentFileIndex < AUDIO_FILE_PATHS.size() - 1) {
                        playNextFile();
                    } else {
                        closeRecord();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextFile() {
        if (currentFileIndex < AUDIO_FILE_PATHS.size() - 1) {
            currentFileIndex++;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                iv_record_pause.setImageResource(R.mipmap.publish_recorder_pause);
            }
            initMediaPlayer();
        } else {
            Toast.makeText(getActivity(), "没有下一首歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    private void playPreviousFile() {
        if (currentFileIndex > 0) {
            currentFileIndex--;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                iv_record_pause.setImageResource(R.mipmap.publish_recorder_pause);
            }
            initMediaPlayer();
        } else {
            Toast.makeText(getActivity(), "没有上一首歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeRecord() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(runnable);
            getActivity().getSupportFragmentManager().beginTransaction().remove(AudioPublishPlayerFragment.this).commit();
            UserPublishRecyclerAdapter instance = UserPublishRecyclerAdapter.getInstance();
            if (instance != null) {
                instance.resetFragment();
                instance.resetPlayingButton();
            }
        }
    }

    private String getTime(int duration) {
        int secondsTotal = duration / 1000;
        int minutes = secondsTotal / 60;
        int seconds = secondsTotal % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        currentProgressTextView.setText(getTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 0);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void togglePlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                iv_record_pause.setImageResource(R.mipmap.publish_recorder_play);
            } else {
                mediaPlayer.start();
                iv_record_pause.setImageResource(R.mipmap.publish_recorder_pause);
                updateSeekBar();
            }
        }
    }

    public void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(runnable);
        }
    }
}

