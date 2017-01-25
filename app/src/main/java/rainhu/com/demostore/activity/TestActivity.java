package rainhu.com.demostore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

import rainhu.com.demostore.logger.Log;

/**
 * Created by huzhengyu on 17-1-17.
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hzy","onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showNfcDialogAndExit();
    }

    private void showNfcDialogAndExit() {
        AlertDialog.Builder alert2 = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alert2.setTitle("dialog 2");
        alert2.setMessage("bbb");
        alert2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert2.setNegativeButton("cancle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alert2.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        
        alert2.show();
                //alert2.show();
    }

    @Override
    protected void onDestroy() {
        Log.i("hzy","onDestroy");
        super.onDestroy();
    }
}
