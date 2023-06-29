package com.george.memoshareapp.application;

import android.app.Application;

import com.amap.api.maps2d.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.george.memoshareapp.utils.LocationUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import es.dmoral.toasty.Toasty;


/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp
 * @className: MyApplication
 * @author: George
 * @description: TODO
 * @date: 2023/4/26 17:08
 * @version: 1.0
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private LatLng currentLatLng;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Toasty.Config.getInstance()
                .tintIcon(true)
                .allowQueue(false)
                .apply();

        LitePal.initialize(this);
        Fresco.initialize(this);
        new LocationUtil(this).getLocation(new LocationUtil.LocationCallback() {
            @Override
            public void onLocationBack(LatLng latLng) {
                currentLatLng = latLng;
                System.out.println("currentLatLng = " + currentLatLng);
            }
        });

        Logger.addLogAdapter(new AndroidLogAdapter());



    }
    public static MyApplication getInstance() {
        return instance;
    }
    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

}

