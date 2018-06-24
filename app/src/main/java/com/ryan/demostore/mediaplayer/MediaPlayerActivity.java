package com.ryan.demostore.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import com.ryan.demostore.R;

/**
 * Created by Ryan on 2018/1/13.
 */

public class MediaPlayerActivity extends Activity{
    public static final String TAG = "MediaPlayer";

    private TextView statusTextView;
    private TextView pathTextView;
    private MediaPlayer mMediaplayer;
    private int playPosiotion;
    private String mFilePath = null;
    private Context mContext;

    public enum STATUS{
        PREPARE,
        PLAYING,
        PAUSE,
        STOP
    }

    private STATUS mStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mediaplayer);
        statusTextView = (TextView) findViewById(R.id.mediaplayer_status);
        pathTextView = (TextView) findViewById(R.id.mediaplayer_path);
        mMediaplayer = new MediaPlayer();

    }


    @Override
    protected void onDestroy() {
        if (mMediaplayer != null){
            mMediaplayer.release();
        }
        mMediaplayer = null;
        super.onDestroy();
    }

    public void onOpenBtnClicked(View v){
        openFileBySAF();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri treeUri = data.getData();
            Log.i(TAG, "Uri: " + treeUri.toString());
            pathTextView.setText(treeUri.toString());
            mFilePath = treeUri.toString();
        }

    }

    private void openFileBySAF() {

        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE); //获取选择目录
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE); //显示结果只有可以打开的文件
        intent.setType("*/*");
        //intent.setFlags(Intent.EXTRA_ALLOW_MULTIPLE);
        startActivityForResult(intent, 100);
    }


    public void mediaPlay(View v){
        switch (v.getId()){
            case R.id.mediaplayer_play:
                if(pathTextView.getText() == null || pathTextView.getText() == ""){
                    return;
                }

                String fileName = pathTextView.getText().toString();
                File audio = new File(fileName);
                if(audio.exists()){
                    play();
                    updateStatus(STATUS.PLAYING);
                } else {
                    Log.e(TAG,"file not exists");
                    mFilePath = null;
                    //Toast.makeText(mContext, "文件不存在！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.mediaplayer_pause:
                if (mMediaplayer.isPlaying()){
                    mMediaplayer.pause();
                    updateStatus(STATUS.PAUSE);
                }
                break;
            case R.id.mediaplayer_stop:
                mMediaplayer.stop();
                updateStatus(STATUS.STOP);
                break;
        }
    }




    private void play(){
        mMediaplayer.reset(); //各项参数恢复成初始状态
        try {
            mMediaplayer.setDataSource(mFilePath);
            mMediaplayer.prepare();//缓冲
            //mMediaplayer.setOnPreparedListener(new );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void updateStatus(STATUS newStatus){
        mStatus = newStatus;
        switch (newStatus){
            case PLAYING:
                statusTextView.setText("音乐播放中...");
                break;
            case PAUSE:
                statusTextView.setText("暂停播放...");
                break;
            case STOP:
                statusTextView.setText("停止播放...");
                break;
        }
    }

}
