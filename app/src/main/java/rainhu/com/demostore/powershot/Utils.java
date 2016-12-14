package rainhu.com.demostore.powershot;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by hu on 16-12-1.
 */

public class Utils {

    private static WindowManager mWindowManager;
    private static ActivityManager mActivityManager;

    private static WindowManager getWindowManager(Context context){
        if(mWindowManager == null)
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager;
    }

    private static ActivityManager getActivityManager(Context context){
        if(mActivityManager == null)
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return mActivityManager;
    }

    //dm.widthPixels 屏幕高度
    //dm.heightPixels
    //dm.density 屏幕密度
    public static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    public static void shotscreen(Activity activity){
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap mp = view.getDrawingCache();

        saveScreenshot(mp);
        // Bitmap mp = Bitmap.createBitmap(view.getDrawingCache(),0,0,);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        // return mp;
    }
    public  static boolean saveScreenshot(Bitmap bitmap){
        if(bitmap != null){
            String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            String filePath = sdCardPath + File.separator + "Pictures"+File.separator + "screenshot.png";

            try {
                File file = new File(filePath);
                FileOutputStream fos =  new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return  false;
            } catch (IOException e) {
                e.printStackTrace();

            }
            return  true;
        }
        return false;

    }

    public Activity getTopActivity(Context context){
        PackageManager pm = context.getPackageManager();

        return null;
    }


    //获取状态栏高度
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // CLog.i("StatusBarHeight : " + statusBarHeight);
        return statusBarHeight;
    }


    public static String getUsedMemPercent(Context context){
//        String dir = "/proc/meminfo";
//        try{
//            FileReader fr = new FileReader(dir);
//            BufferedReader br = new BufferedReader(fr,2048);
//            String memoryLine = br.readLine();
//
//        }catch (Exception e){
//
//        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        long availMem =  mi.availMem;
        long totalMem = mi.totalMem;
        int percent = (int) ((totalMem-availMem) / (float)totalMem * 100) ;
        return percent+"%";
    }

    public static long getAvailMem(Context context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);

        return mi.availMem;
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
}