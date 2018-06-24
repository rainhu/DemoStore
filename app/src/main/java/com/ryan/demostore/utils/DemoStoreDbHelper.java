package com.ryan.demostore.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ryan.demostore.applock.AppLockMetadata;

/**
 * Created by hu on 16-12-14.
 */

public class DemoStoreDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "DemoStoreDbHelper";

    private static DemoStoreDbHelper mDemoDb = null;
    Context mContext;

    public DemoStoreDbHelper(Context context, String name) {
        super(context, name, null, getDatabaseVersion(context));
        mContext = context;
    }

    public static DemoStoreDbHelper getInstance(Context context, String name) {
        Log.i("mydb", "getInstance");
        if (mDemoDb == null) {
            mDemoDb = new DemoStoreDbHelper(context, name);
        }
        return mDemoDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(mContext, db, 0, getDatabaseVersion(mContext));

    }

    private void updateDatabase(Context mContext, SQLiteDatabase db, int fromVersion, int toVersion) {

        int dversion = getDatabaseVersion(mContext);
        if (dversion != toVersion) {
            Log.e(TAG, "Illegal update request . Got " + toVersion + ", expected " + dversion);
            throw new IllegalArgumentException();
        } else if (fromVersion > toVersion) {
            Log.e(TAG, "Illegal update request: can't downgrade from " + fromVersion + " to " + toVersion + " . Did you forget to wipe data?");
            throw new IllegalArgumentException();
        }

        //create table applock
        db.execSQL("create table if not exists " + AppLockMetadata.TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY,"
                + AppLockMetadata.TABLE_COLUMN_PACKAGENAME + " VARCHAR,"
                + AppLockMetadata.TABLE_COLUMN_LABELNAME + " VARCHAR COLLATE NOCASE,"
                + AppLockMetadata.TABLE_COLUMN_STATUS + " VARCHAR,"
                + AppLockMetadata.TABLE_COLUMN_ICON + " BLOB"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(mContext, db, oldVersion, newVersion);
    }

    public static int getDatabaseVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("couldn't get version code for " + context);
        }
    }

}
