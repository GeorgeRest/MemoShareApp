package com.george.memoshareapp.Fragment;

import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.VoiceMessageItem;
import com.george.memoshareapp.interfaces.MultiItemEntity;
import com.george.memoshareapp.interfaces.SendListener;
import com.george.memoshareapp.utils.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyVoiceRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyVoiceRecordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isRecording = false;
    private boolean isCanceled = false;
    private FrameLayout fl_group_chat_rec_bg;
    private ImageView iv_press_2_del_rec_bg;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private MediaPlayer mediaPlayer;
    private AudioTrack audioTrack;
    private final int CHOOSE_PIC_REQUEST_CODE = 3;
    private String fileName;

    public MyVoiceRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyVoiceRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyVoiceRecordFragment newInstance(String param1, String param2) {
        MyVoiceRecordFragment fragment = new MyVoiceRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_voice_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout rl_group_chat_press_2_rec = (RelativeLayout) view.findViewById(R.id.rl_group_chat_press_2_rec);

        rl_group_chat_press_2_rec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isRecording = true;
                fl_group_chat_rec_bg = (FrameLayout) getActivity().findViewById(R.id.fl_group_chat_rec_bg);
                fl_group_chat_rec_bg.setVisibility(View.VISIBLE);
                iv_press_2_del_rec_bg = (ImageView) getActivity().findViewById(R.id.iv_press_2_del_rec_bg);
                iv_press_2_del_rec_bg.setImageResource(R.drawable.group_chat_record_page_del_unpress);
                startRecording();

                return false;
            }
        });

        rl_group_chat_press_2_rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isRecording) {
                    float y = event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            if (y < -100) { // Customize the threshold for canceling
                                isCanceled = true;
                                iv_press_2_del_rec_bg.setImageResource(R.drawable.group_chat_record_page_del_press);

                                // Show "Release to cancel" UI
                                // Implement your logic to handle slide-to-cancel UI here
                            } else {
                                iv_press_2_del_rec_bg.setImageResource(R.drawable.group_chat_record_page_del_unpress);
                                isCanceled = false;

                                // Show normal recording UI
                                // Implement your logic to handle normal recording UI here
                            }
                            break;
                        case MotionEvent.ACTION_UP:

//                            recordMask.setVisibility(View.GONE);
                            // Stop recording audio
                            // Implement your logic to stop recording here
                            fl_group_chat_rec_bg.setVisibility(View.GONE);
                            if (isCanceled) {
                                cancelRecording();
                                // Discard the recorded audio and show a message indicating cancelation
                                Toast.makeText(getContext(), "已取消发送", Toast.LENGTH_SHORT).show();
                            } else {
                                // Send the recorded audio
                                // Implement your logic to send the audio message here
                                stopRecording();
                                SendListener listener = (SendListener) getActivity();
                                Date date = new Date(System.currentTimeMillis());
                                VoiceMessageItem voiceMessageItem = new VoiceMessageItem(audioFile.getAbsolutePath(), date, MultiItemEntity.SELF, "user");
                                voiceMessageItem.setFileName(fileName);
                                listener.sendContent(voiceMessageItem);
                                Toast.makeText(getContext(), "已发送语音", Toast.LENGTH_SHORT).show();
                            }
                            isRecording = false;

                            break;
                    }

                }


                return false;
            }
        });

        ImageView iv_group_chat_send = (ImageView) view.findViewById(R.id.iv_group_chat_send);
        iv_group_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(getActivity())
                        .openGallery(SelectMimeType.ofAll())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .isWithSelectVideoImage(false)//图片视频分别处理
//                                .setSelectionMode(Picture)//设置单选多选
                        .setMaxSelectNum(9)//最多可选多少图片
                        .setMaxVideoSelectNum(9)//最多可选多少视频
                        .isEmptyResultReturn(true)
                        .isMaxSelectEnabledMask(true)//设置是否启用选中数量超过限制时的蒙层效果，超出数量，显示蒙层提示用户无法再继续选择
                        .setImageSpanCount(4) //相册列表每行显示个数
                        .isDisplayCamera(false)//是否显示相机入口
                        .isPreviewVideo(true)//是否可以预览视频
                        .forResult(CHOOSE_PIC_REQUEST_CODE);
            }
        });

    }

    private void cancelRecording() {

        if (isRecording) {
            stopRecording(); // Stop the recording first if it's still ongoing
            if (audioFile != null && audioFile.exists()) {
                audioFile.delete();
                Toast.makeText(getContext(), "已取消录音", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void startRecording() {
        fileName = "audio_" + System.currentTimeMillis() + ".3gp";
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // Create a temporary audio file in the cache directory
            audioFile = new File(getActivity().getCacheDir(), fileName);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Toast.makeText(getContext(), "开始录音", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {

        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            isRecording = false;
            Toast.makeText(getContext(), "录音结束", Toast.LENGTH_SHORT).show();

            // Now you can use the 'audioFile' to handle the recorded audio (e.g., send it, play it, etc.)

            // Delete the temporary audio file after using it, if needed.
//            if (audioFile != null && audioFile.exists()) {
//                audioFile.delete();
//            }

        }

    }


}