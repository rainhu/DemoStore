package com.ryan.demostore;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ryan on 2018/2/10.
 */

public class APP extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getGlobalContext(){
        return mContext;
    }
}
