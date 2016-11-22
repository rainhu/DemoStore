package rainhu.com.demonstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import rainhu.com.demonstore.R;

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
public class ThreadDemoActivity  extends Activity{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threaddemo);
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



}
