package rainhu.com.demostore.viewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import rainhu.com.demostore.R;

/**
 * Created by Ryan on 2017/9/17.
 */

public class ViewerActivity extends Activity {
    private Button circleBtn;
    private CustomizedView myView;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_main);
        init();
    }

    private void init() {
        mContext = this;
        circleBtn = (Button) findViewById(R.id.circleBtn);
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myView = (CustomizedView) findViewById(R.id.customView);
            }
        });
    }
}
