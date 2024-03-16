package com.george.memoshareapp.activities;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.events.ForceLogoutEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForceLogoutEvent(ForceLogoutEvent event) {
        showForceLogoutDialog();
    }

    private void showForceLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("账号异常")
                .setMessage("您的账号在另一设备登录了。您已被强制下线。")
                .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 用户点击“重新登录”后的操作，比如跳转到登录界面
                        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 用户选择取消操作，可以选择关闭应用或其他操作
                        finish();
                    }
                })
                .setCancelable(false) // 使得点击对话框外部不会取消对话框显示
                .show();
    }
}
