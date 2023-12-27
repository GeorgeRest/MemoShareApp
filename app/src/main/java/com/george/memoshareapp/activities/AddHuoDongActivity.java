package com.george.memoshareapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.amap.api.maps2d.model.LatLng;
import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.AddActivityPicAdapter;
import com.george.memoshareapp.application.MyApplication;
import com.george.memoshareapp.interfaces.OnDelPicClickListener;
import com.george.memoshareapp.manager.HuodongManager;
import com.george.memoshareapp.utils.LocationUtil;
import com.george.memoshareapp.utils.SHA1Output;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.io.IOException;
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
    private double altitude;
    private double longtitude;
    private String aoiName;
    private String address;
    private String district;
    private LocationManager manager;
    private String provider;
    private Location location;
    private StringBuilder sb;
    private String countryName;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

//        initData();
        tv_location = (TextView) findViewById(R.id.tv_location);
        initMyData();
        initView();

    }

    @SuppressLint("MissingPermission")
    private void initMyData() {
        huodongManager = new HuodongManager(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        altitude = currentLatLng.latitude;
//        longtitude = currentLatLng.longitude;
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            askLocationSettings();
        }
        provider = LocationManager.GPS_PROVIDER;

        location = manager.getLastKnownLocation(provider);
        if(location !=null){
            //显示当前设备的位置信息
            Log.d("lidu---", "location!=null");
            showLocation(location);
            getPositionName(location);
        }

        Log.d("lidu---", "location==null");
        manager.requestLocationUpdates(provider, 10000, 5, locationListener);
    }
    LocationListener locationListener=new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            Log.d("test", "onLocationChanged");
            //更新当前设备的位置信息
            showLocation(location);
            getPositionName(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    };

    private void getPositionName(Location location) {
        List<Address> addsList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this);
        try {
            addsList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);//得到的位置可能有多个当前只取其中一个
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (addsList != null && addsList.size() > 0) {
            for (int i = 0; i < addsList.size(); i++) {
                Address ads = addsList.get(i);
                countryName = ads.getCountryName();
                //拿到城市
                cityName = ads.getLocality();
            }
        }
        if(countryName == null || cityName == null){
            Log.d(TAG, "initView: SHA1Output = "+SHA1Output.sHA1(this));
            tv_location.setText("请重新进入以获取位置信息");
        }else {
            tv_location.setText(countryName + "/" + cityName);
        }
    }

    private void showLocation(final Location location){
        altitude = location.getLatitude();
        longtitude = location.getLongitude();
    }

    private void askLocationSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("开启位置服务");
        builder.setMessage("本应用需要开启位置服务，是否去设置界面开启位置服务？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AddHuoDongActivity.this.startActivity(intent);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(AddHuoDongActivity.this, "No location provider to use",
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

//    private void initData() {
//        huodongManager = new HuodongManager(this);
//        try {
//            AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
//            AMapLocationClientOption mLocationClientOption = new AMapLocationClientOption();
//            mLocationClientOption.setNeedAddress(true);
//            mLocationClientOption.isNeedAddress();
//            mLocationClientOption.setOnceLocation(true);
//            mLocationClient.setLocationOption(mLocationClientOption);
//            mLocationClient.startLocation();
//            mLocationClient.setLocationListener(new AMapLocationListener() {
//                @Override
//                public void onLocationChanged(AMapLocation aMapLocation) {
//                    Log.d(TAG, "onLocationChanged: 执行到这里");
//                    if (aMapLocation != null) {
//                        Log.d(TAG, "onLocationChanged: 执行到大if这里");
//                        if (aMapLocation.getErrorCode() == 0) {
//                            //可在其中解析amapLocation获取相应内容。
//                            Log.d(TAG, "onLocationChanged: 执行到if这里");
//                            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                            //获取纬度
//                            altitude = aMapLocation.getLatitude();
//                            //获取经度
//                            longtitude = aMapLocation.getLongitude();
//                            aMapLocation.getAccuracy();//获取精度信息
//                            //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                            address = aMapLocation.getAddress();
//                            aMapLocation.getCountry();//国家信息
//                            aMapLocation.getProvince();//省信息
//                            aMapLocation.getCity();//城市信息
//                            //城区信息
//                            district = aMapLocation.getDistrict();
//                            aMapLocation.getStreet();//街道信息
//                            aMapLocation.getStreetNum();//街道门牌号信息
//                            aMapLocation.getCityCode();//城市编码
//                            aMapLocation.getAdCode();//地区编码
//                            //获取当前定位点的AOI信息
//                            aoiName = aMapLocation.getAoiName();
//                            Log.d(TAG, "onLocationChanged: aoiName : " + aoiName);
//                        }else {
//                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                            Log.e("地图错误","定位失败, 错误码:" + aMapLocation.getErrorCode() + ", 错误信息:"
//                                    + aMapLocation.getErrorInfo());
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }



    private void initView() {
        et_huodong_content = (EditText) findViewById(R.id.et_huodong_content);
        tv_publish = (TextView) findViewById(R.id.tv_publish);
        rcy_activity_pic = (RecyclerView) findViewById(R.id.rcy_activity_pic);
        imageUriList = new ArrayList<>();
        adapter = new AddActivityPicAdapter(this,imageUriList);
        rcy_activity_pic.setAdapter(adapter);
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
                        countryName == null || countryName.isEmpty() || cityName == null || cityName.isEmpty() ?
                                "非洲/阿拉巴马州":countryName + "/" + cityName,longtitude,altitude,date,false,0);
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