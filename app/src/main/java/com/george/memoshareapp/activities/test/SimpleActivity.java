package com.george.memoshareapp.activities.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.george.memoshareapp.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;


import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SimpleActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;


    CalendarView mCalendarView;

    LinearLayout mRelativeTool;

    private int mYear;
    CalendarLayout mCalendarLayout;
    private TimePickerView pvCustomTime;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calendar_trip_page;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {

            private TimePickerView pvTime;

            @Override
            public void onClick(View v) {
                /**
                 * @description
                 *
                 * 注意事项：
                 * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
                 * 具体可参考demo 里面的两个自定义layout布局。
                 * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
                 * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
                 */
                java.util.Calendar selectedDate = java.util.Calendar.getInstance();//系统当前时间
                java.util.Calendar startDate = java.util.Calendar.getInstance();
                startDate.set(2000, 0, 23);
                java.util.Calendar endDate = java.util.Calendar.getInstance();
                endDate.set(2030, 11, 28);
                //时间选择器 ，自定义布局
                pvCustomTime = new TimePickerBuilder(SimpleActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(date);
                        int year = calendar.get(java.util.Calendar.YEAR);
                        int month = calendar.get(java.util.Calendar.MONTH) + 1;
                        mCalendarView.scrollToCalendar(year, month, 1);
                    }
                })
                        /*.setType(TimePickerView.Type.ALL)//default is all
                        .setCancelText("Cancel")
                        .setSubmitText("Sure")
                        .setContentTextSize(18)
                        .setTitleSize(20)
                        .setTitleText("Title")
                        .setTitleColor(Color.BLACK)
                       /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                        .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                        .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                        .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
//                       .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                        .setSubmitColor(Color.WHITE)
                        .setCancelColor(Color.WHITE)*/
                        /*.animGravity(Gravity.RIGHT)// default is center*/
                        .setContentTextSize(18)
                        .setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.BLACK)
                        .setLineSpacingMultiplier(10f)
                        .setDate(selectedDate)
                        .setRangDate(startDate, endDate)
                        .setLayoutRes(R.layout.calender_time_picker, new CustomListener() {

                            @Override
                            public void customLayout(View v) {
                                ImageView tvSubmit = (ImageView) v.findViewById(R.id.iv_calender_confirm);
                                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_calender_cancel);
                                tvSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.returnData();
                                        pvCustomTime.dismiss();
                                    }
                                });
                                ivCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.dismiss();
                                    }
                                });
                            }
                        })
                        .setContentTextSize(18)
                        .setItemVisibleCount(5)
                        .setType(new boolean[]{true, true, false, false, false, false})
                        .setLabel("", "", "", "", "", "")
                        .setLineSpacingMultiplier(2.5f)
                        .setTextXOffset(0, 0, 0, 40, 0, -40)
                        .setDividerColor(0xFF24AD9D)
                        .build();
                pvCustomTime.show();
            }
        });

        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(getEnglishMonthName(mCalendarView.getCurMonth()));
        mCalendarView.setFixMode();

    }


    @Override
    protected void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int day = mCalendarView.getCurDay();
        Map<String, Calendar> map = new HashMap<>();


        Calendar today = getSchemeCalendar(2023, 6, 23, Color.RED);//红
        Calendar today1 = getSchemeCalendar(2023, 6, 24, Color.GREEN);//紫
        Calendar today2 = getSchemeCalendar(2023, 6, 21, Color.GRAY);//红和紫
        map = new HashMap<>();
        map.put(today.toString(), today);
        map.put(today1.toString(), today1);
        map.put(today2.toString(), today2);

        mCalendarView.setSchemeDate(map);
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);  // 设置方案颜色
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(getEnglishMonthName(calendar.getMonth()));
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getEnglishMonthName(int monthNumber) {
        // Java的Month类将月份从1（一月）计数到12（十二月）
        Month month = Month.of(monthNumber);
        String englishMonthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return englishMonthName.toUpperCase();
    }

    @Override
    public void onClick(View view) {

    }
}
