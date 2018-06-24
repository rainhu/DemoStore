package com.ryan.demostore.binder;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Created by huzhengyu on 17-1-23.
 */

/*
* 构建一个Messenger，包含一个handler，然后将messenger的binder传给客户端，客户端可以通过handler再构造一个messenger与service通信
*
* */

public class RemoteService extends Service {
    public static final int MSG_SAY_HELLO = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    Handler incomingHandler = new Handler(){

        public void handleMessage(Message msg){
            if(msg.replyTo != null){
                Message msg_client = this.obtainMessage();
                msg_client.what = BinderDemoActivity.SAY_HELLO_TO_CLIENT;
                try {
                    msg.replyTo.send(msg_client);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            switch (msg.what){
                case MSG_SAY_HELLO:
                    Toast.makeText(RemoteService.this, "Hello this is remote Service !", Toast.LENGTH_SHORT).show();
            }

            super.handleMessage(msg);
        }
    };

    Messenger messenger = new Messenger(incomingHandler);

}
