package rainhu.com.demostore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rainhu.com.demostore.R;

import android.util.Log;



/**
 * Created by huzhengyu on 16-12-26.
 */

public class LogToolActivity extends Activity{
    boolean mIsLogging = false;

    @InjectView(R.id.startLogging)
    Button startLogBtn;

    @InjectView(R.id.stopLogging)
    Button stopLogBtn;

    @OnClick(R.id.startLogging)
    public void onStartLoggingClicked(){
        catchingLog();
    }

    @OnClick(R.id.stopLogging)
    public void onStopLoggingClicked(){
        stopCatchingLop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logtool);
        ButterKnife.inject(this);

    }


    private void catchingLog(){
        Log.i("hzy","catchingLog");

        mIsLogging = true;

        ArrayList commandLine = new ArrayList();
        commandLine.add("logcat");
        commandLine.add("-b");
        commandLine.add("all");

        commandLine.add("-f");
        commandLine.add("/sdcard/log/logcat");

        try {
            Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()),1024);
            String line = bufferedReader.readLine();

            StringBuilder builder = new StringBuilder();

            while (line != null && mIsLogging){
               // log.app
                builder.append(line);
                Log.i("hzy", line);
            }


            long currentTime = System.currentTimeMillis();

            File file = new File("/sdcard/" + currentTime + ".txt");

            Log.i("hzy",file.getAbsolutePath());
            file.createNewFile();

            FileWriter fileWrite = new FileWriter(file);
            fileWrite.write(builder.toString());
            fileWrite.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void stopCatchingLop() {
        Log.i("hzy","stopCatchingLop");

        mIsLogging = false;

    }
}
