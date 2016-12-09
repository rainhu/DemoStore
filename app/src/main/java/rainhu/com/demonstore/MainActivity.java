package rainhu.com.demonstore;

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

import rainhu.com.demonstore.activity.AboutActivity;
import rainhu.com.demonstore.activity.AnimationActivity;
import rainhu.com.demonstore.Media.MediaDemoActivity;
import rainhu.com.demonstore.activity.StorageFillerActivity;
import rainhu.com.demonstore.powershot.PowershotActivity;
import rainhu.com.demonstore.activity.ThreadDemoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button storageFillerBtn;
    private Button mediaDemoBtn;
    private Button animationBtn;
    private Button powershotBtn;
    private Button threadBtn;

    private long lastBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageFillerBtn = (Button) findViewById(R.id.storageFillerBtn);
        storageFillerBtn.setOnClickListener(this);

        mediaDemoBtn = (Button) findViewById(R.id.mediaDemoBtn);
        mediaDemoBtn.setOnClickListener(this);

        animationBtn = (Button) findViewById(R.id.animationBtn);
        animationBtn.setOnClickListener(this);

        powershotBtn = (Button) findViewById(R.id.powershotBtn);
        powershotBtn.setOnClickListener(this);

        threadBtn = (Button) findViewById(R.id.threadBtn);
        threadBtn.setOnClickListener(this);

        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("zhengyu","activity touch");
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.storageFillerBtn:
                startActivity(new Intent(this, StorageFillerActivity.class));
                break;
            case R.id.mediaDemoBtn:
                startActivity(new Intent(this,MediaDemoActivity.class));
                break;
            case R.id.animationBtn:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.powershotBtn:
                startActivity(new Intent(this, PowershotActivity.class));
                break;
            case R.id.threadBtn:
                startActivity(new Intent(this, ThreadDemoActivity.class));
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if( curTime - lastBackPressedTime > 2000){
            Toast.makeText(this,"再次点击返回键将会退出！",Toast.LENGTH_SHORT).show();
            lastBackPressedTime = curTime;
        }else{
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
        Intent intent=new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
