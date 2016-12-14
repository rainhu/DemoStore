package rainhu.com.demostore.applock;

import android.net.Uri;

/**
 * Created by hu on 16-12-14.
 */

public class AppLockMetadata {

    public static final String AUTHORITIES = "demostore";
    public static final Uri CONTNET_URI = Uri.parse("content://"+AUTHORITIES+"/applock");
    public static final String TABLE_NAME = "applock";
    public static final String TABLE_COLUMN_PACKAGENAME = "packageName";
    public static final String TABLE_COLUMN_ISNEEDLOCK = "isNeedLock";
    public static final String TABLE_COLUMN_HASLOCKED = "hasLocked";
    public static final String TABLE_COLUMN_LABELNAME="labelName";
}
