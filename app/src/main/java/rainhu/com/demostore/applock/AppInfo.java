package rainhu.com.demostore.applock;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by hu on 16-12-14.
 */


public class AppInfo {
    private String packageName;
    private String appLabel;
    private int status;
    private Drawable appIcon;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "packageName : " + packageName + "\n appLabel : " + appLabel + "\nstatus : " + status;
    }
}
