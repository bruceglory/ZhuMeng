package com.example.bruce.zhumeng;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhang on 2016/5/16.
 */
public class MyApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
