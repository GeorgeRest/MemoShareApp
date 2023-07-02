package com.george.memoshareapp.Fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.activities.HomePageActivity;
import com.george.memoshareapp.adapters.HomePagerAdapter;
import com.george.memoshareapp.adapters.HomeWholeRecyclerViewAdapter;
import com.george.memoshareapp.adapters.UserPublishRecyclerAdapter;

import java.io.IOException;

public class AudioPlayerFragment extends Fragment {
    public static String AUDIO_FILE_PATH = "";
    private MediaPlayer mediaPlayer = null;
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;
    private TextView currentProgressTextView;
    private TextView fileLengthTextView;
    private ImageView cancelRecord;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        handler = new Handler();
        seekBar = view.findViewById(R.id.seekbar);
        currentProgressTextView = view.findViewById(R.id.current_progress_text_view);
        fileLengthTextView = view.findViewById(R.id.file_length_text_view);
        cancelRecord = view.findViewById(R.id.cancel_record);
        initMediaPlayer();

        cancelRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRecord();
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
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(AUDIO_FILE_PATH);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            fileLengthTextView.setText(getTime(mediaPlayer.getDuration()));
            mediaPlayer.start();
            updateSeekBar();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    closeRecord();
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeRecord() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(runnable);
            getActivity().getSupportFragmentManager().beginTransaction().remove(AudioPlayerFragment.this).commit();
            HomeWholeRecyclerViewAdapter adapter = HomeWholeRecyclerViewAdapter.getInstance();
            UserPublishRecyclerAdapter instance = UserPublishRecyclerAdapter.getInstance();
            if (adapter != null) {
                adapter.resetFragment();
                adapter.resetPlayingButton();
            }
            if (instance != null) {
                instance.resetFragment();
                instance.resetPlayingButton();
            }
        }
    }

    private String getTime(int duration) {// 获取音频文件的时长
        int secondsTotal = duration / 1000; // 将毫秒转换为秒
        int minutes = secondsTotal / 60; // 计算分钟数
        int seconds = secondsTotal % 60; // 计算秒数
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
            } else {
                mediaPlayer.start();
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
