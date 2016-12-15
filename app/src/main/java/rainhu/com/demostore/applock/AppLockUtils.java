package rainhu.com.demostore.applock;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;

import java.io.ByteArrayOutputStream;

import rainhu.com.demostore.logger.Log;

/**
 * Created by huzhengyu on 16-12-15.
 */

public class AppLockUtils {
    public static int updateAppStatus(Context context, String packageName, int newStatus) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(AppLockMetadata.TABLE_COLUMN_STATUS, newStatus);
        return resolver.update(AppLockMetadata.CONTNET_URI, values, AppLockMetadata.TABLE_COLUMN_PACKAGENAME + " = " + packageName, null);
    }

    public static int getAppStatus(Context context, String packageName) {
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(AppLockMetadata.CONTNET_URI, new String[]{AppLockMetadata.TABLE_COLUMN_STATUS},
                AppLockMetadata.TABLE_COLUMN_PACKAGENAME + " = " + packageName, null, null);
        if (c == null || c.getCount() <= 0) {
            throw new IllegalArgumentException();
        }
        c.moveToNext();
        int statusIndex = c.getColumnIndex(AppLockMetadata.TABLE_COLUMN_STATUS);
        return c.getInt(statusIndex);
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
}
