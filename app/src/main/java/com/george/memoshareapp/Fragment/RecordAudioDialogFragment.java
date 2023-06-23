package com.george.memoshareapp.Fragment;

import static android.text.format.DateUtils.formatElapsedTime;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.media.AudioFormat;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.R;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.beans.TextViewAndButton;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * @projectName: SoundRecord
 * @package: com.george.soundrecord
 * @className: RecordAudioDialogFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/11 16:54
 * @version: 1.0
 */
public class RecordAudioDialogFragment extends DialogFragment {

    private static final String TAG = "RecordAudioDialogFragme";

    private int mRecordPromptCount = 0;
    private boolean mStartRecording = true;
    long timeWhenPaused = 0;
    private boolean isRecording = false;
    private FloatingActionButton record_start;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;
    private RecordManager recordManager;
    private TextView currentTextView = null;
    private CountDownTimer currentTimer = null;
    private boolean isCountdownRunning = false;
    private RecordingDataListener recordingDataListener;
    private HashMap<TextView, CountDownTimer> timerMap = new HashMap<>();
    private static Map<ImageView, TextView> deleteMap = new HashMap<>();
    private static List<TextViewAndButton> textViewList = new ArrayList<>();
    private FloatingActionButton record_stop;
    private boolean isClosed = false;

    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment dialogFragment = new RecordAudioDialogFragment();
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File recordDir = new File(getContext().getFilesDir(), "recordDir");
        if (!recordDir.exists()) {
            recordDir.mkdir();
        }

        initRecordManager(recordDir);
        initRecordEvent();

    }


    private void initRecordManager(File recordDir) {
        recordManager = RecordManager.getInstance();
        recordManager.init(MyApplication.getInstance(), BuildConfig.DEBUG);
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(16000));
        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT));
        recordManager.changeRecordDir(recordDir.getAbsolutePath());

    }

    private void initRecordEvent() {

        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {

                recordManager.setRecordResultListener(new RecordResultListener() {
                    @Override
                    public void onResult(File result) {
                        if(isClosed){
                            boolean deleted = result.delete();
                            Log.d(TAG, "onResult: "+deleted);
                            return;
                        }
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(result.getAbsolutePath());
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long totalDurationMs = Long.parseLong(time);  // 总时长，单位为毫秒
                          String formattedTime = formatElapsedTime(totalDurationMs);
                        Log.d(TAG, "onResult: " + formattedTime + "“" + time);

                        Recordings recordings = new Recordings();
                        recordings.setRecordTime(Long.parseLong(time));
                        recordings.setRecordCachePath(result.getAbsolutePath());
                        Log.d(TAG, "onResult: " + recordings.getRecordCachePath());

                        if (recordingDataListener != null) {
                            recordingDataListener.onRecordingDataReceived(recordings, 1);
                        }
//                        recordMap.put(newTextview, recordings);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_record_audio, null);
        initView(view);

        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary));
        record_start.setBackgroundTintList(colorStateList);
        record_start.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));
        record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    mChronometerTime.start();
                } else {
                    mChronometerTime.stop();
                }
                mStartRecording = !mStartRecording;
            }
        });
        record_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording){
                    return;
                }
                mChronometerTime.stop();
                timeWhenPaused = 0;
                isRecording = false;
                setCancelable(true);
                Toasty.info(getActivity(), "录音结束...", Toast.LENGTH_SHORT).show();
                recordManager.stop();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                dismiss();
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClosed=true;
                if(!isRecording){
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    dismiss();
                }
                mChronometerTime.stop();
                timeWhenPaused = 0;
                isRecording = false;
                setCancelable(true);
                recordManager.stop();
                Toasty.info(getActivity(), "取消录音", Toast.LENGTH_SHORT).show();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                dismiss();



            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void initView(View view) {
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        record_start = (FloatingActionButton) view.findViewById(R.id.record_start);
        mIvClose = (ImageView) view.findViewById(R.id.record_audio_iv_close);

        record_stop = (FloatingActionButton) view.findViewById(R.id.record_stop);
    }

    private void onRecord(boolean start) {
        if (start) {
            record_start.setImageResource(R.drawable.ic_media_pause);
            Toasty.info(getActivity(), "开始录音...", Toast.LENGTH_SHORT).show();
            if (isRecording) {
                mChronometerTime.setBase(SystemClock.elapsedRealtime() - timeWhenPaused);
                recordManager.resume();
            } else {
                mChronometerTime.setBase(SystemClock.elapsedRealtime());
                recordManager.start();
            }
            mChronometerTime.start();
            isRecording = true;
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setCancelable(false);

        } else {
            record_start.setImageResource(R.drawable.ic_mic_white_36dp);
            timeWhenPaused = SystemClock.elapsedRealtime() - mChronometerTime.getBase();
            mChronometerTime.stop();
            recordManager.pause();
            Toasty.info(getActivity(), "暂停录音...", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDataListener(RecordingDataListener listener) {
        this.recordingDataListener = listener;
    }
}