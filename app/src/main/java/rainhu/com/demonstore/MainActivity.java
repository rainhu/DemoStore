package rainhu.com.demonstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button storageFillerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageFillerBtn = (Button) findViewById(R.id.storageFillerBtn);
        storageFillerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.storageFillerBtn:
                startActivity(new Intent(this, StorageFillerActivity.class));
                break;
            default:
                break;

        }
    }
}
