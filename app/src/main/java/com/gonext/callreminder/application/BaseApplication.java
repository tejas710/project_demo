package com.gonext.callreminder.application;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.gonext.callreminder.utils.StaticUtils;

/**
 * Created by Lenovo on 17-05-2017.
 */

public class BaseApplication extends MultiDexApplication {

    public static boolean isAppWentToBg = true;

    public static BaseApplication baseApplication;

    @Override
    public void onCreate() {

        super.onCreate();
        baseApplication = this;
        MultiDex.install(this);

        StaticUtils.setWindowDimensions(this);
    }

    public static BaseApplication getInstance(){
        return baseApplication;
    }
}
