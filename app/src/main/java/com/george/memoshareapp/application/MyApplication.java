package com.george.memoshareapp.application;

import android.app.Application;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.george.memoshareapp.utils.LocationUtil;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.IOSStyle;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

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
        MMKV.initialize(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        LitePal.initialize(this);
        Fresco.initialize(this);
        DialogX.init(this);
        DialogX.globalStyle = IOSStyle.style();
        new LocationUtil(this).getLocation(new LocationUtil.LocationCallback() {
            @Override
            public void onLocationBack(LatLng latLng) {
                currentLatLng = latLng;
                System.out.println("currentLatLng = " + currentLatLng);
            }
        });


        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
                    @Override
                    public void onComplete(Void data) {
                        Log.e("TAG","隐私协议授权结果提交: 成功 " + data);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("TAG","隐私协议授权结果提交: 失败 " + throwable.getMessage());
                    }
                }
        );

    }
    public static MyApplication getInstance() {
        return instance;
    }
    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

}

