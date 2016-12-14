package rainhu.com.demostore.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import rainhu.com.demostore.applock.AppLockMetadata;
import rainhu.com.demostore.logger.Log;

/**
 * Created by hu on 16-12-14.
 */

public class DemoStoreProvider extends ContentProvider {

    public static final String TAG = "demostoreprovider";
    private DemoStoreDbHelper mDemoStoreDbHelper = null;
    public static final String DATABASE_NAME = "demostore.db";

    private static final UriMatcher URI_MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);


    private static final int TABLE_APPLOCK = 1;

    static {
        URI_MATCHER.addURI("demostore", "*/applock", TABLE_APPLOCK);
    }


    @Override
    public boolean onCreate() {
        mDemoStoreDbHelper = DemoStoreDbHelper.getInstance(getContext(), DATABASE_NAME);
        return mDemoStoreDbHelper == null ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.e("zhengyu","uri :"+uri.toString());
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mDemoStoreDbHelper.getReadableDatabase();
        qb.setTables(getTableName(uri));
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDemoStoreDbHelper.getWritableDatabase();
        long rowId = db.insert(getTableName(uri),null,values);
        if(rowId > 0) {
            Uri insertedtUri = ContentUris.withAppendedId(AppLockMetadata.CONTNET_URI, rowId);
            getContext().getContentResolver().notifyChange(insertedtUri,null);
            return insertedtUri;
        }
        throw new SQLException("Failed to insert row into "+AppLockMetadata.CONTNET_URI);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDemoStoreDbHelper.getWritableDatabase();
        int count = db.delete(getTableName(uri),selection,selectionArgs);
        if(count > 0){
            getContext().getContentResolver().notifyChange(uri,null);
            return count;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDemoStoreDbHelper.getWritableDatabase();
        int count = db.update(getTableName(uri),values,selection,selectionArgs);
        if(count > 0){
            Uri insertedtUri = ContentUris.withAppendedId(AppLockMetadata.CONTNET_URI, count);
            getContext().getContentResolver().notifyChange(insertedtUri,null);
            return 1;
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) throws SQLException{
        int numValues = 0;
        SQLiteDatabase db = mDemoStoreDbHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                insert(uri,values[i]);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        return numValues;
    }

    private String getTableName(Uri uri){
        int index = URI_MATCHER.match(uri);
        String table = null;

        switch (index) {
            case TABLE_APPLOCK:
                table = AppLockMetadata.TABLE_NAME;
                break;
            default:
                break;
        }
        if (table == null){
            Log.e(TAG, "check uri is right");
            throw new IllegalArgumentException();
        }
        return table;
    }
}
