package rainhu.com.demostore.applock;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by hu on 16-12-14.
 *
 */


public class AppInfo {
    private String appLabel;
    private Drawable appIcon;
    private Intent intent;
    private String packageName;


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

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
