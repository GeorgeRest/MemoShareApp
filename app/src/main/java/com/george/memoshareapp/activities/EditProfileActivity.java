package com.george.memoshareapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.george.memoshareapp.R;
import com.george.memoshareapp.beans.JsonBean;
import com.george.memoshareapp.beans.User;
import com.george.memoshareapp.dialog.LoadingDialog;
import com.george.memoshareapp.interfaces.UpdateUserListener;
import com.george.memoshareapp.manager.UserManager;
import com.george.memoshareapp.properties.AppProperties;
import com.george.memoshareapp.utils.GetJsonDataUtil;
import com.george.memoshareapp.utils.GlideEngine;
import com.google.gson.Gson;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_EDITED_GENDER = "edited_gender";
    public static final String EXTRA_EDITED_BIRTHDAY = "edited_birthday";
    public static final String EXTRA_EDITED_REGION = "edited_region";
    public static final String EXTRA_EDITED_SIGNATURE = "edited_signature";
    public static final String EXTRA_EDITED_NAME = "edited_name";
    public static final String EXTRA_EDITED_HEAD_PORTRAIT = "edited_head_portrait";

    private RelativeLayout rl_name;
    private RelativeLayout rl_signature;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_birthday;
    private RelativeLayout rl_region;
    private CircleImageView head_portrait;
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
    private String headPortraitPath;
    ActivityResultLauncher<Intent> activityResultLauncher;

    private String returnString;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static boolean isLoaded = false;
    private OptionsPickerView regionPicker;
    private TextView tv_complete;
    private String phoneNumber;
    private SharedPreferences.Editor edit;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        String headPortraitPath = sp.getString("headPortraitPath", AppProperties.DEFAULT_AVATAR);
        Glide.with(this).load(headPortraitPath).into(head_portrait);
        edit = sp.edit();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent intent = result.getData();
                            returnString = intent.getStringExtra("result");
                            tv_edit_name.setText(returnString);
                        } else if (result.getResultCode() == 2) {
                            Intent intent = result.getData();
                            returnString = intent.getStringExtra("result");
                            tv_edit_signature.setText(returnString);
                        }
                    }
                });
        initTimePicker();

        initLunarPicker();
        initCustomTimePicker();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {

//                        Toast.makeText(EditProfileActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(EditProfileActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(EditProfileActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_region = (RelativeLayout) findViewById(R.id.rl_region);
        head_portrait = (CircleImageView) findViewById(R.id.head_portrait);
        tv_complete = (TextView) findViewById(R.id.edit_tv_complete);
        rl_name.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_region.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        tv_edit_signature = (TextView) findViewById(R.id.tv_edit_signature);
        tv_edit_name = (TextView) findViewById(R.id.tv_edit_name);
        tv_edit_gender = (TextView) findViewById(R.id.tv_edit_gender);
        tv_edit_birthday = (TextView) findViewById(R.id.tv_edit_birthday);
        tv_edit_region = (TextView) findViewById(R.id.tv_edit_region);
        back = (ImageView) findViewById(R.id.edit_iv_back);
        head_portrait.setOnClickListener(this);
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        phoneNumber = sp.getString("phoneNumber", "");
        user = new UserManager(this).findUserByPhoneNumber(phoneNumber);
        getParamsFromUser();


    }

    private void getParamsFromUser() {

        String gender = user.getGender();
        if (gender != null) {
            tv_edit_gender.setText(gender);
        }
        String birthday = user.getBirthday();
        if (birthday == null || birthday.isEmpty()) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            tv_edit_birthday.setText(currentDate);
        } else {
            tv_edit_birthday.setText(birthday);
        }
        String name = user.getName();
        if (name != null) {
            tv_edit_name.setText(name);
        }
        String signature = user.getSignature();
        if (signature != null) {
            tv_edit_signature.setText(signature);
        }
        String region = user.getRegion();
        if (region != null) {
            tv_edit_region.setText(region);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.head_portrait:
                PictureSelector.create(this)
                        .openGallery(SelectMimeType.ofAll())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setMaxSelectNum(1)
                        .isEmptyResultReturn(true)
                        .isMaxSelectEnabledMask(true)
                        .setCropEngine(new CropFileEngine() {
                            @Override
                            public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
                                UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
                                destPath = destinationUri + "";
                                uCrop.setImageEngine(new UCropImageEngine() {
                                    @Override
                                    public void loadImage(Context context, String url, ImageView imageView) {

                                    }

                                    @Override
                                    public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                                    }
                                });
                                UCrop.Options options = new UCrop.Options();
                                options.setCircleDimmedLayer(true);
                                options.setShowCropFrame(false);
                                options.setShowCropGrid(false);
                                uCrop.withOptions(options);
                                uCrop.start(fragment.getActivity(), fragment, requestCode);
                            }

                        })
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                headPortraitPath = result.get(0).getRealPath();

                                Glide.with(EditProfileActivity.this).load(destPath).thumbnail(Glide.with(EditProfileActivity.this).load(R.drawable.photo_loading))
                                        .error(R.drawable.ic_close).into(head_portrait);edit.putString("headPortraitPath", headPortraitPath);
                                edit.commit();
                            }
                            @Override
                            public void onCancel() {
                            }
                        });
                break;
            case R.id.rl_name:
                Intent intent1 = new Intent(this, EditNameActivity.class);
                intent1.putExtra("user", user);
                activityResultLauncher.launch(intent1);
                break;
            case R.id.rl_signature:
                Intent intent2 = new Intent(this, EditSignatureActivity.class);
                intent2.putExtra("user", user);
                activityResultLauncher.launch(intent2);
                break;
            case R.id.rl_gender:
                showBottomDialog();
                break;
            case R.id.rl_birthday:
                birthdayScrollWheel();
                break;
            case R.id.rl_region:
                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(EditProfileActivity.this, "网络正忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.edit_iv_back:
                finish();
            case R.id.edit_tv_complete:
                LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                loadingDialog.startAnim();
                String name = tv_edit_name.getText().toString();
                String signature = tv_edit_signature.getText().toString();
                String region = tv_edit_region.getText().toString();
                String gender = tv_edit_gender.getText().toString();
                String birthday = tv_edit_birthday.getText().toString();
                //User updateUser = new User(phoneNumber, name, signature, gender, birthday, region, user.getBackGroundPath(), user.getHeadPortraitPath());
                User updateUser = new User(phoneNumber, name, signature, gender, birthday, region);
                Logger.d(updateUser);

//                new UserManager(this).updateUserInfo(updateUser,phoneNumber);
                MultipartBody.Part headPortraitPart = null;
                if (!TextUtils.isEmpty(headPortraitPath)) {
                    File headFile = new File(headPortraitPath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
                    headPortraitPart = MultipartBody.Part.createFormData("headPortrait", headFile.getName(), requestBody);
                } else {
                    headPortraitPath = sp.getString("headPortraitPath", AppProperties.DEFAULT_AVATAR);
                    File headFile = new File(headPortraitPath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
                    headPortraitPart = MultipartBody.Part.createFormData("headPortrait", headFile.getName(), requestBody);
                }

                new UserManager(this).updateUserInfo(updateUser, headPortraitPart, new UpdateUserListener() {
                    @Override
                    public void onUpdateUserListener(boolean isUpdated) {
                        loadingDialog.endAnim();
                        loadingDialog.dismiss();
                        if (isUpdated) {
                            Toasty.success(EditProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            saveAndFinish();
                        } else {
                            Toasty.error(EditProfileActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
    private void birthdayScrollWheel() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 11, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                getTime2view(year, month, day);
            }
        })
                .setContentTextSize(18)
                .setDividerColor(Color.parseColor("#FFBB86FC"))
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
                                Toast.makeText(EditProfileActivity.this, "生日设置成功~", Toast.LENGTH_SHORT).show();
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
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .setLineSpacingMultiplier(2.5f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .setDividerColor(0xFF24AD9D)
                .build();
        pvCustomTime.show();
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
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_edit_gender_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_gender.setText("女");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_edit_gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit_gender.setText("未知");
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
                .setDate(selectedDate).setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new CustomListener() {
                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_calender_cancel);
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
        }).setDate(selectedDate).setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
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
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveAndFinish() {

        Intent data = new Intent();
        data.putExtra(EXTRA_EDITED_BIRTHDAY, tv_edit_birthday.getText().toString());
        data.putExtra(EXTRA_EDITED_SIGNATURE, tv_edit_signature.getText().toString());
        data.putExtra(EXTRA_EDITED_NAME, tv_edit_name.getText().toString());
        data.putExtra(EXTRA_EDITED_GENDER, tv_edit_gender.getText().toString());
        data.putExtra(EXTRA_EDITED_REGION, tv_edit_region.getText().toString());
        data.putExtra(EXTRA_EDITED_HEAD_PORTRAIT, sp.getString("headPortraitPath", AppProperties.DEFAULT_AVATAR));
        setResult(RESULT_OK, data);
        finish();
    }


    private void showPickerView() {

        regionPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String regionMessage = opt1tx + "·" + opt2tx;
                tv_edit_region.setText(regionMessage);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final ImageView im_ensure = (ImageView) v.findViewById(R.id.im_ensure);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        im_ensure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                regionPicker.returnData();
                                regionPicker.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                regionPicker.dismiss();
                            }
                        });
                    }
                })
                .isDialog(false)
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(true)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        regionPicker.setPicker(options1Items, options2Items, options3Items);//三级选择器
        regionPicker.show();
    }

    private void initJsonData() {

        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");

        ArrayList<JsonBean> jsonBean = parseData(JsonData);

        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


}