package com.ryan.demostore.binder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by huzhengyu on 17-1-23.
 */

public class LocalService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //onBinder中返回binder,binder包含了service的句柄，客户端得到句柄以后就可以调用service的公共方法了
        return new LocalBinder();
    }

    public void satHello(){
        Toast.makeText(this.getApplicationContext(),"this is localService", Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder extends Binder{
        LocalService getService(){
            return LocalService.this;
        }
    }
}
