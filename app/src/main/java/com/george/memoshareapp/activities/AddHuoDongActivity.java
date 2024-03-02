package com.george.memoshareapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.AddActivityPicAdapter;
import com.george.memoshareapp.beans.Post;
import com.george.memoshareapp.interfaces.OnDelPicClickListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.george.memoshareapp.utils.SHA1Output;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddHuoDongActivity extends AppCompatActivity {
    public static final String TAG = "AddHuoDongActivity";
    private String phoneNumber;
    private List<Uri> imageUriList = new ArrayList<>();
    private RecyclerView rcy_activity_pic;
    private AddActivityPicAdapter adapter;
    private EditText et_huodong_content;
    private TextView tv_location;
    private TextView tv_publish;
    private HuodongManager huodongManager;
    private double latitude;
    private double longtitude;

    private String address;
    private String location;
    private boolean isFollowing;
    private TextView tv_add_huodong_title;
    private int followId;
    private ConstraintLayout cl_loc_zone;
    private ConstraintLayout cl_tag_zone;
    public static final int TAG_REQUES_CODE = 5;
    private String tagResult = "";
    private TextView tv_huodong_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AMapLocationClient.updatePrivacyShow(this.getApplicationContext(),true,true);
        AMapLocationClient.updatePrivacyAgree(this.getApplicationContext(),true);

        setContentView(R.layout.activity_add_huo_dong);

        XXPermissions.with(this)
                .permission(Permission.ACCESS_COARSE_LOCATION)//网络
                .permission(Permission.ACCESS_FINE_LOCATION)//GPS定位
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            Toasty.info(AddHuoDongActivity.this, "部分权限未正常授予,可能会导致某些功能无法正常使用", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            Toasty.error(AddHuoDongActivity.this, "获取权限失败，请手动授予权限", Toast.LENGTH_SHORT, true).show();
                            XXPermissions.startPermissionActivity(AddHuoDongActivity.this, permissions);
                        }
                    }
                });

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        isFollowing = intent.getBooleanExtra("isFollowing", false);
        followId = intent.getIntExtra("followId", 0);


//        initData();
        tv_location = (TextView) findViewById(R.id.tv_location);

        initData();

        initView();

    }

    private void initData() {
        huodongManager = new HuodongManager(this);
        try {
            AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
            AMapLocationClientOption mLocationClientOption = new AMapLocationClientOption();
            mLocationClientOption.setNeedAddress(true);
            mLocationClientOption.isNeedAddress();
            mLocationClientOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationClientOption);
            mLocationClient.startLocation();
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    Log.d(TAG, "onLocationChanged: SHA1 : "+SHA1Output.sHA1(AddHuoDongActivity.this).toString());
                    Log.d(TAG, "onLocationChanged: 执行到这里");
                    if (aMapLocation != null) {
                        Log.d(TAG, "onLocationChanged: 执行到大if这里");
                        if (aMapLocation.getErrorCode() == 0) {
                            //可在其中解析amapLocation获取相应内容。
                            Log.d(TAG, "onLocationChanged: 执行到if这里");
                            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                            //获取纬度
                            latitude = aMapLocation.getLatitude();
                            //获取经度
                            longtitude = aMapLocation.getLongitude();
                            aMapLocation.getAccuracy();//获取精度信息
                            //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                            address = aMapLocation.getAddress();
                            String province = aMapLocation.getProvince();//省信息
                            String city = aMapLocation.getCity();//城市信息
                            String street = aMapLocation.getStreet();//街道信息
                            aMapLocation.getStreetNum();//街道门牌号信息
                            //获取当前定位点的AOI信息
                            String aoiName = aMapLocation.getAoiName();
                            location = province + city + street + aoiName;
                            tv_location.setText(location == null || location.isEmpty() ? "请选择位置" : location);
                            Log.d(TAG, "onLocationChanged: location : " + location);
                        }else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("地图错误","定位失败, 错误码:" + aMapLocation.getErrorCode() + ", 错误信息:"
                                    + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//mLocationClient.setLocationListener(new
//
//    AMapLocationListener() {
//        @Override
//        public void onLocationChanged (AMapLocation aMapLocation){
//            if (aMapLocation != null) {
//                if (aMapLocation.getErrorCode() == 0) {
//                    altitude = aMapLocation.getLatitude();
//                    longtitude = aMapLocation.getLongitude();
//                    address = aMapLocation.getAddress();
//                } else {
//                    Log.e("地图错误", "定位失败, 错误码:" + aMapLocation.getErrorCode() + ", 错误信息:"
//                            + aMapLocation.getErrorInfo());
//                }
//            }
//        }
//});论文用



    private void initView() {
        et_huodong_content = (EditText) findViewById(R.id.et_huodong_content);
        tv_publish = (TextView) findViewById(R.id.tv_publish);
        cl_loc_zone = (ConstraintLayout) findViewById(R.id.cl_loc_zone);
        tv_add_huodong_title = (TextView) findViewById(R.id.tv_add_huodong_title);
        tv_add_huodong_title.setText(isFollowing ? "活动跟拍" : "活动发布");
        rcy_activity_pic = (RecyclerView) findViewById(R.id.rcy_activity_pic);
        imageUriList = new ArrayList<>();
        adapter = new AddActivityPicAdapter(this,imageUriList);
        rcy_activity_pic.setAdapter(adapter);
        tv_huodong_tag = (TextView) findViewById(R.id.tv_huodong_tag);

        cl_tag_zone = (ConstraintLayout) findViewById(R.id.cl_tag_zone);

        cl_tag_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddHuoDongActivity.this, TageActivity.class);
                startActivityForResult(intent, TAG_REQUES_CODE);
            }
        });
        cl_loc_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddHuoDongActivity.this, MapLocationActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        rcy_activity_pic.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        adapter.setOnDelPicClickListener(new OnDelPicClickListener() {
            @Override
            public void onPicDelete(int index) {
                imageUriList.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
        tv_publish.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(!(imageUriList.size() > 0)){
                    Toast.makeText(AddHuoDongActivity.this,"请选择至少一张照片",Toast.LENGTH_SHORT).show();
                    return;
                }
                Date date = new Date(System.currentTimeMillis());
                Log.d(TAG, "onClick: imageUriList" + imageUriList.size());
                huodongManager.uploadHuodong(AddHuoDongActivity.this,imageUriList,phoneNumber,et_huodong_content.getText().toString().trim(),
                        location == null || location.isEmpty() ? "非洲阿拉巴马州" : location,longtitude, latitude,date,isFollowing,followId,tagResult);
            }
        });

        cl_loc_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddHuoDongActivity.this, MapLocationActivity.class), 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                switch (requestCode){
                    case AddActivityPicAdapter.REQUEST_CODE_CHOOSE:
                        getPhotoFromAlbum(data);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Post post = (Post) data.getSerializableExtra("publishContent");
                        latitude = post.getLatitude();
                        longtitude = post.getLongitude();
                        location = post.getLocation();
                        tv_location.setText(location);
                        break;
                    case TAG_REQUES_CODE:
                        if(data != null){
                            tagResult = data.getStringExtra("tagResult");
                            if(tagResult.isEmpty()){
                                tv_huodong_tag.setText("无标签");
                            }else {
                                tv_huodong_tag.setText(tagResult);
                            }
                        }
                        break;
                }
                break;
        }

    }

    private void getPhotoFromAlbum(Intent data) {
        if (data != null){
            List<LocalMedia> selectList = PictureSelector.obtainSelectorList(data);
            for (LocalMedia localMedia:selectList) {
                Logger.d(localMedia.getPath());
            }
            if (selectList != null ) {
                int count = selectList.size();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = Uri.parse(selectList.get(i).getPath());
                    if(imageUriList.size() != 0){
                        imageUriList.add(imageUriList.size() - 1, imageUri);
                    }else {
                        imageUriList.add(imageUri);
                    }
                }
            } else {
                return;
            }
        }
    }
}