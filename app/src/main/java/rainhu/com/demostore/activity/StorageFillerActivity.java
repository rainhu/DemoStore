package rainhu.com.demostore.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StatFs;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import rainhu.com.demostore.R;

/**
 * Created by hu on 16-11-6.
 */

public class StorageFillerActivity extends Activity implements View.OnClickListener{

    private Context mContext;
    private ProgressDialog mProgressDialog = null;
    private ProgressBar mProgressBar;
    private TextView mShowProcess;

    private Button mFillBtn;
    private Button mClearBtn;
    private Button mClearAllBtn;
    private Button mCancleBtn;

    private EditText mEditText;

    public final static int one_MB = 1024*1024;
    public String FILENAME = "mess.txt";

    public int FILL_STORAGE = 0;
    public int CLEAR_STORAGE = 1;
    public int CLEAR_ALL = 2;

    private TextView mTotalStorage;
    private TextView mFreeStorage;
    private TextView mFilesTextView;

    private StatFs mDataFileStats;

    private SharedPreferences mSharedPreferences;

    private Map<String, Long> mFileMap;

    private FillTask mTask;
    private Boolean isTaskActive = false;

    public static final int ERROR_NO_SPACE = -1;
    public static final int ERROR_NO_SUITABLE_FILE = -2;
    public static final int ERROR_INVALID_INPUT = -3;

    public static final int SUCCESS_FILL_STORAGE = 0;
    public static final int SUCCESS_CLEAR_STORAGE = 1;
    public static final int SUCCESS_CLEAR_ALL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storagefiller);
        mContext = this;
        mFillBtn = (Button) findViewById(R.id.storageFiller_fill);
        mFillBtn.setOnClickListener(this);
        mClearBtn = (Button) findViewById(R.id.storageFiller_clear);
        mClearBtn.setOnClickListener(this);
        mClearAllBtn = (Button) findViewById(R.id.storageFiller_clearAll);
        mClearAllBtn.setOnClickListener(this);
        mCancleBtn = (Button) findViewById(R.id.storageFiller_cancel);
        mCancleBtn.setEnabled(false);
        mCancleBtn.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.storageFiller_storageOpData);

        mProgressBar = (ProgressBar) findViewById(R.id.storageFiller_processBar);
        mShowProcess = (TextView) findViewById(R.id.storageFiller_showProcess);

        mTotalStorage = (TextView) findViewById(R.id.totalStorage);
        mFreeStorage = (TextView) findViewById(R.id.freeStorage);

        mDataFileStats = new StatFs("/data");
        mTotalStorage.setText("Total : "+ mDataFileStats.getTotalBytes()/1024/1024 + " MB");
        //mFreeStorage.setText("Free : " + mDataFileStats.getAvailableBlocksLong()/1024/1024 + " MB");

        mSharedPreferences =  mContext.getSharedPreferences("sp",MODE_PRIVATE);


        mFilesTextView = (TextView) findViewById(R.id.files);

        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setExitTransition(fade);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mFileMap = (Map<String, Long>) mSharedPreferences.getAll();

        restateDataDir();
    }

    private void restateDataDir() {
        mDataFileStats.restat("/data");
        mFreeStorage.setText("Free : " + mDataFileStats.getAvailableBytes()/1024/1024 + " MB");

        String createdFilesText = mFileMap.isEmpty() ?  "" : "CreatedFiles : \n";

        Set set = mFileMap.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext() ) {
            Map.Entry tmpentry = (Map.Entry) it.next();
            File tmpFile = new File((String) tmpentry.getKey());
            Long tmpFileSize = (Long) tmpentry.getValue();

            createdFilesText += tmpFile.getName() + " : "+ tmpFileSize.toString() + "MB";
            createdFilesText += "\n";
        }
        mFilesTextView.setText(createdFilesText);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.storageFiller_fill:
                if(!isTaskActive){
                    mTask = new FillTask();
                    mTask.execute(FILL_STORAGE);
                }else{
                    Toast.makeText(StorageFillerActivity.this,"please wait for another task finished !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.storageFiller_clear:
                if(!isTaskActive) {
                    mTask = new FillTask();
                    mTask.execute(CLEAR_STORAGE);
                }else{
                    Toast.makeText(StorageFillerActivity.this,"please wait for another task finished !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.storageFiller_clearAll:
                if(!isTaskActive) {
                    mTask = new FillTask();
                    mTask.execute(CLEAR_ALL);
                }else{
                    Toast.makeText(StorageFillerActivity.this,"please wait for another task finished !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.storageFiller_cancel:
                if(isTaskActive){
                    onTaskCancled();
                }else{
                    Toast.makeText(StorageFillerActivity.this,"no Task is running now !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void onTaskCancled() {
        mTask.cancel(true);
        Toast.makeText(StorageFillerActivity.this,"cancle task succeed !!!", Toast.LENGTH_SHORT).show();
        mCancleBtn.setEnabled(false);
        mProgressBar.setProgress(0);
        isTaskActive = false;
        mEditText.setText("");
        restateDataDir();

    }


    public long getEditTextData(){
        String number = mEditText.getText().toString();
        if( number.trim().equals("")){
            //Toast.makeText(mContext,"Please enter a valid number!",Toast.LENGTH_SHORT).show();
            return 0L;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!pattern.matcher(number).matches()){
            return -1;
        }
        return Long.parseLong(number);
    }

    //AsyncTask第一个参数确定tast.execute的传入参数， 对应到doInBackground的接收参数
    //第二个参数显示进度的参数
    //第三个为doInbackground的返回和onPostExecute的传入参数
    private  class FillTask extends AsyncTask<Integer,Integer, Integer>{

        @Override
        protected void onPreExecute() {
            Log.i("zhengyu","onPreExecute");
            mCancleBtn.setEnabled(true);
            mShowProcess.setText("Loading ...");
            mProgressBar.setProgress(0);
            isTaskActive = true;
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            Log.i("zhengyu","doInBackground");
            //String data = mEditText.get
            final long expectSize = getEditTextData();
            if(expectSize < 0 && params[0] != 2 ){
                return ERROR_INVALID_INPUT;
            }

            int msg = 0;
            int count = mFileMap.size();
            if(params[0] == 0) { //FillStorage
                if(expectSize > mDataFileStats.getAvailableBytes()/1024/1024){
                    //要填充的存储大于剩余可用存储
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StorageFillerActivity.this,"no space left to fill storage "+expectSize+" !!!", Toast.LENGTH_SHORT).show();
                            mEditText.setText("");
                        }
                    });
                    return ERROR_NO_SPACE;
                }

                char[] arrayOfChar = new char[one_MB];
                FileWriter localFileWriter = null;
                String fileName = StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME+ (++count);
                Log.i("zhengyu","filename:"+fileName);
                ///data/user/0/rainhu.com.demonstore/files/mess.txt
                File file = new File(fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    localFileWriter = new FileWriter(fileName, true);
                    for (int i = 0; i < expectSize; i++) {
                        localFileWriter.write(arrayOfChar);
                        publishProgress((int) (i * 100 / expectSize),0);
                    }
                    localFileWriter.flush();
                    localFileWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putLong(fileName,expectSize);
                editor.commit();

                msg =  SUCCESS_FILL_STORAGE;
            }else if(params[0] == 1){ //clear

                Set set = mFileMap.entrySet();
                Iterator it = set.iterator();

                File targetFile = null;
                Long smallestFitableFileSize = Long.MAX_VALUE;

                Long genelatedSize = 0l;
                //选出最合适替换的文件
                while (it.hasNext()) {
                    Map.Entry tmpentry = (Map.Entry) it.next();
                    File tmpFile = new File((String) tmpentry.getKey());
                    Long tmpFileSize = (Long) tmpentry.getValue();

                    genelatedSize += tmpFileSize;
                    if(tmpFileSize >= expectSize && tmpFileSize < smallestFitableFileSize ){
                        targetFile = tmpFile;
                        smallestFitableFileSize = tmpFileSize;
                    }

                }

                if(targetFile == null){
                    //该软件没有生成过任何比目标清理更大的文件，不执行清理
                    if(expectSize > genelatedSize) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StorageFillerActivity.this, "no larger file generated by this app!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return ERROR_NO_SUITABLE_FILE;
                    }




                }
                try {
                    FileWriter fileWiter = new FileWriter(targetFile);
                    //覆盖填充差值
                    Log.i("zhengyu","tartFile : "+targetFile.getName()+" smallestFitableFileSize : "+smallestFitableFileSize+" expectSize : "+expectSize);
                    char[] arrayOfChar = new char[one_MB];
                    for (int i = 0; i < smallestFitableFileSize - expectSize; i++) {
                        fileWiter.write(arrayOfChar);
                        publishProgress((int) (i * 100 / (smallestFitableFileSize - expectSize)),1);
                    }
                    fileWiter.flush();
                    fileWiter.close();
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putLong(targetFile.getAbsolutePath(),(smallestFitableFileSize - expectSize));
                    editor.commit();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg =  SUCCESS_CLEAR_STORAGE;
            }else if (params[0] == 2) {  //clear all
                int size  = mFileMap.size();

                Set set = mFileMap.entrySet();
                Iterator it = set.iterator();
                int j = 1;
                while (it.hasNext()) {
                    Map.Entry tmpentry = (Map.Entry) it.next();
                    File file = new File((String) tmpentry.getKey());
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                        publishProgress( j * 100 / size , 2);
                        j++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();
                msg = SUCCESS_CLEAR_ALL;

            }
            mFileMap = (Map<String, Long>) mSharedPreferences.getAll();
            return msg;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //Log.i("zhengyu","values:"+values[0]);
            if(values[1] == 0 || values[1] == 2 ) { //from fill
                mProgressBar.setProgress(values[0]);
                restateDataDir();
            }else if(values[1] == 1){ //from clear
                mProgressBar.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Integer msg) {
            Log.i("zhengyu","onPostExecute");
            switch (msg){
                case SUCCESS_FILL_STORAGE:
                    Toast.makeText(StorageFillerActivity.this,"success in fill storage !!!", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS_CLEAR_STORAGE:
                    Toast.makeText(StorageFillerActivity.this,"success in clear storage !!!", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS_CLEAR_ALL:
                    Toast.makeText(StorageFillerActivity.this,"success in clear all !!!", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_NO_SPACE:
                case ERROR_NO_SUITABLE_FILE:
                case ERROR_INVALID_INPUT :
                    Toast.makeText(StorageFillerActivity.this,"please in put valid number !!!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            restateDataDir();
            mCancleBtn.setEnabled(false);
            mEditText.setText("");
            isTaskActive = false;
            mProgressBar.setProgress(0);
        }
    }
}
