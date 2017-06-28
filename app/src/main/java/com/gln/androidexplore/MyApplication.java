package com.gln.androidexplore;

import android.app.Application;
import android.content.Context;

import com.gln.androidexplore.util.LogUtils;

/**
 * Created by guolina on 2017/6/22.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.level = LogUtils.LEVEL_VERBOSE;
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
