package rainhu.com.demostore.temp;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by huzhengyu on 17-3-6.
 */

public class TestIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
