package rainhu.com.demostore.applock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Sampler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


/**
 * Created by huzhengyu on 16-12-15.
 */

public class AppLockUtils {

    public static int updateAppStatus(Context context, String packageName, int newStatus) {
        Log.i(AppLockMetadata.TAG,"updateAppStatus, pacakgeName : "+packageName+" newStatus : "+newStatus);

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(AppLockMetadata.TABLE_COLUMN_STATUS, newStatus);
        return resolver.update(AppLockMetadata.CONTNET_URI, values, AppLockMetadata.TABLE_COLUMN_PACKAGENAME + " =? " , new String[]{packageName});
    }

    public static int getAppStatus(Context context, String packageName) {
        if(packageName == null){
            return  -1;
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(AppLockMetadata.CONTNET_URI, new String[]{AppLockMetadata.TABLE_COLUMN_STATUS},
                AppLockMetadata.TABLE_COLUMN_PACKAGENAME + " = ?" , new String[]{packageName}, null);
        if (c == null || c.getCount() <= 0 ) {
            return -1;
        }
        int stat = -1;

        if(c.moveToNext()) {
            int statusIndex = c.getColumnIndex(AppLockMetadata.TABLE_COLUMN_STATUS);
            stat = c.getInt(statusIndex);
        }
        c.close();
        return stat;
    }

    public static int recoverAllAppsFromUnlockedToNeedLockStatus(Context context){
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(AppLockMetadata.TABLE_COLUMN_STATUS, AppLockMetadata.NEED_APPLOCK);
        return resolver.update(AppLockMetadata.CONTNET_URI, values, AppLockMetadata.TABLE_COLUMN_STATUS +" =? ",new String[]{"3"});

    }

    public static byte[] changeDrawableToByte(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Drawable changeByteToDrawable(Context context, byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        Drawable drawable = bitmapDrawable;
        return drawable;
    }

    public static Activity getTopResumedActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if(activities == null) {
                Log.i("zhengyu","empty");
                return null;
            }
            Log.i("zhengyu","activity size : "+activities.size());
            for (Object activityRecord : activities.values()) {
                Log.i("zhengyu","activityRecordclient : "+activityRecord.toString());
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                // if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
                //}
            }
            return null;
        } catch (Exception e) {

            Log.i("zhengyu",e.toString());
            return null;
        }
    }

    public static String getTopRunningApp(Context context) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTasks = am.getRunningTasks(1);
            if( null != appTasks && !appTasks.isEmpty()){
                return appTasks.get(0).topActivity.getPackageName();
            }
        }


        UsageStatsManager usageStatsManager = (UsageStatsManager)
                context.getSystemService(Context.USAGE_STATS_SERVICE);

        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,ts-2000, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null ||
                    recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();
    }

    public static String getLauncherPackageName(Context context)
    {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if(res.activityInfo == null)
        {
            return "";
        }
        //如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
        if(res.activityInfo.packageName.equals("android"))
        {
            return "";
        }else
        {
            return res.activityInfo.packageName;
        }
    }
}
