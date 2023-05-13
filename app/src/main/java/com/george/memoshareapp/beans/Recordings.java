package com.george.memoshareapp.beans;

import android.os.CountDownTimer;

import org.litepal.crud.LitePalSupport;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.beans
 * @className: Recordings
 * @author: George
 * @description: TODO
 * @date: 2023/5/11 22:32
 * @version: 1.0
 */
public class Recordings extends LitePalSupport {
    private int id;
    private String recordCachePath;
    private long recordTime;
    private CountDownTimer countDownTimer;
    private long InitialRecordTime;

    public Recordings() {
    }

    public Recordings(int id, String recordCachePath, long recordTime) {
        this.id = id;
        this.recordCachePath = recordCachePath;
        this.recordTime = recordTime;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public long getInitialRecordTime() {
        return InitialRecordTime;
    }

    public void setInitialRecordTime(long initialRecordTime) {
        InitialRecordTime = initialRecordTime;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
