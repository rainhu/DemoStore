package rainhu.com.demostore.applock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by huzhengyu on 16-12-17.
 */

public class ApplockService extends Service {

    ActivityManager mAm ;
    Thread workThead;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {





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
        workThead = new Thread(runnable);
        workThead.start();
    }


    public void isNeedLock(){

    }
}
