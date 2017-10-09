package rainhu.com.demostore.binder;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rainhu.com.demostore.R;

import android.util.Log;
import android.widget.Toast;
/**
 * Created by huzhengyu on 16-11-22.
 */

/***
 * HandlerThread
 * HandlerThread实际上是带有Looper的thread,
 * 启动方式
 * HandlerThread handlerThread = new HandlerThread("test);
 * handlerThread.start();
 * 在不需要线程Loop的时候需要调用HandlerThread.quitSafely() 或 HandlerThread.quit();
 *
 * Thread thread = ne Thread(new Runnable(){
 *     @Override
 *     public void run(){
 *         Looper.prepare();
 *         // do works
 *         ///...
 *         Looper.loop();
 *     }
 * });
 * thread.start();
 *
 *
 *
 */



public class BinderDemoActivity extends Activity {
    ServiceConnection localserviceCon;
    ServiceConnection remoteServiceCon;
    ServiceConnection AIDLRemoteServiceCon;

    IAIDLRemoteService mAIDLRemoteService;

    public static final int SAY_HELLO_TO_CLIENT = 0;

    @InjectView(R.id.binderDemoBtn_bindlocalservice)
    Button bindLocalServiceBrn;

    @InjectView(R.id.binderDemoBtn_unbindlocalservice)
    Button unbindLocalServiceBtn;

    @OnClick(R.id.binderDemoBtn_bindlocalservice)
    public void onBindLocalServiceBtnClicked(){
        Log.i("hzy","onLocalServiceBtnClicked");
        Intent service = new Intent(BinderDemoActivity.this,LocalService.class);
        this.bindService(service, localserviceCon, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.binderDemoBtn_unbindlocalservice)
    public void onUnbindLocalServiceBtnClicked(){
        this.unbindService(localserviceCon);
    }

    @InjectView(R.id.binderDemoBtn_bindremoteservice)
    Button bindRemoteServiceBtn;

    @OnClick(R.id.binderDemoBtn_bindremoteservice)
    public void onBindRemoteServiceBtnClicked(){
        Intent service = new Intent(BinderDemoActivity.this, RemoteService.class);
        this.bindService(service,remoteServiceCon,Context.BIND_AUTO_CREATE);
    }

    @InjectView(R.id.binderDemoBtn_unbindremoteservice)
    Button unbindRemoteServiceBtn;

    @OnClick(R.id.binderDemoBtn_unbindremoteservice)
    public void onUnbindRemoteServiceBtnClicked(){
        this.unbindService(remoteServiceCon);
    }

    @InjectView(R.id.binderDemoBtn_bindAIDLRemoteService)
    Button bindAIDLRemoteSeviceBtn;

    @OnClick(R.id.binderDemoBtn_bindAIDLRemoteService)
    public void onBindAIDLRemoteService(){
        Intent service = new Intent(BinderDemoActivity.this, AIDLRemoteService.class);
        bindService(service,new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("hzy","AIDL remote service connected");
                //这边的Serbice是BinderProxy
                //asInterface返回IAIDLRemoteService.Stub.Proxy
                mAIDLRemoteService = IAIDLRemoteService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("hzy","AIDL remote service disconnected");
                mAIDLRemoteService = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    @InjectView(R.id.binderDemoBtn_unbindAIDLRemoteService)
    Button unbindAIDLRemoteSeviceBtn;

    @OnClick(R.id.binderDemoBtn_unbindAIDLRemoteService)
    public void onUnbindAIDLRemoteService(){

    }

    class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAY_HELLO_TO_CLIENT:
                    Toast.makeText(BinderDemoActivity.this, "Hello remote client", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger messenger_receiver = new Messenger(new IncomingHandler());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binderdemo);

        ButterKnife.inject(this);

        PackageManager pm = getPackageManager();
        localserviceCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("hzy","service connected");
                LocalService localService = ((LocalService.LocalBinder)service).getService();
                localService.satHello();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("hzy","service disconnected");

            }
        };

        remoteServiceCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("hzy","remote service connected");

                //通过messenger发送消息到service, 通过service端传来的binder，用来构建messenger向service发送消息
                Messenger messenger = new Messenger(service);
                Message msg = new Message();
                msg.what = RemoteService.MSG_SAY_HELLO;
                msg.replyTo = messenger_receiver;

                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("hzy","remote service disconnected");
            }
        };


        AIDLRemoteServiceCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("hzy","AIDL remote service connected");
                //这边的Serbice是BinderProxy
                //asInterface返回IAIDLRemoteService.Stub.Proxy
                mAIDLRemoteService = IAIDLRemoteService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("hzy","AIDL remote service disconnected");
                mAIDLRemoteService = null;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("hzy","test");

       // connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void connect() {
        String myString = null;
        try{
            URL myURL = new URL("http://www.google.com/robots.txt");
            URLConnection ucon = myURL.openConnection();
            InputStream is = ucon.getInputStream();

        } catch (MalformedURLException e) {
            Log.e("hzy","e:"+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("hzy","e:"+e.toString());
            e.printStackTrace();
        }

    }

    //basic use of handler

    public void doOperation(){

        //从global线程池中获取message对象，很多情况下能够避免创建新的message对象
        Message msg = Message.obtain();
        msg.arg1 = 1;
        msg.arg2 = 2;
        msg.what = 3;
        msg.obj = new Object();
        //mHandler.sendMessage(msg);
        mHandler.sendMessageDelayed(msg,1000);
        //mHandler.sendEmptyMessage(int what);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //do time-consume work
                    break;
                default:
                    break;
            }

        }
    };

}
