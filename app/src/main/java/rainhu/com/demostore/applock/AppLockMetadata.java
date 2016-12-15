package rainhu.com.demostore.applock;

import android.net.Uri;

/**
 * Created by hu on 16-12-14.
 */

public class AppLockMetadata {

    public static final String AUTHORITIES = "demostore";
    public static final Uri CONTNET_URI = Uri.parse("content://" + AUTHORITIES + "/applock");
    public static final String TABLE_NAME = "applock";
    public static final String TABLE_COLUMN_PACKAGENAME = "packageName";
    public static final String TABLE_COLUMN_STATUS = "lock_status";
    public static final String TABLE_COLUMN_LABELNAME = "labelName";
    public static final String TABLE_COLUMN_ICON = "icon";

    public static final int NEED_NOT_APPLOCK = 0;
    public static final int NEED_APPLOCK = 1;
    public static final int ALREADY_UNLOCKED = 2;

}
