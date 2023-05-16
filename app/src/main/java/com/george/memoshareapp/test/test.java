package com.george.memoshareapp.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.george.memoshareapp.Fragment.AudioPlayerFragment;
import com.george.memoshareapp.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class test extends AppCompatActivity {
    private Button mBtnPlayAudio;
    public AudioPlayerFragment fragment;
    private Button record_sound;
    private Map<Button, Fragment> buttonFragmentMap=new HashMap<>();
    private String recordPath1="/data/user/0/com.george.memoshareapp/files/recordDirrecord_20230515_15_13_09.mp3";
    private String recordPath2="/data/user/0/com.george.memoshareapp/files/recordDirrecord_20230515_15_19_31.mp3";
    private String recordPath3="/data/user/0/com.george.memoshareapp/files/recordDirrecord_20230515_15_19_21.mp3";
    private Button play_sound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_record);
        initView();
        mBtnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick( recordPath2, mBtnPlayAudio);
            }
        });
        record_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick( recordPath1, record_sound);
            }
        });
        play_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick( recordPath3, play_sound);
            }
        });


    }

    private void handleClick( String recordPath, Button btn) {
        if (fragment == null) {
            addFragment(recordPath, btn);
        } else {
            AudioPlayerFragment bt_fragment = (AudioPlayerFragment) buttonFragmentMap.get(btn);
            if (fragment != bt_fragment) {
                fragment.stopPlayback();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragment = null;
                addFragment(recordPath, btn);
            } else {
                fragment.togglePlayback();
            }
        }
    }

    private void addFragment(String recordPath, Button btn) {
        AudioPlayerFragment.AUDIO_FILE_PATH= recordPath;
        fragment = new AudioPlayerFragment();
        buttonFragmentMap.put(btn, fragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.record_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    private void initView() {
        mBtnPlayAudio = (Button) findViewById(R.id.main_btn_play_sound);
        record_sound = (Button) findViewById(R.id.main_btn_record_sound);
        play_sound = (Button) findViewById(R.id.play_sound);
        // /data/user/0/com.george.memoshareapp/files/recordDirrecord_20230515_15_19_31.mp3
        // /data/user/0/com.george.memoshareapp/files/recordDirrecord_20230515_15_19_21.mp3
    }
}