package rainhu.com.demonstore.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by hu on 16-11-6.
 */

public class LoggingActivity extends AppCompatActivity {

    protected String logTag = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(logTag,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(logTag,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(logTag,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(logTag,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(logTag,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(logTag,"onDestroy");
    }
}

