package com.george.memoshareapp.beans;

import android.os.CountDownTimer;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: Recordings
 * @author: George
 * @description: TODO
 * @date: 2023/5/11 22:32
 * @version: 1.0
 */
public class Recordings extends LitePalSupport implements Serializable {
    private Post post;
    private String recordCachePath;
    private long recordTime;
    @Column(ignore = true)
    private CountDownTimer countDownTimer;
    @Column(ignore = true)
    private long initialRecordTime;
    public Recordings() {
    }

    public Recordings( String recordCachePath, long recordTime, CountDownTimer countDownTimer, long initialRecordTime) {
        this.recordCachePath = recordCachePath;
        this.recordTime = recordTime;
        this.countDownTimer = countDownTimer;
        this.initialRecordTime = initialRecordTime;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getRecordCachePath() {
        return recordCachePath;
    }

    public void setRecordCachePath(String recordCachePath) {
        this.recordCachePath = recordCachePath;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public long getInitialRecordTime() {
        return initialRecordTime;
    }

    public void setInitialRecordTime(long initialRecordTime) {
        this.initialRecordTime = initialRecordTime;
    }
}

