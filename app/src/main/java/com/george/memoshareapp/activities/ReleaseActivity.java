package com.george.memoshareapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.memoshareapp.Fragment.RecordAudioDialogFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ImageAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.interfaces.PhotoChangedListener;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.george.memoshareapp.manager.PostManager;
import com.george.memoshareapp.utils.CustomItemDecoration;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener, RecordingDataListener, PhotoChangedListener {

    private static final String TAG = "ReleaseActivity";
    private static String timeHourMinute;
    private static String memoireTimeYear;
    private TextView release_permission;
    private RelativeLayout rl_permission;
    private RelativeLayout rl_time;
    private DateFormat format;
    private Calendar calendar;
    private TextView release_time;
    private TextView release_time_hour;
    private ImageView release_button;
    private int PUBLIC_PERMISSION = 1;
    private PostManager postManager;
    private RelativeLayout addLocation;
    public static final int MAP_INFORMATION_SUCCESS = 1;
    public static final int RESULT_CODE_CONTACT = 2;

    private Post post;
    private TextView record;

    private RelativeLayout rl_location;
    private TextView tv_location;
    private double latitude;
    private double longitude;
    private String location;
    private List<Recordings> recordingsList = new ArrayList<>();
    private int StyleType = 5;
    private EditText release_edit;
    private ImageView release_back;

    private RelativeLayout addat;

    private static final int MAX_IMAGES = 9;  // Maximum number of images
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUriList = new ArrayList<>();
    private RelativeLayout rl_at;
    private RelativeLayout rl_addat;
    private String memoryTime;
    private String systemYear;
    private String systemMonth;
    private String systemDay;
    private String hour;
    private String minute;
    private String contactName;

    private List<String> addedNames = new ArrayList<>();
    private SpannableString spannableString;
    private ClickableSpan clickableSpan;
    private String atText;
    public String phoneNumber;

    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initView();
        initRecyclerView();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
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
                if (!s.toString().isEmpty() || !getImageUriList().isEmpty() || !recordingsList.isEmpty()) {
                    release_button.setImageResource(R.mipmap.re_press);
                } else {
                    release_button.setImageResource(R.mipmap.release_buttton);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initView() {
        addedNames = new ArrayList<>();
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
        rl_addat = (RelativeLayout) findViewById(R.id.rl_addat);
        release_edit = (EditText) findViewById(R.id.release_edit);
        release_back = (ImageView) findViewById(R.id.release_back);
        postManager = new PostManager(this);
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        addat.setOnClickListener(this);
        release_button.setOnClickListener(this);
        addLocation.setOnClickListener(this);

    }

    private void initRecyclerView() {
        // Set to 3-column grid layout
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        // Add blank items in RecyclerView
        imageUriList.add(null);
        imageAdapter = new ImageAdapter(this,this, imageUriList);
        recyclerView.setAdapter(imageAdapter);

        // 设置 RecyclerView 中的 item 的相对位置
//        int spanCount = 3;
//        int spacing = 0;
//        boolean includeEdge = true;
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

//        imageAdapter.updateButtonPosition(MAX_IMAGES);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
        recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));

        record.setOnClickListener(this);
        RecordAudioDialogFragment.recordCount = 0;
    }

    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                getYearMonthDay(year, monthOfYear + 1, dayOfMonth);
                tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                showTimePickerDialog(ReleaseActivity.this, StyleType, release_time_hour, calendar);

            }
        }, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getYearMonthDay(int year, int month, int dayOfMonth) {
        memoireTimeYear = year + "/" + month + "/" + dayOfMonth;
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
                        getTime(hourOfDay, minute);
                        tv.setText(hourOfDay + ":" + minute);

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

    private String memoryTime() {
        return memoryTime = memoireTimeYear + " " + timeHourMinute;

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

    private void getPhotoFromAlbum(Intent data) {
        // 从相册获取图片
        if (data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null && clipData.getItemCount() + imageUriList.size() > MAX_IMAGES + 1) {
                // 选择的图片数量超过了限制，提示用户重新选择
                Toast.makeText(this, "最多可展示 " + MAX_IMAGES + " 照片" + "," + "请重新选择！", Toast.LENGTH_SHORT).show();
            } else {
                // 处理选择的图片
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUriList.add(imageUriList.size() - 1, imageUri);
                    }
                } else {
                    Uri imageUri = data.getData();
                    imageUriList.add(imageUriList.size() - 1, imageUri);
                }
                imageAdapter.updateImageListAndButtonPosition(MAX_IMAGES);
            }
        }

    }

    //imageUriList即是所选择照片的uri，但因为我的逻辑需要判断list尾部是否存在null，在获取list时要做出判断，若有则需要移除，具体逻辑如下
    private List<Uri> getImageUriList() {
        List<Uri> list = new ArrayList<>();
        for (int i = 0; i < imageUriList.size(); i++) {
            if (imageUriList.get(i) != null) {
                list.add(imageUriList.get(i));
            }
        }
        return list;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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
                if (isInputEmpty()) {
                    Toast.makeText(this, "请输入内容，或添加图片或语音", Toast.LENGTH_SHORT).show();
                    return;
                }
                getSystemTime();
                if (location == null) {
                    location = "";
                }
                content = release_edit.getText().toString();
                content = removeAtNames(content);
                if (getImageUriList().size()==0){
                    Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                postManager.getDBParameter(getImageUriList(), phoneNumber, content, recordingsList, addedNames, location, longitude, latitude, PUBLIC_PERMISSION, getSystemTime(), memoryTime());
                finish();
                break;
            case R.id.release_back:
                finish();
                break;
            case R.id.rl_addLocation:
                startActivityForResult(new Intent(this, MapLocationActivity.class), 1);
                break;
            case R.id.rl_addat:
                startActivityForResult(new Intent(this, ContactListActivity.class), RESULT_CODE_CONTACT);
                break;

            case R.id.record:
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

    private String getSystemTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        systemYear = String.valueOf(cal.get(Calendar.YEAR));
        systemMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        systemDay = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0) {
            hour = String.valueOf(cal.get(Calendar.HOUR));
        } else
            hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        String publishedTime = systemYear + "/" + systemMonth + "/" + systemDay + " " + hour + ":" + minute;
        return publishedTime;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case MAP_INFORMATION_SUCCESS:
                post = (Post) data.getSerializableExtra("publishContent");
                latitude = post.getLatitude();
                longitude = post.getLongitude();
                location = post.getLocation();
                tv_location.setText(location);
                rl_location.setVisibility(View.VISIBLE);

                break;
            case RESULT_CODE_CONTACT:
                contactName = data.getStringExtra("name");
                addAtName(contactName);
                break;
            case RESULT_OK:
                getPhotoFromAlbum(data);
                if (!release_edit.getText().toString().isEmpty() || !getImageUriList().isEmpty() || !recordingsList.isEmpty()) {
                    release_button.setImageResource(R.mipmap.re_press);
                } else {
                    release_button.setImageResource(R.mipmap.release_buttton);
                }

                break;
        }
    }

    private void addAtName(String name) {

        if (!addedNames.contains(name)) {
            atText = "@" + name + " ";
            // 创建一个SpannableString对象
            spannableString = new SpannableString(atText);
            // 创建一个ClickableSpan对象
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // 定义点击事件，打开好友的个人信息页面
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // 定义样式，高亮显示
                    ds.setColor(Color.parseColor("#685c97"));
                    ds.setUnderlineText(false);
                }
            };
            addedNames.add(name);
        }
        content = this.release_edit.getText().toString().trim();
        content = removeAtNames(content);
        spannableString.setSpan(clickableSpan, 0, atText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将SpannableString添加到EditText的内容中
        release_edit.append(spannableString);
    }

    private String removeAtNames(String text) {
        return text.replaceAll("@\\w+", "");
    }

    @Override
    public void onRecordingDataReceived(Recordings recording, int type) {
        if (recording != null && type == 1) {
            recordingsList.add(recording);
        }
        if (recording != null && type == 0) {
            recordingsList.remove(recording);
        }
        if (!release_edit.getText().toString().isEmpty() || !getImageUriList().isEmpty() || !recordingsList.isEmpty()) {
            release_button.setImageResource(R.mipmap.re_press);
        } else {
            release_button.setImageResource(R.mipmap.release_buttton);
        }
        Log.d("TAG", "onRecordingDataReceived: " + recordingsList.size());
        for (Recordings recordings : recordingsList) {
            String recordCachePath = recordings.getRecordCachePath();
            System.out.println(recordCachePath);
        }
    }
    private boolean isInputEmpty() {
        boolean isEditTextEmpty = release_edit.getText().toString().trim().isEmpty();
        boolean isImageUriListEmpty = getImageUriList().isEmpty();
        boolean isRecordingsListEmpty = recordingsList.isEmpty();
        return isEditTextEmpty && isImageUriListEmpty && isRecordingsListEmpty;
    }
    private void updateReleaseButton() {
        boolean isEditTextEmpty = release_edit.getText().toString().isEmpty();
        boolean isPhotoListEmpty = getImageUriList().isEmpty(); // getImageUriList() 是你的方法，用于获取所有非空照片的Uri。
        boolean isAudioListEmpty = recordingsList.isEmpty(); // recordingsList是你的语音列表

        if (!isEditTextEmpty || !isPhotoListEmpty || !isAudioListEmpty) {
            release_button.setImageResource(R.mipmap.re_press);
        } else {
            release_button.setImageResource(R.mipmap.release_buttton);
        }
    }



    @Override
    public void onPhotoChanged() {
        updateReleaseButton();
    }
}