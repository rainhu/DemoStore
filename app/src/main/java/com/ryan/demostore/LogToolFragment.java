package com.ryan.demostore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by huzhengyu on 16-12-29.
 */

public class LogToolFragment extends Fragment {
    boolean mIsLogging = false;

    @InjectView(R.id.startLogging)
    Button startLogBtn;

    @InjectView(R.id.stopLogging)
    Button stopLogBtn;

    @OnClick(R.id.startLogging)
    public void onStartLoggingClicked(){
        //catchingLog();
        install();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logtool, container , false);
        ButterKnife.inject(this , view);
        return view;
    }
    private void install() {
        //"/sdcard/aiqiyi_80740.apk"
        ///String path = "content://downloads/all_downloads/4";
//        PackageInfo packageinfo = getPackageManager().getPackageArchiveInfo(path , 0);
//
//        packageinfo.applicationInfo.sourceDir = path;
//        packageinfo.applicationInfo.publicSourceDir = path;
        String path = "content://downloads/my_downloads/9";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(path) ,"application/vnd.android.package-archive");
        getContext().startActivity(intent);
    }

    @OnClick(R.id.stopLogging)
    public void onStopLoggingClicked(){
        stopCatchingLop();
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
