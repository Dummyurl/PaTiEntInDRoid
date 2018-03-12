package com.ziffytech.activities;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


/**
 * Created by LENOVO on 8/18/2016.
 */
public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();


    }
}
