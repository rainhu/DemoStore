package com.ryan.demostore.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by huzhengyu on 16-12-17.
 */

public class ApplockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null){
            return;
        }


        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.i(AppLockMetadata.TAG,"boot_complete");


        }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            Log.i(AppLockMetadata.TAG,"ACTION_SCREEN_OFF");
            AppLockUtils.recoverAllAppsFromUnlockedToNeedLockStatus(context);
        }

    }
}
