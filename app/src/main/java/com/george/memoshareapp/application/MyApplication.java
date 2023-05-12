package com.george.memoshareapp.application;

import android.app.Application;

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
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Toasty.Config.getInstance()
                .tintIcon(true)
                .allowQueue(false)
                .apply();

        LitePal.initialize(this);
    }
    public static MyApplication getInstance() {
        return instance;
    }

}

