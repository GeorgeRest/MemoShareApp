package com.george.memoshareapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_EDITED_GENDER = "edited_gender";
    public static final String EXTRA_EDITED_BIRTHDAY = "edited_birthday";
    public static final String EXTRA_EDITED_REGION = "edited_region";
    public static final String EXTRA_EDITED_SIGNATURE = "edited_signature";
    public static final String EXTRA_EDITED_NAME = "edited_name";



    private static int EDIT_GENDER = 0;
    private RelativeLayout rl_name;
    private RelativeLayout rl_signature;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_birthday;
    private RelativeLayout rl_region;
    private CircleImageView camera;
    private String destPath;

    private TextView tv_edit_gender;
    private TextView tv_edit_birthday;
    private TimePickerView pvTime;
    private TimePickerView pvCustomLunar;
    private TimePickerView pvCustomTime;
    private String time;
    private TextView tv_edit_signature;
    private TextView tv_edit_name;
    private TextView tv_edit_region;
    private ImageView back;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = (User) getIntent().getSerializableExtra("user");
        initView();
        initTimePicker();
        initLunarPicker();
        initCustomTimePicker();
    }
    private void initView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_region = (RelativeLayout) findViewById(R.id.rl_region);
        camera = (CircleImageView) findViewById(R.id.camera);

        rl_name.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_region.setOnClickListener(this);

        tv_edit_signature = (TextView) findViewById(R.id.tv_edit_signature);
        tv_edit_name = (TextView) findViewById(R.id.tv_edit_name);
        tv_edit_gender = (TextView) findViewById(R.id.tv_edit_gender);
        tv_edit_birthday = (TextView) findViewById(R.id.tv_edit_birthday);
        tv_edit_region = (TextView) findViewById(R.id.tv_edit_region);
        back = (ImageView) findViewById(R.id.edit_iv_back);
        camera.setOnClickListener(this);
        getParamsFromUser();


    }

    private void getParamsFromUser() {

        String gender = user.getGender();
        if (gender!=null){
            tv_edit_gender.setText(gender);
        }
        String birthday = user.getBirthday();
        if (birthday!=null){
            tv_edit_birthday.setText(birthday);
        }
        String name = user.getName();
        if (name==null){
            long id = user.getId();
            tv_edit_name.setText(user.generateDefaultName(id));
        }else {
            tv_edit_name.setText(name);
        }
        String signature = user.getSignature();
        if (signature!=null){
            tv_edit_signature.setText(signature);
        }
        String region = user.getRegion();
        if (region!=null){
            tv_edit_region.setText(region);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camera:
//                PictureSelector.create(this)
//                        .openGallery(SelectMimeType.ofImage())
//                        .setImageEngine(GlideEngine.createGlideEngine())
//                        .setMaxSelectNum(1)
//                        .isEmptyResultReturn(true)
//                        .isMaxSelectEnabledMask(true)
//
//                        .setCropEngine(new CropFileEngine() {
//                            @Override
//                            public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
//                                UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
//                                destPath = destinationUri+"";
//                                System.out.println("-------------" + srcUri+"-----"+destinationUri+"-----"+dataSource+"-----"+requestCode);
//                                uCrop.setImageEngine(new UCropImageEngine() {
//                                    @Override
//                                    public void loadImage(Context context, String url, ImageView imageView) {
//                                    }
//
//                                    @Override
//                                    public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
//                                    }
//                                });
//
//                                UCrop.Options options = new UCrop.Options();
//                                options.setCircleDimmedLayer(true);
//                                options.setShowCropFrame(false);
//                                options.setShowCropGrid(false);
//                                uCrop.withOptions(options);
//                                uCrop.start(fragment.getActivity(), fragment, requestCode);
//                            }
//
//                        })
//                        .forResult(new OnResultCallbackListener<LocalMedia>() {
//                            @Override
//                            public void onResult(ArrayList<LocalMedia> result) {
//                                System.out.println("-------------" + result.get(0).getRealPath());
//
//                                Glide.with(EditProfileActivity.this).load(destPath).into(camera);
//
//                            }
//
//                            @Override
//                            public void onCancel() {
//                            }
//                        });

                break;
            case R.id.rl_name:
                break;
            case R.id.rl_signature:
                break;
            case R.id.rl_gender:
                showBottomDialog();
                break;
            case R.id.rl_birthday:
                pvCustomLunar.show();
                break;
            case R.id.rl_region:
                break;
            case R.id.edit_iv_back:
                finish();
                break;
        }
    }
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.edit_gender, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();//获取dialog的window对象
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_edit_gender_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_gender.setText("男");
                EDIT_GENDER = 1;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_edit_gender_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_gender.setText("女");
                EDIT_GENDER = 0;
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_edit_gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_gender.setText("未知");
                EDIT_GENDER = 1;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_edit_gender_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                getTime2view(year, month, day);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new CustomListener() {

                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();

                                pvCustomLunar.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });


                    }

                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }

    private void getTime2view(int year, int month, int day) {
        String str = year + "-" + month + "-" + day;
        tv_edit_birthday.setText(str);
    }
    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void initCustomTimePicker() {

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_edit_birthday.setText(getTime(date));
            }
        })

                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
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
                .setType(new boolean[]{false, false, false, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onDestroy() {
        saveAndFinish();
        super.onDestroy();


    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
        super.onBackPressed();


    }

    private void saveAndFinish() {

        Intent data = new Intent();

        data.putExtra(EXTRA_EDITED_BIRTHDAY, tv_edit_birthday.getText().toString());
//        data.putExtra(EXTRA_EDITED_SIGNATURE, tv_edit_signature.getText().toString());
//        data.putExtra(EXTRA_EDITED_NAME, tv_edit_name.getText().toString());
        data.putExtra(EXTRA_EDITED_GENDER, tv_edit_gender.getText().toString());
//        data.putExtra(EXTRA_EDITED_REGION, tv_edit_region.getText().toString());

        setResult(RESULT_OK, data);
        finish();
    }


}