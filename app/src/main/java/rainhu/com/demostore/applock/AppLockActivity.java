package rainhu.com.demostore.applock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rainhu.com.demostore.R;


/**
 * Created by hu on 16-12-14.
 */

public class AppLockActivity extends Activity {
    private List<AppInfo> appInfoList;
    private Context mContext;

    private ListView mAppListView;
    AppListAdapter mAppListAdapter = null;

    LinearLayout mContentLayout;
    LinearLayout mLodingLayout;

    private AppLoaderTask mLoaderTask = null;

//
//    HandlerThread = new HandlerThread
//    HandlerThread ht = new HandlerThread(){}




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        mContext = this;
        mContentLayout = (LinearLayout) findViewById(R.id.applock_content);
        mLodingLayout = (LinearLayout) findViewById(R.id.applock_loading);
        mAppListView = (ListView) findViewById(R.id.applock_applist);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoaderTask = new AppLoaderTask();
        mLoaderTask.execute();
    }

    class AppLoaderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("zhengyu", "onPreExecute");
            mLodingLayout.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAllApps(mContext);
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAppListAdapter = new AppListAdapter(mContext, appInfoList);
            mAppListView.setAdapter(mAppListAdapter);

            mAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AppListAdapter.AppInfoViewHolder viewholder = (AppListAdapter.AppInfoViewHolder) view.getTag();
                    final AppInfo appInfo = mAppListAdapter.getItem(position);
                    int newStatus;
                    if (viewholder.aSwitch.isChecked()) {
                        viewholder.aSwitch.setChecked(false);
                        newStatus = AppLockMetadata.NEED_APPLOCK;
                    } else {
                        viewholder.aSwitch.setChecked(true);
                        newStatus = AppLockMetadata.NEED_APPLOCK;
                    }
                    AppLockUtils.updateAppStatus(mContext, appInfoList.get(position).getPackageName(), newStatus);
                }
            });

            mLodingLayout.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);

        }
    }

    private void getAllApps(Context context) {
        appInfoList = new ArrayList<AppInfo>();


        PackageManager mManager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = mManager.queryIntentActivities(mainIntent, 0);

        //根据名字排序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mManager));
        if (appInfoList != null) {
            appInfoList.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String packageName = reInfo.activityInfo.packageName;

                if ("rainhu.com.demostore".equals(packageName)) {
                    continue;
                }

                String activityName = reInfo.activityInfo.name;
                String appLabel = (String) reInfo.loadLabel(mManager);
                Drawable icon = reInfo.loadIcon(mManager);

                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(packageName, activityName));

                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setAppIcon(icon);
                appInfo.setPackageName(packageName);
                appInfo.setStatus(AppLockMetadata.NEED_NOT_APPLOCK);
                appInfoList.add(appInfo);
            }
        }
    }

    private void insertAppInfoToDb() {
        getAllApps(mContext);

        ContentValues[] arrayValues = new ContentValues[appInfoList.size()];

        for (int i = 0; i < appInfoList.size(); i++) {

            String packageName = appInfoList.get(i).getPackageName();
            String appName = appInfoList.get(i).getAppLabel();
            Drawable icon = appInfoList.get(i).getAppIcon();

            //add the packageName,isNeedLock,hasLocked in the database
            ContentValues values = new ContentValues();
            values.put(AppLockMetadata.TABLE_COLUMN_PACKAGENAME, packageName);
            values.put(AppLockMetadata.TABLE_COLUMN_STATUS, AppLockMetadata.NEED_NOT_APPLOCK);
            values.put(AppLockMetadata.TABLE_COLUMN_LABELNAME, appName);
            values.put(AppLockMetadata.TABLE_COLUMN_ICON, AppLockUtils.changeDrawableToByte(icon));
            arrayValues[i] = values;
            //resolver.insert(FingerprintsContentProviderMetadata.CONTNET_URI, values);
        }
        getContentResolver().bulkInsert(AppLockMetadata.CONTNET_URI, arrayValues);

        Cursor cursor = getContentResolver().query(AppLockMetadata.CONTNET_URI,
                new String[]{AppLockMetadata.TABLE_COLUMN_PACKAGENAME,
                        AppLockMetadata.TABLE_COLUMN_STATUS,
                        AppLockMetadata.TABLE_COLUMN_LABELNAME},
                null, null, AppLockMetadata.TABLE_COLUMN_LABELNAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int packageNameIndex = cursor.getColumnIndex(AppLockMetadata.TABLE_COLUMN_PACKAGENAME);
                int isNeedLockIndex = cursor.getColumnIndex(AppLockMetadata.TABLE_COLUMN_STATUS);
                String packageName = cursor.getString(packageNameIndex);
            }


        }

        cursor.close();

    }

}
