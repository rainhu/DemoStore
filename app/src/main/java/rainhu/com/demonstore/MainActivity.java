package rainhu.com.demonstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rainhu.com.demonstore.activity.AnimationActivity;
import rainhu.com.demonstore.activity.MediaDemoActivity;
import rainhu.com.demonstore.activity.StorageFillerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button storageFillerBtn;
    private Button mediaDemoBtn;
    private Button animationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageFillerBtn = (Button) findViewById(R.id.storageFillerBtn);
        storageFillerBtn.setOnClickListener(this);

        mediaDemoBtn = (Button) findViewById(R.id.mediaDemoBtn);
        mediaDemoBtn.setOnClickListener(this);

        animationBtn = (Button) findViewById(R.id.animationBtn);
        animationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.storageFillerBtn:
                startActivity(new Intent(this, StorageFillerActivity.class));
                break;
            case R.id.mediaDemoBtn:
                startActivity(new Intent(this,MediaDemoActivity.class));
                break;
            case R.id.animationBtn:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            default:
                break;
        }
    }
}
