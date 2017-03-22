
package rainhu.com.demostore;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
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
import butterknife.OnClick;
import rainhu.com.demostore.activity.AboutActivity;
import rainhu.com.demostore.activity.AnimationActivity;
import rainhu.com.demostore.activity.TempActivity;
import rainhu.com.demostore.applock.AppLockActivity;
import rainhu.com.demostore.mediademo.MediaDemoActivity;
import rainhu.com.demostore.activity.StorageFillerActivity;
import rainhu.com.demostore.powershot.PowershotActivity;
import rainhu.com.demostore.binder.BinderDemoActivity;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.storageFillerBtn)
    Button storageFillerBtn;

    @InjectView(R.id.mediaDemoBtn)
    Button mediaDemoBtn;

    @InjectView(R.id.animationBtn)
    Button animationBtn;

    @InjectView(R.id.powershotBtn)
    Button powershotBtn;

    @InjectView(R.id.binderDemoBtn)
    Button binderDemoBtn;

    @InjectView(R.id.applockBtn)
    Button applockBtn;

    @InjectView(R.id.toolbar)
    Toolbar toolbar ;

    @InjectView(R.id.tempBtn)
    Button tempBtn;

    //FrameLayout mFragmentContainer;

    //Fragment currentFragment;


    private long lastBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);


        initToolBar();


        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new LogToolFragment();
            fm.beginTransaction()
              .add(R.id.fragment_container, fragment)
              .commit();
        }

        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);

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
            R.id.binderDemoBtn ,
            R.id.animationBtn,
            R.id.tempBtn,
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
            case R.id.binderDemoBtn:
                startActivity(new Intent(this, BinderDemoActivity.class));
                break;
            case R.id.animationBtn:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.tempBtn:
                startActivity(new Intent(this, TempActivity.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                Log.i("hzy","about");
                goAboutActivity();
                break;
        }
        return true;
    }


    private void goAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

//    private void switchFragment(android.support.v4.app.Fragment fragment, String title) {
//
//        if (currentFragment == null || !currentFragment.getClass().getName().equals(fragment.getClass().getName())) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
//                    .commit();
//        }
//        currentFragment = fragment;
//    }

}
