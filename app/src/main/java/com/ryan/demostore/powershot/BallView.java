package com.ryan.demostore.powershot;


import android.app.ActivityManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.text.DecimalFormat;
import java.util.List;

import com.ryan.demostore.R;

/**
 * Created by hu on 16-12-1.
 */

public class BallView extends FrameLayout implements View.OnLongClickListener{
    //    private Button startBtn;
    private TextView tipView;

    private WindowManager mWindowManager;
    private ActivityManager mActivityManager;
    private WindowManager.LayoutParams mParams;
    private Context mContext;

    //记录点击时候球内部的坐标
    private float downInviewX;
    private float downInviewY;

    //记录点击时候相对于屏幕的坐标
    private float downInScreenX;
    private float downInScreenY;

    //记录手指up后，相对于屏幕的坐标
    private float upInScreenX;
    private float upInScreenY;

    public BallView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.ball_view, this);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        mContext = context;

//        startBtn = (Button) findViewById(R.id.startshot);
//        startBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        tipView = (TextView) findViewById(R.id.tips);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downInviewX = event.getX();
                downInviewY = event.getY();
                downInScreenX = event.getRawX();
                downInScreenY = event.getRawY() - getStatusBarHeight();

                //这里进行赋值是为了在up的时候判断球的位置有无移动
                upInScreenX = event.getRawX();
                upInScreenY =  event.getRawY() - getStatusBarHeight();
                break;

            case MotionEvent.ACTION_MOVE:
                upInScreenX = event.getRawX();
                upInScreenY = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:

                if(downInScreenX == upInScreenX && downInScreenY  == upInScreenY){
                    //表示点击球
                    onBallClicked();
                }

                break;
            default:
                break;
        }

        return true;
    }

    private void onBallClicked() {

        // List<ActivityManager.RunningAppProcessInfo> list =  mActivityManager.getRunningAppProcesses();

        long beforeClearAvailMem = Utils.getAvailMem(mContext);
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();


        if(processes == null) {
            Toast.makeText(mContext, "no process need to clear !", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0; i< processes.size();i++){
            //ActivityManager.RunningAppProcessInfo appProcessInfo = processes.get(i);
            AndroidAppProcess process = processes.get(i);
            if(process.getPackageName().contains("powershot"))
                continue;
            mActivityManager.killBackgroundProcesses(process.getPackageName());

        }

        long afterClearAvailMem = Utils.getAvailMem(mContext);
        DecimalFormat df = new DecimalFormat("0.00");
        float freedMemFloat =  (float)(afterClearAvailMem-beforeClearAvailMem)/1024/1024;
        String freedMem =  freedMemFloat >= 0 ?  df.format (freedMemFloat)  : 0+"";

        Toast.makeText(mContext, "clear finished ! "+ freedMem +" MB free ", Toast.LENGTH_SHORT).show();
    }


    public void setTipText(String tips){
        tipView.setText(tips);
    }
    public void setParams(WindowManager.LayoutParams p){
        mParams = p;
    }

    private void dismiss(){
        this.dismiss();

    }

    private int getStatusBarHeight(){
        return Utils.getStatusBarHeight(mContext);
    }

    private void updateViewPosition(){
        mParams.x = (int)(upInScreenX - downInviewX);
        mParams.y = (int)(upInScreenY - downInviewY);
        //CLog.i("mParams.x : "+mParams.x+"  mParams.y : "+mParams.y);
        mWindowManager.updateViewLayout(this, mParams);
    }

    @Override
    public boolean onLongClick(View v) {

        //dismiss();
        Log.i("zhengyu","exit");

        return true;
    }
}