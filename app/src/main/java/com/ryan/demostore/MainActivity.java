
package com.ryan.demostore;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ryan.demostore.activity.AboutActivity;
import com.ryan.demostore.fragment.CameraTestFragment;
import com.ryan.demostore.fragment.MediaDemoFragment;
import com.ryan.demostore.fragment.StorageFillerFragment;
import com.ryan.demostore.activity.BaseActivity;


public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private NavigationView mNavigationView;

    private long lastBackPressedTime = 0;
    private int currentFragmentId;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) ;
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if(savedInstanceState == null){
            currentFragmentId = 0;
            Fragment currentFragment = new StorageFillerFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.contentLayout,currentFragment,"content");
            ft.commit();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNavigationViewHeader();
        initFragment();
    }

    private void initFragment() {

    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    private void initNavigationViewHeader() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }

    private class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment currentFragment = null;
            switch (item.getItemId()){
                case R.id.storagefiller:
                    currentFragment = new StorageFillerFragment();
                    break;
                case R.id.mediademo:
                    currentFragment = new MediaDemoFragment();
                    break;
                case R.id.cameratest:
                    currentFragment = new CameraTestFragment();
                    break;

            }

            if(currentFragment != null){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contentLayout,currentFragment,"content");
                ft.commit();
            }

            mDrawerLayout.closeDrawer(Gravity.START);
            return false;
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            long curTime = System.currentTimeMillis();
            if (curTime - lastBackPressedTime > 2000) {
                Toast.makeText(this, "再次点击返回键将会退出！", Toast.LENGTH_SHORT).show();
                lastBackPressedTime = curTime;
            } else {
                super.onBackPressed();
            }
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
}
