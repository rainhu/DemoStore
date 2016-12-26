package rainhu.com.demostore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import rainhu.com.demostore.activity.AboutActivity;
import rainhu.com.demostore.activity.AnimationActivity;
import rainhu.com.demostore.activity.LogToolActivity;
import rainhu.com.demostore.applock.AppLockActivity;
import rainhu.com.demostore.mediademo.MediaDemoActivity;
import rainhu.com.demostore.activity.StorageFillerActivity;
import rainhu.com.demostore.powershot.PowershotActivity;
import rainhu.com.demostore.activity.ThreadDemoActivity;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.storageFillerBtn)
    Button storageFillerBtn;

    @InjectView(R.id.mediaDemoBtn)
    Button mediaDemoBtn;

    @InjectView(R.id.animationBtn)
    Button animationBtn;

    @InjectView(R.id.powershotBtn)
    Button powershotBtn;

    @InjectView(R.id.threadBtn)
    Button threadBtn;

    @InjectView(R.id.applockBtn)
    Button applockBtn;


    @InjectView(R.id.logToolBtn)
    Button logtoolBtn;

    private long lastBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @OnClick( {
            R.id.storageFillerBtn,
            R.id.mediaDemoBtn ,
            R.id.applockBtn ,
            R.id.powershotBtn ,
            R.id.threadBtn ,
            R.id.animationBtn,
            R.id.logToolBtn
    })
    public void pickView(View view){
        switch (view.getId()){
            case R.id.storageFillerBtn:
                startActivity(new Intent(this, StorageFillerActivity.class));
                break;
            case R.id.mediaDemoBtn:
                startActivity(new Intent(this, MediaDemoActivity.class));
                break;
            case R.id.applockBtn:
                startActivity(new Intent(this, AppLockActivity.class));
                break;
            case R.id.powershotBtn:
                startActivity(new Intent(this, PowershotActivity.class));
                break;
            case R.id.threadBtn:
                startActivity(new Intent(this, ThreadDemoActivity.class));
                break;
            case R.id.animationBtn:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.logToolBtn:
                startActivity(new Intent(this, LogToolActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastBackPressedTime > 2000) {
            Toast.makeText(this, "再次点击返回键将会退出！", Toast.LENGTH_SHORT).show();
            lastBackPressedTime = curTime;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_about:
                    goAboutActivity();
                    break;
            }
            return true;
        }


    };

    private void goAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
