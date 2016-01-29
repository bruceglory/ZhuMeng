package com.example.bruce.zhumeng.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by bruce on 2016/1/21.
 */
public class ResourceGetUtils {

    public static String getStringFromResource(Context context,int res){
        return context.getResources().getString(res);
    }


}
