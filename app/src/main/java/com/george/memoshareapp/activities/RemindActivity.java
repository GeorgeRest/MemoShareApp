package com.george.memoshareapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.CommentBean;
import com.george.memoshareapp.beans.Remind;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.http.api.RemindServiceApi;
import com.george.memoshareapp.http.response.HttpData;
import com.george.memoshareapp.manager.RetrofitManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rl_contact;
    private RelativeLayout rl_content;
    private RelativeLayout rl_time;
    private RelativeLayout rl_interval;
    private RelativeLayout rl_note;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String remindContent;
    private String remindNote;
    private TextView tv_contact;
    private TextView tv_content;
    private TextView tv_time_date;
    private TextView tv_interval;
    private TextView tv_note;
    private int StyleType = 5;
    private Calendar calendar;
    private static String memoireTimeYear;
    private static String timeHourMinute;
    private TextView tv_time_hour;
    private ImageView iv_back;
    private TextView tv_finish;
    private User remindedUser;
    public static final int RESULT_CODE_REMIND_CONTENT = 1;
    public static final int RESULT_CODE_REMIND_USER = 2;
    public static final int RESULT_CODE_REMIND_NOTE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        initView();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_CODE_REMIND_USER) {
                            Intent intent = result.getData();
                            remindedUser = (User)intent.getSerializableExtra("contact_user");
                            String name = remindedUser.getName();
                            tv_contact.setText(name);
                        } else if (result.getResultCode() == RESULT_CODE_REMIND_CONTENT) {
                            Intent intent = result.getData();
                            remindContent = intent.getStringExtra("result");
                            tv_content.setText(remindContent);
                        } else if (result.getResultCode() == RESULT_CODE_REMIND_NOTE) {
                            Intent intent = result.getData();
                            remindNote = intent.getStringExtra("result");
                            tv_note.setText(remindNote);
                        }
                    }
                });
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        rl_interval = (RelativeLayout) findViewById(R.id.rl_interval);
        rl_note = (RelativeLayout) findViewById(R.id.rl_note);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time_date = (TextView) findViewById(R.id.tv_time_date);
        tv_time_hour = (TextView) findViewById(R.id.tv_time_hour);
        tv_interval = (TextView) findViewById(R.id.tv_interval);
        tv_note = (TextView) findViewById(R.id.tv_note);
        calendar = Calendar.getInstance(Locale.CHINA);

        iv_back.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        rl_content.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        rl_interval.setOnClickListener(this);
        rl_note.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_contact:
                Intent intent1 = new Intent(this, ContactListActivity.class);
                activityResultLauncher.launch(intent1);
                break;
            case R.id.rl_content:
                Intent intent2 = new Intent(this, RemindContentActivity.class);
                activityResultLauncher.launch(intent2);
                break;
            case R.id.rl_time:
                showDatePickerDialog(this, StyleType, tv_time_date, calendar);
                break;
            case R.id.rl_interval:
                showBottomDialog();
                break;
            case R.id.rl_note:
                Intent intent3 = new Intent(this, RemindNoteActivity.class);
                activityResultLauncher.launch(intent3);
                break;
            case R.id.tv_finish:
                SharedPreferences user = getSharedPreferences("User", MODE_PRIVATE);
                String myPhoneNumber = user.getString("phoneNumber", "");
                String time1 = (String) tv_time_date.getText();
                String time2 = (String) tv_time_hour.getText();
                String remindInterval = (String) tv_interval.getText();
                Remind remind = new Remind();
                remind.setReminderUserPhoneNumber(myPhoneNumber);
                remind.setRemindedUserPhoneNumber(remindedUser.getPhoneNumber());
                remind.setRemindContent(remindContent);
                remind.setRemindTime(time2);
                remind.setSelectDate(time1);
                remind.setRemindInterval(remindInterval);
                remind.setRemindNote(remindNote);
                RemindServiceApi serviceApi = RetrofitManager.getInstance().create(RemindServiceApi.class);
                serviceApi.postRemind(remind).enqueue(new Callback<HttpData<Remind>>() {
                    @Override
                    public void onResponse(Call<HttpData<Remind>> call, Response<HttpData<Remind>> response) {
                        if (response.isSuccessful()) {
                            finish();

                        } else {
                            int code = response.code();
                            Toast.makeText(RemindActivity.this, String.valueOf(code), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpData<Remind>> call, Throwable t) {

                    }
                });
                break;
        }
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.edit_interval_time, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();//获取dialog的window对象
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //获取屏幕的宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        //设置对话框大小为屏幕宽度的90%
        int dialogWidth = (int) (screenWidth * 0.95); // 你可以根据需要调整这个0.9的值

        // 设置对话框大小
        window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_interval.setText("关");
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_5min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_interval.setText("5分钟");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_10min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_interval.setText("10分钟");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_20min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_interval.setText("20分钟");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_30min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_interval.setText("30分钟");
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

    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                getYearMonthDay(year, monthOfYear + 1, dayOfMonth);
                tv.setText(memoireTimeYear);

                showTimePickerDialog(RemindActivity.this, StyleType, tv_time_hour, calendar);

            }
        }, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getYearMonthDay(int year, int month, int dayOfMonth) {
        memoireTimeYear = year + "-" + month + "-" + dayOfMonth;
        return memoireTimeYear;
    }

    public void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = getTime(hourOfDay, minute);
                        tv.setText(time);

                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    private String getTime(int hourOfDay, int minute) {
        timeHourMinute = hourOfDay + ":" + minute;
        return timeHourMinute;
    }
}

