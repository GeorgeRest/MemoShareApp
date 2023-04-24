package com.george.memoshareapp.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.george.memoshareapp.R;

/**
 * @projectName: litepalTest1
 * @package: com.george.memoshareapp.utils
 * @className: VerificationCountDownTimer
 * @author: George
 * @description: TODO
 * @date: 2023/4/24 16:19
 * @version: 1.0
 */
public class VerificationCountDownTimer extends CountDownTimer {

    private TextView mTextView;

    public VerificationCountDownTimer(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);
        mTextView.setText(millisUntilFinished / 1000 + "秒重新发送");
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);
    }
}
