package rainhu.com.demonstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import rainhu.com.demonstore.R;
import android.os.Process;

/**
 * Created by huzhengyu on 16-11-18.
 */

public class TempDemoActivity extends Activity {
    private Button mGetExclusiveCoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempdemo);

        mGetExclusiveCoreBtn = (Button) findViewById(R.id.getExclusiveCoreBtn);
    }

    public void onGetExclusiveCoreBtnClicked(View view){
        Log.i("zhengyu","onGetExclusiveCoreBtnClicked");

        int temp [] = Process.getExclusiveCores();
        Log.i("zhengyu"," length: "+temp.length);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("zhengyu","activity touch");
        return true;
    }
}
