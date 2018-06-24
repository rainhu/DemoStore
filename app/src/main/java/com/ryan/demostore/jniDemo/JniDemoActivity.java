package com.ryan.demostore.jniDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ryan.demostore.R;

/**
 * Created by Ryan on 2017/8/30.
 */

public class JniDemoActivity extends Activity {
    private TextView mText;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jnidemo_activity_main);
        mText = (TextView) findViewById(R.id.jniDemo_tipBtn);
        mText.setText(getTextFromJni());

        mButton = (Button) findViewById(R.id.ledBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JniDemoActivity.this, LedActivity.class));
            }
        });

        openMtGpio();
    }

    private native String getTextFromJni();
    private native int openMtGpio();

    public native int add(int a, int b);
    public native int openGpioDev();
    public native int getGpio(int num);
    public native int releaseGpio(int num);
    public native int setGpioState(int num, int state);



    static {
        System.loadLibrary("hello-jni");
    }
}
