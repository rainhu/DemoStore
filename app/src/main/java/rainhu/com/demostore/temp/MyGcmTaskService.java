package rainhu.com.demostore.temp;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import rainhu.com.demostore.logger.Log;

/**
 * Created by huzhengyu on 17-3-7.
 */

public class MyGcmTaskService extends GcmTaskService {
    public static final String TAG = "MyGcmTaskService";

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.i("hzy","onRunTask");
        return 0;
    }
}
