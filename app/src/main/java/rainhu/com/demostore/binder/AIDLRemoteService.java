package rainhu.com.demostore.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by huzhengyu on 17-1-25.
 */

public class AIDLRemoteService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        if (IAIDLRemoteService.class.getName().equals(intent.getAction())){
            return mRemoteBinder;
        }
        return null;
    }

    //匿名内部类
    IAIDLRemoteService.Stub mRemoteBinder = new IAIDLRemoteService.Stub(){
        @Override
        public void sayHello() throws RemoteException {
            Toast.makeText(AIDLRemoteService.this, "hello , I'm aidlremoteservice",Toast.LENGTH_SHORT).show();
        }
    };
}
