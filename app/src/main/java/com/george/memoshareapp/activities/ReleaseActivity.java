package com.george.memoshareapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.george.memoshareapp.R;
import com.george.memoshareapp.Fragment.RecordAudioDialogFragment;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.manager.ContentManager;
import com.george.memoshareapp.utils.PermissionUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener, RecordingDataListener {

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
   // private PublishContent publishContent;
    private Post post;
    private TextView record;
    private Button mBtnRecordAudio;
    private Button mBtnPlayAudio;
    private RelativeLayout rl_location;
    private TextView tv_location;
    private double latitude;
    private double longitude;
    private String location;
    private List<Recordings> recordingsList  = new ArrayList<>();;
    private int StyleType=5;
    private EditText release_edit;
    private ImageView release_back;
    private TextView at;
    private RelativeLayout addat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
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
        tv_location = (TextView) findViewById(R.id.tv_location);
        release_button = (ImageView) findViewById(R.id.release_button);
        addLocation = (RelativeLayout) findViewById(R.id.rl_addLocation);
        addat = (RelativeLayout) findViewById(R.id.rl_addat);
        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
        record = (TextView) findViewById(R.id.record);
        release_edit = (EditText) findViewById(R.id.release_edit);
        release_back = (ImageView) findViewById(R.id.release_back);
        contentManager = new ContentManager(this);
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        addat.setOnClickListener(this);
        release_button.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        record.setOnClickListener(this);
        RecordAudioDialogFragment.recordCount=0;
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
                break;
            case R.id.rl_addat:
                startActivityForResult(new Intent(this, ContactListActivity.class), RESULT_CODE_CONTACT);
                break;
            case R.id.release_back:
                finish();
                break;
            case R.id.record:
                PermissionUtils.recordPermission(this);
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setDataListener(this);
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        if (!fragment.isRecording()) {
                            fragment.dismiss();
                        }
                    }
                });
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
                case MAP_INFORMATION_SUCCESS:
                    post = (Post) data.getSerializableExtra("publishContent");
                    latitude = post.getLatitude();
                    longitude = post.getLongitude();
                    location = post.getLocation();
                    tv_location.setText(location);
                    rl_location.setVisibility(View.VISIBLE);
                    break;
                case RESULT_CODE_CONTACT:
                    String name = data.getStringExtra("name");
                    release_edit.setText("@"+name);
                    break;


        }

    }

    @Override
    public void onRecordingDataReceived(Recordings recording) {
        if (recording != null) {
            Log.d(TAG, "onRecordingDataReceived: "+recording.getRecordTime());
            Log.d(TAG, "onRecordingDataReceived: "+recording.getRecordCachePath());

            recordingsList.add(recording);
            Log.d(TAG, "onRecordingDataReceived: "+recordingsList.size());

        }

    }
}