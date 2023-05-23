package com.george.memoshareapp.Fragment;

import static android.text.format.DateUtils.formatElapsedTime;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.TextViewAndButton;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.Recordings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.io.IOException;
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
    private static final int MAX_RECORD_TIME = 60;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            onRecord(false);
        }
    };

    private boolean mStartRecording = true;
    long timeWhenPaused = 0;
    private boolean isRecording = false;
    private FloatingActionButton record_start;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;
    private OnAudioCancelListener mListener;
    private RecordManager recordManager;
    private TextView record;
    public static int recordCount = 0;
    private RelativeLayout recordContainer;
    private long elapsedMillis;
    private TextView newTextview;
    private Map<TextView, Recordings> recordMap;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private CountDownTimer currentCountDownTimer;
    private TextView currentTextView = null;
    private CountDownTimer currentTimer = null;
    private boolean isCountdownRunning = false;
    private MediaPlayer mediaPlayer;
    private RecordingDataListener recordingDataListener;
    private HashMap<TextView, CountDownTimer> timerMap = new HashMap<>();
    private static Map<ImageView, TextView> deleteMap = new HashMap<>();
    private static List<TextViewAndButton> textViewList = new ArrayList<>();
    private int marginTop;
    private FloatingActionButton record_stop;
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
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(result.getAbsolutePath());
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long totalDurationMs = Long.parseLong(time);  // 总时长，单位为毫秒
                        int formattedTime = formatElapsedTime(totalDurationMs);
                        Log.d(TAG, "onResult: " + formattedTime + "“"+time);
                        newTextview.setText(formattedTime + "“");
                        recordMap = new HashMap<>();
                        Recordings recordings = new Recordings();
                        Log.d(TAG, "onResult: " + recordings.getRecordCachePath());
                        recordings.setRecordTime(Long.parseLong(time));
                        recordings.setRecordCachePath(result.getAbsolutePath());
                        if (recordingDataListener != null) {
                            recordingDataListener.onRecordingDataReceived(recordings, 1);
                        }
                        recordMap.put(newTextview, recordings);
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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
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
                    mHandler.postDelayed(mRunnable, MAX_RECORD_TIME * 1000);  // 开始计时
                    mChronometerTime.start();
                } else {
                    mHandler.removeCallbacks(mRunnable);
                    mChronometerTime.stop();
                }
                mStartRecording = !mStartRecording;
            }
        });
        record_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometerTime.stop();
                timeWhenPaused = 0;
                isRecording = false;
                setCancelable(true);
                Toasty.info(getActivity(), "录音结束...", Toast.LENGTH_SHORT).show();
                recordManager.stop();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                animatorStart();
                dismiss();
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                if (isRecording) {
                    onRecord(false);
                }
            }
        });
        builder.setView(view);

        return builder.create();
    }

    private void initView(View view) {
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        record_start = (FloatingActionButton) view.findViewById(R.id.record_start);
        mIvClose = (ImageView) view.findViewById(R.id.record_audio_iv_close);
        record = (TextView) getActivity().findViewById(R.id.record);
        recordContainer = (RelativeLayout) getActivity().findViewById(R.id.record_container);
        record_stop = (FloatingActionButton) view.findViewById(R.id.record_stop);
        prefs = getActivity().getSharedPreferences("RecordCountPrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.apply();

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



    private void animatorStart() {
        float currentX = record.getX();
        int width = record.getWidth();
        float newX = currentX + width + dpToPx(1);
        marginTop = dpToPx(6);
        ObjectAnimator animator = ObjectAnimator.ofFloat(record, "x", currentX, newX);
        animator.setDuration(0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                addNewImageViewAt(currentX, width);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    private void addNewImageViewAt(float x, int width) {

        newTextview = new TextView(getContext());
        newTextview.setBackgroundResource(R.drawable.record_background);
        Log.d(TAG, "addNewImageViewAt: " + newTextview);

        ImageView deleteButton = new ImageView(getContext());
        TextViewAndButton textViewAndButton = new TextViewAndButton(newTextview, deleteButton);
        textViewList.add(textViewAndButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = textViewList.indexOf(textViewAndButton);
                recordContainer.removeView(textViewAndButton.textView);

                Recordings recordings = recordMap.get(textViewAndButton.textView);
                if (recordingDataListener != null) {
                    recordingDataListener.onRecordingDataReceived(recordings, 0);
                }
                recordContainer.removeView(textViewAndButton.deleteButton);
                textViewList.remove(textViewAndButton);
                int size = textViewList.size();
                int prevTextViewRight = 0;
                for (int i = 0; i < size; i++) {
                    TextViewAndButton item = textViewList.get(i);
                    RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) item.textView.getLayoutParams();
                    textParams.setMargins(prevTextViewRight, marginTop, 0, 0);
                    item.textView.setLayoutParams(textParams);

                    RelativeLayout.LayoutParams buttonParams = (RelativeLayout.LayoutParams) item.deleteButton.getLayoutParams();
                    buttonParams.setMargins(prevTextViewRight, marginTop, 0, 0);
                    item.deleteButton.setLayoutParams(buttonParams);

                    prevTextViewRight += item.textView.getWidth();
                }
                recordCount--;
                if (recordCount < 3) {
                    record.setVisibility(View.VISIBLE);
                }
                float currentX = record.getX();
                int distance = record.getWidth() + 2;
                float targetX = currentX - distance;
                ObjectAnimator animator = ObjectAnimator.ofFloat(record, "translationX", targetX);
                animator.setDuration(0);
                animator.start();
            }
        });

        deleteButton.setImageResource(R.drawable.delete_icon);
        RelativeLayout.LayoutParams deleteButtonParams = new RelativeLayout.LayoutParams(50, 50);
        deleteButtonParams.addRule(RelativeLayout.ALIGN_START, newTextview.getId());
        deleteButtonParams.addRule(RelativeLayout.ALIGN_TOP, newTextview.getId());
        deleteButtonParams.setMargins((int) x, 20, 0, 0);

        deleteButton.setLayoutParams(deleteButtonParams);


        // 设置 TextView 的宽高
        int textViewWidth = dpToPx(115);
        int textViewHeight = dpToPx(39);
        int marginTop = dpToPx(6);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
        params.setMargins((int) x, marginTop, 0, 0);
        newTextview.setLayoutParams(params);
        newTextview.setTextColor(getResources().getColor(R.color.new_text_color));
        newTextview.setTextSize(15);
        newTextview.setTypeface(null, Typeface.BOLD);
        newTextview.setPadding(100, 0, 0, 0);
        newTextview.setGravity(Gravity.CENTER);
        recordContainer.addView(newTextview);
        recordContainer.addView(deleteButton);
        recordCount++;


        newTextview.setOnClickListener(new View.OnClickListener() {
            private long totalRecordTime = 0;

            @Override
            public void onClick(View view) {
                Recordings recordings = recordMap.get(newTextview);
                if (!isCountdownRunning) {
                    // Store the total time only at the beginning
                    if (totalRecordTime == 0) {
                        totalRecordTime = recordings.getRecordTime();
                    }
                    // 创建倒计时器，设置倒计时时间和间隔时间
                    recordings.setCountDownTimer(new CountDownTimer(recordings.getRecordTime(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (isCountdownRunning) {
                                // 每次间隔时间到达时，更新倒计时文本
                                newTextview.setText(String.valueOf(formatElapsedTime(millisUntilFinished)) + "“");
                                recordings.setRecordTime(millisUntilFinished); // 更新剩余时间
                                Log.d(TAG, "onTick: " + millisUntilFinished + "  " + recordings.getRecordTime() + "  " + recordings.getCountDownTimer());
                            } else {
                                cancel(); // 如果倒计时已暂停，则取消倒计时器
                            }
                        }

                        @Override
                        public void onFinish() {
                            // 倒计时结束时的处理
                            // Reset the timer to the total time
                            newTextview.setText(String.valueOf(formatElapsedTime(totalRecordTime)) + "“");
                            recordings.setRecordTime(totalRecordTime);
                            recordings.setCountDownTimer(null);
                            isCountdownRunning = false;

                            // Reset the MediaPlayer
                            if (mediaPlayer != null) {
                                mediaPlayer.seekTo(0);
                                mediaPlayer.pause();
                            }
                        }
                    });

                    recordings.getCountDownTimer().start();
                    isCountdownRunning = true;

                    // Start or resume the MediaPlayer
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(recordings.getRecordCachePath()); // 设置音频文件的路径
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mediaPlayer.start();

                } else {
                    // 如果倒计时器正在运行，则暂停倒计时
                    if (recordings.getCountDownTimer() != null) {
                        recordings.getCountDownTimer().cancel();
                        isCountdownRunning = false;
                    }

                    // Pause the MediaPlayer
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                }
            }
        });

        if (recordCount == 3) {
            record.setVisibility(View.GONE);
        }

    }


    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public int formatElapsedTime(long elapsedMillis) {
        int elapsedSeconds = (int) elapsedMillis / 1000; // 转换为秒
        return elapsedSeconds;
    }

    public boolean isRecording() {
        return !mStartRecording;
    }

    public void setOnCancelListener(OnAudioCancelListener listener) {
        this.mListener = listener;

    }

    public void setDataListener(RecordingDataListener listener) {
        this.recordingDataListener = listener;
    }


    public interface OnAudioCancelListener {
        void onCancel();
    }
}