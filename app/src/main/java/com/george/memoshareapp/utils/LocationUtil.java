package com.george.memoshareapp.utils;

import android.content.Context;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;

public class LocationUtil {

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private LocationCallback mLocationCallback;

    public LocationUtil(Context context) {
        AMapLocationClient.setApiKey("b73d5e0ad525991966aed0de9c8cecc5");
        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        try {
            locationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);
        locationOption.setOnceLocationLatest(true);
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(mLocationListener);
    }

    public interface LocationCallback {
        void onLocationBack(LatLng latLng);
    }

    public void getLocation(LocationCallback callback) {
        this.mLocationCallback = callback;
        locationClient.startLocation();
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    // 解析定位结果
                    LatLng result = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    if (mLocationCallback != null) {
                        mLocationCallback.onLocationBack(result);
                    }
                } else {

                }
            }
        }
    };
}
