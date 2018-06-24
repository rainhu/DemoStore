package com.ryan.demostore.applock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by huzhengyu on 16-12-17.
 */

public class ApplockService extends Service {

    ActivityManager mAm ;
    Thread workThead;
    Context mContext;
    private static final int SLEEP_TIME = 100;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                checkTopActivityIsNeedLock();

                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mContext = this;

        workThead = new Thread(runnable);
        workThead.start();
    }


    private void checkTopActivityIsNeedLock(){
       // Activity topActivity =  AppLockUtils.getTopResumedActivity();


        String packageName = AppLockUtils.getTopRunningApp(mContext);

        if("rainhu.com.demostore".equals(packageName)){
            return;
        }

        String launcherPackageName = AppLockUtils.getLauncherPackageName(mContext);
        if(launcherPackageName.equals(packageName)){
            return;
        }


        int appStatus = AppLockUtils.getAppStatus(mContext, packageName);
        switch (appStatus){
            case AppLockMetadata.NEED_NOT_APPLOCK:
                break;
            case AppLockMetadata.NEED_APPLOCK:
                Log.i(AppLockMetadata.TAG,"need lock!");
                startLock();

                AppLockUtils.updateAppStatus(mContext, packageName,AppLockMetadata.ALREADY_UNLOCKED );
                break;
            case AppLockMetadata.ALREADY_UNLOCKED:
                break;
        }

    }

    private void startLock() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return  START_STICKY;
    }
}
