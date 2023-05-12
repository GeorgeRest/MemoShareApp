package com.george.memoshareapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.PublishContent;
import com.george.memoshareapp.manager.ContentManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReleaseActivity";
    private static String time;
    private static String memoireTime;
    private TextView release_permission;
    private RelativeLayout rl_permission;
    private RelativeLayout rl_time;
    private DateFormat format;
    private Calendar calendar;
    private TextView release_time;
    private TextView release_time_hour;
    private ImageView release_button;
    private int PUBLIC_PERMISSION = 1;
    private ContentManager contentManager;
    private RelativeLayout addLocation;
    public static final int MAP_INFORMATION_SUCCESS=1 ;
    public static final int RESULT_CODE_CONTACT = 2;
    private PublishContent publishContent;
    private int StyleType=5;
    private EditText release_edit;
    private ImageView release_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release);
        initView();
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        release_button.setOnClickListener(this);
        release_back.setOnClickListener(this);
        release_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){
                    release_button.setImageResource(R.mipmap.releasr_press);
                }else {
                    release_button.setImageResource(R.mipmap.release_buttton);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView() {
        format = DateFormat.getDateTimeInstance();
        calendar = Calendar.getInstance(Locale.CHINA);
        rl_permission = (RelativeLayout) findViewById(R.id.RL_permission);
        rl_time = (RelativeLayout) findViewById(R.id.RL_time);
        release_permission = (TextView) findViewById(R.id.release_permission);
        release_time = (TextView) findViewById(R.id.release_time);
        release_time_hour = (TextView) findViewById(R.id.release_time_hour);
        release_button = (ImageView) findViewById(R.id.release_button);
        addLocation = (RelativeLayout) findViewById(R.id.rl_addLocation);
        release_edit = (EditText) findViewById(R.id.release_edit);
        release_back = (ImageView) findViewById(R.id.release_back);
        contentManager = new ContentManager(this);
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        release_button.setOnClickListener(this);
        addLocation.setOnClickListener(this);
    }

    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                showTimePickerDialog(ReleaseActivity.this, StyleType, release_time_hour, calendar);
                getYearMonthDay(year, monthOfYear + 1, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getYearMonthDay(int year, int month, int dayOfMonth) {
        memoireTime = year + "/" + month + "/" + dayOfMonth;
        return memoireTime;

    }

    public void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText(hourOfDay + ":" + minute);
                        getTime(hourOfDay, minute);
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    private String getTime(int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        return time;
    }


    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.release_permission, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();//获取dialog的window对象
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_pc_public).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                release_permission.setText("公开");
                PUBLIC_PERMISSION = 1;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_pc_private).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                release_permission.setText("私密");
                PUBLIC_PERMISSION = 0;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RL_permission:
                showBottomDialog();
                break;
            case R.id.RL_time:
                showDatePickerDialog(this, StyleType, release_time, calendar);
                break;
            case R.id.release_button:
                contentManager.saveContent2DB(PUBLIC_PERMISSION, memoireTime, time);
                break;
            case R.id.rl_addLocation:
                startActivityForResult(new Intent(this, MapLocationActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
                case MAP_INFORMATION_SUCCESS:
                    publishContent = (PublishContent) data.getSerializableExtra("publishContent");
                    break;
            case R.id.release_back:
                finish();
                break;

        }

    }
}