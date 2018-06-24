package com.ryan.demostore.jniDemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.ryan.demostore.R;

/**
 * Created by Ryan on 2017/9/8.
 */

public class LedActivity extends Activity {
    private Button blueBtn;
    private Button yellowBtn;
    private Button greenBtn;
    private Button redBtn;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        //init();

    }

    private void init() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.icon = R.drawable.ball;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xFF0000FF;
        mNotificationManager.notify(0, notification);

        blueBtn = (Button) findViewById(R.id.blueBtn);
        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification();
                notification.defaults = Notification.DEFAULT_LIGHTS;
                notification.icon = R.drawable.ball;
                notification.flags = Notification.FLAG_SHOW_LIGHTS;
                notification.ledARGB = 0xFF0000FF;
                mNotificationManager.notify(0, notification);
            }
        });
        greenBtn = (Button) findViewById(R.id.greenBtn);
        redBtn = (Button) findViewById(R.id.redBtn);
        yellowBtn = (Button) findViewById(R.id.yellowBtn);

    }

    private native int openBlue();

    static {
        System.loadLibrary("hello-jni");
    }
}
