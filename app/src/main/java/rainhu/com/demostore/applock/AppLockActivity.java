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
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rainhu.com.demostore.logger.Log;

/**
 * Created by hu on 16-12-14.
 */

public class AppLockActivity extends Activity {
    private List<AppInfo> appInfoList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }


    @Override
    protected void onResume() {
        super.onResume();

        //insertAppInfoToDb();
        getAllApps(mContext);
        for(AppInfo app : appInfoList){
            Log.i("zhengyu",app.toString());
        }
    }

    private void getAllApps(Context context) {
        appInfoList = new ArrayList<AppInfo>();

        PackageManager mManager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = mManager.queryIntentActivities(mainIntent,0);
        //根据名字排序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mManager));
        if (appInfoList != null) {
            appInfoList.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name;
                String packageName = reInfo.activityInfo.packageName;
                String appLabel = (String) reInfo.loadLabel(mManager);
                Drawable icon = reInfo.loadIcon(mManager);

                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(packageName, activityName));

                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                appInfo.setPackageName(packageName);
                appInfoList.add(appInfo);
            }
        }
    }

    private void insertAppInfoToDb(){
        getAllApps(mContext);
        Cursor cursor = getContentResolver().query(AppLockMetadata.CONTNET_URI,
                new String[]{ AppLockMetadata.TABLE_COLUMN_PACKAGENAME,
                        AppLockMetadata.TABLE_COLUMN_ISNEEDLOCK,
                        AppLockMetadata.TABLE_COLUMN_LABELNAME },
                null, null,AppLockMetadata.TABLE_COLUMN_LABELNAME+" ASC");
        //int packageNameIndex = cursor.getColumnIndex(AppLockMetadata.TABLE_COLUMN_PACKAGENAME);
        //int isNeedLockIndex = cursor.getColumnIndex(AppLockMetadata.TABLE_COLUMN_ISNEEDLOCK);

        ContentValues[] arrayValues = new ContentValues[appInfoList.size()];

        for (int i = 0; i < appInfoList.size(); i++) {

            String packageName = appInfoList.get(i).getPackageName();
            String appName = appInfoList.get(i).getAppLabel();
            Drawable icon = appInfoList.get(i).getAppIcon();

            //add the packageName,isNeedLock,hasLocked in the database
            ContentValues values = new ContentValues();
            values.put(AppLockMetadata.TABLE_COLUMN_PACKAGENAME, packageName);
            values.put(AppLockMetadata.TABLE_COLUMN_ISNEEDLOCK, "0");
            values.put(AppLockMetadata.TABLE_COLUMN_HASLOCKED, "0");
            values.put(AppLockMetadata.TABLE_COLUMN_LABELNAME,appName);
            arrayValues[i] = values;
            //resolver.insert(FingerprintsContentProviderMetadata.CONTNET_URI, values);
        }
        cursor.close();
        getContentResolver().bulkInsert(AppLockMetadata.CONTNET_URI, arrayValues);
    }


}
