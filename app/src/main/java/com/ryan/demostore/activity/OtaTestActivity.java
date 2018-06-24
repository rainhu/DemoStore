package com.ryan.demostore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.File;
import java.io.IOException;

import butterknife.InjectView;
import butterknife.OnClick;
import com.ryan.demostore.R;

/**
 * Created by rainhu on 17-5-16.
 */

public class OtaTestActivity extends Activity{

    @InjectView(R.id.otaDialogBtn)
    Button otaDialogBtn;

    @InjectView(R.id.factoryReset)
    CheckBox factoryReset;

    @InjectView(R.id.enterpriseReset)
    CheckBox enterpriseReset;

    @OnClick(R.id.otaDialogBtn)
    public void onotaDialogBtnClicked() {
        try {
            RecoverySystem.installPackage(this, new File("/data/update.zip"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
