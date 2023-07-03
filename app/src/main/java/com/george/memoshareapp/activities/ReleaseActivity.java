//package com.george.memoshareapp.activities;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.TimePickerDialog;
//import android.content.ClipData;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextPaint;
//import android.text.style.ClickableSpan;
//import android.view.DragEvent;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.chad.library.adapter.base.QuickAdapterHelper;
//import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener;
//import com.george.memoshareapp.Fragment.RecordAudioDialogFragment;
//import com.george.memoshareapp.R;
//import com.george.memoshareapp.adapters.AddSingleItemAdapter;
//import com.george.memoshareapp.adapters.ImageAdapter;
//import com.george.memoshareapp.adapters.RecordAdapter;
//import com.george.memoshareapp.beans.Post;
//import com.george.memoshareapp.beans.Recordings;
//import com.george.memoshareapp.interfaces.PhotoChangedListener;
//import com.george.memoshareapp.interfaces.RecordingDataListener;
//import com.george.memoshareapp.manager.PostManager;
//import com.george.memoshareapp.utils.CustomItemDecoration;
//import com.george.memoshareapp.utils.RecordDragAndSwipe;
//import com.george.memoshareapp.utils.SpacesItemDecoration;
//import com.orhanobut.logger.Logger;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//
//public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener, RecordingDataListener, PhotoChangedListener {
//
//    private static final String TAG = "ReleaseActivity";
//    private static String timeHourMinute;
//    private static String memoireTimeYear;
//    private TextView release_permission;
//    private RelativeLayout rl_permission;
//    private RelativeLayout rl_time;
//    private DateFormat format;
//    private Calendar calendar;
//    private TextView release_time;
//    private TextView release_time_hour;
//    private ImageView release_button;
//    private int PUBLIC_PERMISSION = 1;
//    private PostManager postManager;
//    private RelativeLayout addLocation;
//    public static final int MAP_INFORMATION_SUCCESS = 1;
//    public static final int RESULT_CODE_CONTACT = 2;
//
//    private Post post;
//    private TextView record;
//
//    private RelativeLayout rl_location;
//    private TextView tv_location;
//    private double latitude;
//    private double longitude;
//    private String location;
//    private List<Recordings> recordingsList = new ArrayList<>();
//    private int StyleType = 5;
//    private EditText release_edit;
//    private ImageView release_back;
//
//    private RelativeLayout addat;
//    private static final int MAX_IMAGES = 9;  // Maximum number of images
//    private RecyclerView recyclerView;
//    private ImageAdapter imageAdapter;
//    private List<Uri> imageUriList = new ArrayList<>();
//    private RelativeLayout rl_at;
//    private RelativeLayout rl_addat;
//    private String memoryTime;
//    private String systemYear;
//    private String systemMonth;
//    private String systemDay;
//    private String hour;
//    private String minute;
//    private String contactName;
//
//    private List<String> addedNames = new ArrayList<>();
//    private SpannableString spannableString;
//    private ClickableSpan clickableSpan;
//    private String atText;
//    public String phoneNumber;
//
//    private String content;
//
//    private RecyclerView recordRecyclerView;
//    private RecordAdapter recordAdapter;
//    private ImageView deleteArea;
//    private LinearLayout ll_add;
//    private RelativeLayout rlRecordOutside;
//    private LinearLayout ll_move_add;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_release_1);
//        initView();
//        initRecyclerView();
//        initRecordRecycler();
//        phoneNumber = getIntent().getStringExtra("phoneNumber");
//        rl_permission.setOnClickListener(this);
//        rl_time.setOnClickListener(this);
//        release_button.setOnClickListener(this);
//        release_back.setOnClickListener(this);
////        deletePhotoOrRecord(); 6/17
//
////        release_edit.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////
////            }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                if (!s.toString().isEmpty() || !getImageUriList().isEmpty() || !recordingsList.isEmpty()) {
////                    release_button.setImageResource(R.mipmap.re_press);
////                } else {
////                    release_button.setImageResource(R.mipmap.release_buttton);
////                }
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////
////            }
////        });
//
//
//    }
//
//    private void deletePhotoOrRecord() {
//        deleteArea.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                int action = event.getAction();
//                View view = (View) event.getLocalState();
//                switch (action) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        // 当开始拖动时，隐藏你的 record
//                        // 你可以在这里添加你隐藏 record 的代码
//
//                        view.setVisibility(View.INVISIBLE);
//                        Logger.d("开始拖动");
//                        break;
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        // 当拖动进入删除区域时，可以改变 ImageView 的颜色来给用户反馈
//                        Logger.d("拖动进入删除区域");
//                        break;
//                    case DragEvent.ACTION_DRAG_EXITED:
//                        // 当拖动离开删除区域时，改变 ImageView 的颜色
//                        Logger.d("拖动离开删除区域");
//                        break;
//                    case DragEvent.ACTION_DROP:
//                        // 当在删除区域释放时，实现你的删除操作
//                        // 你可以在这里添加你删除 record 的代码
//                        Logger.d("释放");
//                        break;
//                    case DragEvent.ACTION_DRAG_ENDED:
//                        // 当拖动结束时，显示你的 record，并恢复 ImageView 的颜色
//                        // 你可以在这里添加你显示 record 的代码
//                        view.setVisibility(View.VISIBLE);
//                        Logger.d("拖动结束");
//
//                        break;
//                }
//                return true;
//            }
//        });
//    }
//
//
//    private void initView() {
//        addedNames = new ArrayList<>();
//        format = DateFormat.getDateTimeInstance();
//        calendar = Calendar.getInstance(Locale.CHINA);
//        rl_permission = (RelativeLayout) findViewById(R.id.RL_permission);
//        rl_time = (RelativeLayout) findViewById(R.id.RL_time);
//        release_permission = (TextView) findViewById(R.id.release_permission);
//        release_time = (TextView) findViewById(R.id.release_time);
//        release_time_hour = (TextView) findViewById(R.id.release_time_hour);
//        tv_location = (TextView) findViewById(R.id.tv_location);
//        release_button = (ImageView) findViewById(R.id.release_button);
//        addLocation = (RelativeLayout) findViewById(R.id.rl_addLocation);
//        addat = (RelativeLayout) findViewById(R.id.rl_addat);
//        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
////        record = (TextView) findViewById(R.id.record);
//        rl_addat = (RelativeLayout) findViewById(R.id.rl_addat);
//        release_edit = (EditText) findViewById(R.id.release_edit);
//        release_back = (ImageView) findViewById(R.id.release_back);
//        deleteArea = (ImageView) findViewById(R.id.delete_area);
//        ll_add = (LinearLayout) findViewById(R.id.ll_add);
//        postManager = new PostManager(this);
//        rl_permission.setOnClickListener(this);
//        rl_time.setOnClickListener(this);
//        addat.setOnClickListener(this);
//        release_button.setOnClickListener(this);
//        addLocation.setOnClickListener(this);
//        ll_move_add = (LinearLayout) findViewById(R.id.ll_move_add);
//
//        rlRecordOutside = findViewById(R.id.rl_record_outside);
//    }
//
//    private void initRecyclerView() {
//        // Set to 3-column grid layout
//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        recyclerView.setHasFixedSize(true);
//
//        // Add blank items in RecyclerView
//        imageUriList.add(null);
//        imageAdapter = new ImageAdapter(this, this, imageUriList);
//        recyclerView.setAdapter(imageAdapter);
//
//        // 设置 RecyclerView 中的 item 的相对位置
////        int spanCount = 3;
////        int spacing = 0;
////        boolean includeEdge = true;
////        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//
////        imageAdapter.updateButtonPosition(MAX_IMAGES);
//
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
//        recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));
//
////        record.setOnClickListener(this);
////        RecordAudioDialogFragment.recordCount = 0;
//    }
//
//    public void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
//        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
//        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
//            // 绑定监听器(How the parent is notified that the date is set.)
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // 此处得到选择的时间，可以进行你想要的操作
//                getYearMonthDay(year, monthOfYear + 1, dayOfMonth);
//                tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
//
//                showTimePickerDialog(ReleaseActivity.this, StyleType, release_time_hour, calendar);
//
//            }
//        }, calendar.get(Calendar.YEAR)
//                , calendar.get(Calendar.MONTH)
//                , calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    private String getYearMonthDay(int year, int month, int dayOfMonth) {
//        memoireTimeYear = year + "/" + month + "/" + dayOfMonth;
//        return memoireTimeYear;
//
//    }
//
//    public void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
//        // Calendar c = Calendar.getInstance();
//        // 创建一个TimePickerDialog实例，并把它显示出来
//        // Activity是context的子类
//        new TimePickerDialog(activity, themeResId,
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        getTime(hourOfDay, minute);
//                        tv.setText(hourOfDay + ":" + minute);
//
//                    }
//                }
//                // 设置初始时间
//                , calendar.get(Calendar.HOUR_OF_DAY)
//                , calendar.get(Calendar.MINUTE)
//                // true表示采用24小时制
//                , true).show();
//    }
//
//    private String getTime(int hourOfDay, int minute) {
//        timeHourMinute = hourOfDay + ":" + minute;
//        return timeHourMinute;
//    }
//
//    private String memoryTime() {
//        return memoryTime = memoireTimeYear + " " + timeHourMinute;
//
//    }
//
//
//    private void showBottomDialog() {
//        //1、使用Dialog、设置style
//        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
//        //2、设置布局
//        View view = View.inflate(this, R.layout.release_permission, null);
//        dialog.setContentView(view);
//
//        Window window = dialog.getWindow();//获取dialog的window对象
//        //设置弹出位置
//        window.setGravity(Gravity.BOTTOM);
//        //设置弹出动画
//        window.setWindowAnimations(R.style.main_menu_animStyle);
//        //设置对话框大小
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.show();
//
//        dialog.findViewById(R.id.tv_pc_public).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                release_permission.setText("公开");
//
//
//                PUBLIC_PERMISSION = 1;
//                dialog.dismiss();
//            }
//        });
//
//        dialog.findViewById(R.id.tv_pc_private).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                release_permission.setText("私密");
//
//
//                PUBLIC_PERMISSION = 0;
//                dialog.dismiss();
//            }
//        });
//
//        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//    }
//
//    private void getPhotoFromAlbum(Intent data) {
//        // 从相册获取图片
//        if (data != null) {
//            ClipData clipData = data.getClipData();
//            if (clipData != null && clipData.getItemCount() + imageUriList.size() > MAX_IMAGES + 1) {
//                // 选择的图片数量超过了限制，提示用户重新选择
//                Toast.makeText(this, "最多可展示 " + MAX_IMAGES + " 照片" + "," + "请重新选择！", Toast.LENGTH_SHORT).show();
//            } else {
//                // 处理选择的图片
//                if (clipData != null) {
//                    int count = clipData.getItemCount();
//                    for (int i = 0; i < count; i++) {
//                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                        imageUriList.add(imageUriList.size() - 1, imageUri);
//                    }
//                    int imageLines = (imageUriList.size() / 3)+1;
//                    if (imageLines == 1){
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_begin_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//                    } else if (imageLines == 2) {
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_top_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//
//                    }else {
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_topp_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//
//                    }
//                } else {
//                    Uri imageUri = data.getData();
//                    imageUriList.add(imageUriList.size() - 1, imageUri);
//                    int imageLines = (imageUriList.size() / 3)+1;
//                    if (imageLines == 1){
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_begin_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//                    } else if (imageLines == 2) {
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_top_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//                    }else {
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_move_add.getLayoutParams();
//                        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin_topp_value);
//                        ll_move_add.setLayoutParams(layoutParams);
//
//                    }
//
//                }
//                imageAdapter.updateImageListAndButtonPosition(MAX_IMAGES);
//            }
//        }
//
//    }
//
//    //imageUriList即是所选择照片的uri，但因为我的逻辑需要判断list尾部是否存在null，在获取list时要做出判断，若有则需要移除，具体逻辑如下
//    private List<Uri> getImageUriList() {
//        List<Uri> list = new ArrayList<>();
//        for (int i = 0; i < imageUriList.size(); i++) {
//            if (imageUriList.get(i) != null) {
//                list.add(imageUriList.get(i));
//            }
//        }
//        return list;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.RL_permission:
//                showBottomDialog();
//                break;
//            case R.id.RL_time:
//
//
//                showDatePickerDialog(this, StyleType, release_time, calendar);
//
//
//                break;
//            case R.id.release_button:
//                if (isInputEmpty()) {
//                    Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                getSystemTime();
//
//                if (location == null) {
//                    location = "";
//                }
//
//                content = release_edit.getText().toString();
//                content = removeAtNames(content);
//                postManager.getDBParameter(getImageUriList(), phoneNumber, content, recordingsList, addedNames, location, longitude, latitude, PUBLIC_PERMISSION, getSystemTime(), memoryTime());
//                finish();
//                break;
//            case R.id.release_back:
//                finish();
//                break;
//            case R.id.rl_addLocation:
//                startActivityForResult(new Intent(this, MapLocationActivity.class), 1);
//                break;
//            case R.id.rl_addat:
//                startActivityForResult(new Intent(this, ContactListActivity.class), RESULT_CODE_CONTACT);
//                break;
//
////            case R.id.record:
////                RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
////                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
////                fragment.setDataListener(this);
////                break;
//        }
//    }
//
//
//    private void initRecordRecycler() {
//
//        recordRecyclerView = (RecyclerView) findViewById(R.id.rl_record);
//        recordRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        List<Recordings> recordings = new ArrayList<>();
//        recordings.add(null);
//        recordAdapter = new RecordAdapter(this, recordings);
//        recordAdapter.setRecordListener(new RecordAdapter.RecordListener() {
//            @Override
//            public void onRecordClicked() {
//                RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
//                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
//                fragment.setDataListener(ReleaseActivity.this);
//            }
//        });
//
//
//        QuickAdapterHelper helper = new QuickAdapterHelper.Builder(recordAdapter).build();
//        AddSingleItemAdapter singleItemAdapter = new AddSingleItemAdapter();
//        helper.addAfterAdapter(singleItemAdapter);
//
//
//        recordRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 0, 2, 0));
//
//        RecordDragAndSwipe recordDragAndSwipe = new RecordDragAndSwipe(recordAdapter);
//        recordDragAndSwipe
//                .setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)//可进行上下拖动，交换位置。 ItemTouchHelper.LEFT 允许向左拖动，ItemTouchHelper.RIGHT 允许向右拖动;//可进行左右滑动删除
//                .setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)//可进行左右滑动删除
//                .setItemViewSwipeEnabled(false);
//        recordDragAndSwipe.attachToRecyclerView(recordRecyclerView)
//                .setDataCallback(recordAdapter)
//                .setItemDragListener(new OnItemDragListener() {
//                    @Override
//                    public void onItemDragStart(@Nullable RecyclerView.ViewHolder viewHolder, int pos) {
//                        ViewGroup.LayoutParams params = recordRecyclerView.getLayoutParams();
//                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                        recordRecyclerView.setLayoutParams(params);
////                        ll_move_add.animate().translationY(-500);
////                        rlRecordOutside.setTranslationZ(getResources().getDimension(R.dimen.translation_z_value));
//
//                        Logger.d("onItemDragStart");
//                    }
//
//                    @Override
//                    public void onItemDragMoving(@NonNull RecyclerView.ViewHolder source, int from, @NonNull RecyclerView.ViewHolder target, int to) {
//
//                    }
//
//                    @Override
//                    public void onItemDragEnd(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
//                        ViewGroup.LayoutParams params = recordRecyclerView.getLayoutParams();
//                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                        recordRecyclerView.setLayoutParams(params);
//
//                    }
//                });
//
//        recordRecyclerView.setAdapter(recordAdapter);
//
//
//    }
//
//    private String getSystemTime() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//
//        systemYear = String.valueOf(cal.get(Calendar.YEAR));
//        systemMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
//        systemDay = String.valueOf(cal.get(Calendar.DATE));
//        if (cal.get(Calendar.AM_PM) == 0) {
//            hour = String.valueOf(cal.get(Calendar.HOUR));
//        } else
//            hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
//        minute = String.valueOf(cal.get(Calendar.MINUTE));
//        String publishedTime = systemYear + "/" + systemMonth + "/" + systemDay + " " + hour + ":" + minute;
//        return publishedTime;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode) {
//            case MAP_INFORMATION_SUCCESS:
//                post = (Post) data.getSerializableExtra("publishContent");
//                latitude = post.getLatitude();
//                longitude = post.getLongitude();
//                location = post.getLocation();
//                tv_location.setText(location);
//
//                rl_location.setVisibility(View.VISIBLE);
//
//                break;
//            case RESULT_CODE_CONTACT:
//                contactName = data.getStringExtra("name");
//                addAtName(contactName);
//                break;
//            case RESULT_OK:
//                getPhotoFromAlbum(data);
//                if (!getImageUriList().isEmpty()) {
//                    release_button.setImageResource(R.mipmap.re_press);
//                } else {
//                    release_button.setImageResource(R.mipmap.release_buttton);
//                }
//
//                break;
//        }
//    }
//
//    private void addAtName(String name) {
//
//        if (!addedNames.contains(name)) {
//            atText = "@" + name + " ";
//            // 创建一个SpannableString对象
//            spannableString = new SpannableString(atText);
//            // 创建一个ClickableSpan对象
//            clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    // 定义点击事件，打开好友的个人信息页面
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//                    // 定义样式，高亮显示
//                    ds.setColor(Color.parseColor("#685c97"));
//                    ds.setUnderlineText(false);
//                }
//            };
//            addedNames.add(name);
//        }
//        content = this.release_edit.getText().toString().trim();
//        content = removeAtNames(content);
//        spannableString.setSpan(clickableSpan, 0, atText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        // 将SpannableString添加到EditText的内容中
//        release_edit.append(spannableString);
//    }
//
//    private String removeAtNames(String text) {
//        return text.replaceAll("@\\w+", "");
//    }
//
//    @Override
//    public void onRecordingDataReceived(Recordings recording, int type) {
//        List<Recordings> recordings = recordAdapter.getItems();
//
//        if (recording != null && type == 1) {
//            // remove null placeholder before adding new recording
//            if (recordings.get(recordings.size() - 1) == null) {
//                recordings.remove(recordings.size() - 1);
//                recordAdapter.notifyItemRemoved(recordings.size()); // notify that placeholder has been removed
//            }
//
//            // add new recording
//            recordings.add(recording);
//            recordAdapter.notifyItemInserted(recordings.size() - 1); // notify that recording has been added
//
//            // add null placeholder back if needed
//            if (recordings.size() < 3) {
//                recordings.add(null);
//                recordAdapter.notifyItemInserted(recordings.size() - 1); // notify that placeholder has been added
//            }
//        }
//
//        if (recording != null && type == 0) {
//            int position = recordingsList.indexOf(recording);
//            recordingsList.remove(recording);
//            // notify the adapter about the removed item
//            recordAdapter.notifyItemRemoved(position);
//        }
//    }
//
//
////        if (!release_edit.getText().toString().isEmpty() || !getImageUriList().isEmpty() || !recordingsList.isEmpty()) {
////            release_button.setImageResource(R.mipmap.re_press);
////        } else {
////            release_button.setImageResource(R.mipmap.release_buttton);
////        }
////        Log.d("TAG", "onRecordingDataReceived: " + recordingsList.size());
////        for (Recordings recordings : recordingsList) {
////            String recordCachePath = recordings.getRecordCachePath();
////            System.out.println(recordCachePath);
////        }
//
//    private boolean isInputEmpty() {
//        boolean isEditTextEmpty = release_edit.getText().toString().trim().isEmpty();
//        boolean isImageUriListEmpty = getImageUriList().isEmpty();
//        boolean isRecordingsListEmpty = recordingsList.isEmpty();
////        return isEditTextEmpty && isImageUriListEmpty && isRecordingsListEmpty;
//        return isImageUriListEmpty;
//    }
//
//    private void updateReleaseButton() {
//        boolean isEditTextEmpty = release_edit.getText().toString().isEmpty();
//        boolean isPhotoListEmpty = getImageUriList().isEmpty(); // getImageUriList() 是你的方法，用于获取所有非空照片的Uri。
//        boolean isAudioListEmpty = recordingsList.isEmpty(); // recordingsList是你的语音列表
//
////        if (!isEditTextEmpty || !isPhotoListEmpty || !isAudioListEmpty) {
//        if (!isPhotoListEmpty) {
//            release_button.setImageResource(R.mipmap.re_press);
//        } else {
//            release_button.setImageResource(R.mipmap.release_buttton);
//        }
//    }
//
//    @Override
//    public void onPhotoChanged() {
//        updateReleaseButton();
//    }
//}
package com.george.memoshareapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener;
import com.george.memoshareapp.Fragment.RecordAudioDialogFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.ImageAdapter;
import com.george.memoshareapp.adapters.RecordAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.beans.Recordings;
import com.george.memoshareapp.interfaces.PhotoChangedListener;
import com.george.memoshareapp.interfaces.RecordingDataListener;
import com.george.memoshareapp.manager.PostManager;
import com.george.memoshareapp.utils.CustomItemDecoration;
import com.george.memoshareapp.utils.ImageDragAndSwipe;
import com.george.memoshareapp.utils.RecordDragAndSwipe;
import com.george.memoshareapp.utils.SpacesItemDecoration;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private static final int REQUEST_CODE_CHOOSE = 3;

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
    private RecyclerView recyclerView;//更改变量名 imageRecyclerView
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

    private RecyclerView recordRecyclerView;
    private RecordAdapter recordAdapter;
    private ImageView deleteArea;
    //    private LinearLayout ll_add;
    private LinearLayout ll_move_add;
    private RecyclerView rl_record;
    private RecyclerView rv_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initView();
        initRecyclerView();
        initRecordRecycler();
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        release_button.setOnClickListener(this);
        release_back.setOnClickListener(this);
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
//        record = (TextView) findViewById(R.id.record);
        rl_addat = (RelativeLayout) findViewById(R.id.rl_addat);
        release_edit = (EditText) findViewById(R.id.release_edit);
        release_back = (ImageView) findViewById(R.id.release_back);
        deleteArea = (ImageView) findViewById(R.id.delete_area);
//        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_move_add = (LinearLayout) findViewById(R.id.ll_move_add);
        rl_record = (RecyclerView) findViewById(R.id.rl_record);
        rv_image = (RecyclerView) findViewById(R.id.rv_image);
        postManager = new PostManager(this);
        rl_permission.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        addat.setOnClickListener(this);
        release_button.setOnClickListener(this);
        addLocation.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        imageUriList.add(null);
        imageAdapter = new ImageAdapter(this,  imageUriList);
        recyclerView.setAdapter(imageAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_expected_size);
        recyclerView.addItemDecoration(new CustomItemDecoration(spacingInPixels));

        ImageDragAndSwipe imageDragAndSwipe = new ImageDragAndSwipe(imageAdapter) {
            boolean isUp = false;
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                int[] itemLocation = new int[2];
                viewHolder.itemView.getLocationInWindow(itemLocation);
                int[] deleteLocation = new int[2];
                deleteArea.getLocationInWindow(deleteLocation);
                // 判断 Item 的 y 坐标是否进入有效范围
                if (itemLocation[1] + viewHolder.itemView.getHeight() >= deleteLocation[1]) {
//                    deleteArea.setBackgroundResource(R.drawable.delete_finish);
                    if (isUp) {
                        deleteArea.setTag(true);
                        viewHolder.itemView.setVisibility(View.INVISIBLE);
                        imageAdapter.removeAt(viewHolder.getAdapterPosition());//删除
                        Logger.d(imageAdapter.getItems());

                        List<Uri> images = imageAdapter.getItems();
                        if (images.get(images.size() -1) != null){
                            images.add(null);
                            imageAdapter.notifyItemInserted(images.size() - 1);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                upDateLines(imageUriList.size() - 1);
                            }
                        }, 300);

                    }else{
                        deleteArea.setTag(false);
                        deleteArea.setBackgroundResource(R.drawable.prepare_delete);
                    }
                }
                isUp = false;
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                isUp = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);

            }

        };

        imageDragAndSwipe
                .setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)//可进行上下拖动，交换位置。 ItemTouchHelper.LEFT 允许向左拖动，ItemTouchHelper.RIGHT 允许向右拖动;//可进行左右滑动删除
                .setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);//可进行左右滑动删除;
        imageDragAndSwipe.attachToRecyclerView(recyclerView)
                .setDataCallback(imageAdapter)
                .setItemDragListener(new OnItemDragListener() {
                    @Override
                    public void onItemDragStart(@Nullable RecyclerView.ViewHolder viewHolder, int pos) {
                        deleteArea.setVisibility(View.VISIBLE);
                        rv_image.setTranslationZ(rv_image.getTranslationZ() + 10);
                    }


                    @Override
                    public void onItemDragMoving(@NonNull RecyclerView.ViewHolder source, int from, @NonNull RecyclerView.ViewHolder target, int to) {

                    }

                    @Override
                    public void onItemDragEnd(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
                        rv_image.setTranslationZ(rv_image.getTranslationZ() - 10);
                        deleteArea.setVisibility(View.GONE);
                        imageUriList = imageAdapter.getItems();
                        Logger.d(imageUriList);
                    }
                });
        recyclerView .setAdapter(imageAdapter);
    }


    private void getPhotoFromAlbum(Intent data) {
        if (data != null){
            List<LocalMedia> selectList = PictureSelector.obtainSelectorList(data);
            if (selectList != null ) {
                int count = selectList.size();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = Uri.parse(selectList.get(i).getPath());

                    String fileType = getFileType(selectList.get(i).getPath());

                    if(fileType.equals("video")) {
                        imageUriList.add(0, imageUri);
                    }
                    else {
                        imageUriList.add(imageUriList.size() - 1, imageUri);
                    }
                    upDateLines(imageUriList.size() - 1);
                }
            } else {
                return;
            }
            imageAdapter.updateImageListAndButtonPosition(MAX_IMAGES);

        }
    }

    public String getFileType(String path) {
        if(path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg") ||
                path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".gif")) {
            return "photo";
        }
        else if(path.toLowerCase().endsWith(".mp4") || path.toLowerCase().endsWith(".avi") ||
                path.toLowerCase().endsWith(".mov") || path.toLowerCase().endsWith(".mkv")) {
            return "video";
        }
        else {
            return "unknown";
        }
    }

    private void upDateLines(int Size) {
        int imageLines = (Size / 3)+1;
        if (imageLines == 1){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
            layoutParams.topMargin = (int) getResources().getDimension(R.dimen.release_oneLine_margin);
            ll_move_add.setLayoutParams(layoutParams);
        } else if (imageLines == 2) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
            layoutParams.topMargin = (int) getResources().getDimension(R.dimen.release_twoLines_margin);
            ll_move_add.setLayoutParams(layoutParams);

        }else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_move_add.getLayoutParams();
            layoutParams.topMargin = (int) getResources().getDimension(R.dimen.release_threeLines_margin);
            ll_move_add.setLayoutParams(layoutParams);

        }

    }

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
                    Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                getSystemTime();

                if (location == null) {
                    location = "";
                }

                content = release_edit.getText().toString();
                content = removeAtNames(content);
                recordingsList.removeAll(Collections.singleton(null));
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

        }
    }


    private void initRecordRecycler() {

        recordRecyclerView = (RecyclerView) findViewById(R.id.rl_record);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Recordings> recordings = new ArrayList<>();
        recordings.add(null);
        recordAdapter = new RecordAdapter(this, recordings);
        recordAdapter.setRecordListener(new RecordAdapter.RecordListener() {
            @Override
            public void onRecordClicked() {
                RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setDataListener(ReleaseActivity.this);
            }
        });


        recordRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 0, 2, 0));

        RecordDragAndSwipe recordDragAndSwipe = new RecordDragAndSwipe(recordAdapter){
            boolean isUp = false;
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                int[] itemLocation = new int[2];
                viewHolder.itemView.getLocationInWindow(itemLocation);
                int[] deleteLocation = new int[2];
                deleteArea.getLocationInWindow(deleteLocation);
                // 判断 Item 的 y 坐标是否进入有效范围
                if (itemLocation[1] + viewHolder.itemView.getHeight() >= deleteLocation[1]) {
//                    deleteArea.setBackgroundResource(R.drawable.delete_finish);
                    if (isUp) {
                        deleteArea.setTag(true);
                        viewHolder.itemView.setVisibility(View.INVISIBLE);
                        recordAdapter.removeAt(viewHolder.getAdapterPosition());//删除
                        Logger.d(recordAdapter.getItems());

                        List<Recordings> recordings = recordAdapter.getItems();
                        if (recordings.get(recordings.size() -1) != null){
                            recordings.add(null);
                            recordAdapter.notifyItemInserted(recordings.size() - 1);
                        }
                    }else{
                        deleteArea.setTag(false);
                        deleteArea.setBackgroundResource(R.drawable.prepare_delete);
                    }
                }
                isUp = false;
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                isUp = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);

            }
        };
        recordDragAndSwipe
                .setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)//可进行上下拖动，交换位置。 ItemTouchHelper.LEFT 允许向左拖动，ItemTouchHelper.RIGHT 允许向右拖动;//可进行左右滑动删除
                .setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);//可进行左右滑动删除;
        recordDragAndSwipe.attachToRecyclerView(recordRecyclerView)
                .setDataCallback(recordAdapter)
                .setItemDragListener(new OnItemDragListener() {
                    @Override
                    public void onItemDragStart(@Nullable RecyclerView.ViewHolder viewHolder, int pos) {
                        deleteArea.setVisibility(View.VISIBLE);
//                        rl_record.setTranslationZ(getResources().getDimension(R.dimen.translation_z_value));
                        rl_record.setTranslationZ(rl_record.getTranslationZ() + 10);
                    }


                    @Override
                    public void onItemDragMoving(@NonNull RecyclerView.ViewHolder source, int from, @NonNull RecyclerView.ViewHolder target, int to) {

                    }

                    @Override
                    public void onItemDragEnd(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
                        rl_record.setTranslationZ(rl_record.getTranslationZ() - 10);
                        deleteArea.setVisibility(View.GONE);
                        recordingsList=recordAdapter.getItems();
                        Logger.d(recordingsList);
                    }
                });

        recordRecyclerView.setAdapter(recordAdapter);


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
                if (!getImageUriList().isEmpty()) {
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
        List<Recordings> recordings = recordAdapter.getItems();
        if (recording != null && type == 1) {
            // remove null placeholder before adding new recording
            if (recordings.get(recordings.size() - 1) == null) {
                recordings.remove(recordings.size() - 1);
                recordAdapter.notifyItemRemoved(recordings.size()); // notify that placeholder has been removed
            }

            // add new recording
            recordings.add(recording);
            recordAdapter.notifyItemInserted(recordings.size() - 1); // notify that recording has been added

            // add null placeholder back if needed
            if (recordings.size() < 3) {
                recordings.add(null);
                recordAdapter.notifyItemInserted(recordings.size() - 1); // notify that placeholder has been added
            }
        }

        recordingsList=recordAdapter.getItems();
        Logger.d(recordingsList);
    }


    private boolean isInputEmpty() {
        boolean isEditTextEmpty = release_edit.getText().toString().trim().isEmpty();
        boolean isImageUriListEmpty = getImageUriList().isEmpty();
        boolean isRecordingsListEmpty = recordingsList.isEmpty();
//        return isEditTextEmpty && isImageUriListEmpty && isRecordingsListEmpty;
        return isImageUriListEmpty;
    }

    private void updateReleaseButton() {
        boolean isEditTextEmpty = release_edit.getText().toString().isEmpty();
        boolean isPhotoListEmpty = getImageUriList().isEmpty(); // getImageUriList() 是你的方法，用于获取所有非空照片的Uri。
        boolean isAudioListEmpty = recordingsList.isEmpty(); // recordingsList是你的语音列表

//        if (!isEditTextEmpty || !isPhotoListEmpty || !isAudioListEmpty) {
        if (!isPhotoListEmpty) {
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