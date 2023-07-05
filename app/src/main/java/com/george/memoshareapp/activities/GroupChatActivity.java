package com.george.memoshareapp.activities;

import static android.text.format.DateUtils.formatElapsedTime;

import android.media.AudioFormat;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.GroupChatRecordAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    private boolean mStartRecording = true;
    private long timeWhenPaused = 0;
    private boolean isRecording = false;
    private FloatingActionButton record_start;
    private FloatingActionButton record_stop;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;
    private RecyclerView rl_record;
    private Button please_say;
    private boolean isClosed = false;
    private RecordingDataListener recordingDataListener;
    private RecyclerView recordRecyclerView;
    private List<Recordings> recordingsList = new ArrayList<>();
    private GroupChatRecordAdapter recordAdapter;
    private ImageView deleteArea;
    private RecordManager recordManager;
    private File recordDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        recordDir = new File(this.getFilesDir(), "recordDir");
        if (!recordDir.exists()) {
            recordDir.mkdir();
        }
        initView();

        recordAdapter = new GroupChatRecordAdapter(this, recordingsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rl_record.setLayoutManager(linearLayoutManager);
        rl_record.setAdapter(recordAdapter);
    }

    private void initView() {
        rl_record = (RecyclerView) findViewById(R.id.record);
        record_start = (FloatingActionButton) findViewById(R.id.record_start);
        please_say = (Button) findViewById(R.id.please_say);

        initRecordManager(recordDir);
        initRecordEvent();

        please_say.setOnTouchListener(new View.OnTouchListener() {
            private long touchDownTime; //添加一个变量来记录按下的时间

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchDownTime = System.currentTimeMillis(); //在按下的时候记录当前时间
                    RecordManager.getInstance().start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    long touchDuration = System.currentTimeMillis() - touchDownTime; //抬起时计算按下的时间长度
                    if(touchDuration < 1000) { //如果按下的时间小于1秒
                        Toast.makeText(v.getContext(), "按下的时间太短", Toast.LENGTH_SHORT).show(); //弹出 Toast
                    } else {
                        RecordManager.getInstance().stop(); //否则执行长按监听的操作
                    }
                }
                return false;
            }
        });
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

                    private long time1;

                    @Override
                    public void onResult(File result) {
//                        if (isClosed) {
//                            boolean deleted = result.delete();
//
//                            return;
//                        }
                        System.out.println(result.getAbsolutePath() + "----------");
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(result.getAbsolutePath());
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long totalDurationMs = Long.parseLong(time);  // 总时长，单位为毫秒
                        System.out.println("=========time====="+time);
                        System.out.println("=========time====="+Long.parseLong(time));
                        time1 = (Long.parseLong(time))/1000;
                        System.out.println("=========time====="+ time1);

                        String formattedTime = formatElapsedTime(totalDurationMs);

                        Recordings recordings = new Recordings();

                        recordings.setRecordTime(time1);

                        recordings.setRecordCachePath(result.getAbsolutePath());
//                        if (recordingDataListener != null) {
//                            recordingDataListener.onRecordingDataReceived(recordings, 1);
//                        }
                        recordingsList.add(recordings);
                        // 通知adapter数据已经改变

                        recordAdapter.notifyItemInserted(0);
                        System.out.println("notify+--------");
                    }
                });
            }

            @Override
            public void onError(String error) {
            }
        });
    }

}