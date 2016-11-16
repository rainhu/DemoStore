package rainhu.com.demonstore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    private EditText mEditText;

    public final static int one_MB = 1024*1024;
    public String FILENAME = "mess.txt";

    public  int FILL_STORAGE = 0;
    public  int CLEAR_STORAGE = 1;
    public int CLEAR_ALL = 2;

    private TextView mTotalStorage;
    private TextView mFreeStorage;

    private StatFs mDataFileStats;

    private SharedPreferences mSharedPreferences;

    private Map<String, Long> mFileMap;

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

        mEditText = (EditText) findViewById(R.id.storageFiller_storageOpData);

        mProgressBar = (ProgressBar) findViewById(R.id.storageFiller_processBar);
        mShowProcess = (TextView) findViewById(R.id.storageFiller_showProcess);

        mTotalStorage = (TextView) findViewById(R.id.totalStorage);
        mFreeStorage = (TextView) findViewById(R.id.freeStorage);

        mDataFileStats = new StatFs("/data");
        mTotalStorage.setText("Total : "+ mDataFileStats.getTotalBytes()/1024/1024 + " MB");
        //mFreeStorage.setText("Free : " + mDataFileStats.getAvailableBlocksLong()/1024/1024 + " MB");

        mSharedPreferences =  mContext.getSharedPreferences("sp",MODE_PRIVATE);


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
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.storageFiller_fill:
                new FillTask().execute(FILL_STORAGE);
                break;
            case R.id.storageFiller_clear:
                new FillTask().execute(CLEAR_STORAGE);
                break;
            case R.id.storageFiller_clearAll:
                new FillTask().execute(CLEAR_ALL);
            default:
                break;
        }
    }



    public long getEditTextData(){
        String number = mEditText.getText().toString();
        if( number.trim().equals("")){
            //Toast.makeText(mContext,"Please enter a valid number!",Toast.LENGTH_SHORT).show();
            return 0L;
        }

        return Long.parseLong(number);
    }

    private  class FillTask extends AsyncTask<Integer,Integer,Boolean>{


        @Override
        protected void onPreExecute() {
            mShowProcess.setText("Loading ...");
        }


        @Override
        protected Boolean doInBackground(Integer... params) {
            //String data = mEditText.get
            long expectSize = getEditTextData();

             int count = mFileMap.size();

            if(params[0] == 0) { //FillStorage


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
                        publishProgress((int) (i * 100 / expectSize));
                    }
                    localFileWriter.flush();
                    localFileWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putLong(fileName,expectSize);
                editor.commit();

            }else if(params[0] == 1){
                Set set = mFileMap.entrySet();
                Iterator it = set.iterator();

                File targetFile = null;
                Long smallestFitableFileSize = Long.MAX_VALUE;

                //选出最合适替换的文件
                while (it.hasNext()) {
                    Map.Entry tmpentry = (Map.Entry) it.next();
                    File tmpFile = new File((String) tmpentry.getKey());
                    Long tmpFileSize = (Long) tmpentry.getValue();
                    if(tmpFileSize > expectSize && tmpFileSize < smallestFitableFileSize ){
                        targetFile = tmpFile;
                        smallestFitableFileSize = tmpFileSize;
                    }

                }

                if(targetFile == null){
                    //该软件没有生成过任何比目标清理更大的文件，不执行清理
                    Log.i("zhengyu","not clear");
                    return null;
                }
                try {
                    FileWriter fileWiter = new FileWriter(targetFile);
                    //覆盖填充差值
                    Log.i("zhengyu","tartFile : "+targetFile.getName()+" smallestFitableFileSize : "+smallestFitableFileSize+" expectSize : "+expectSize);
                    char[] arrayOfChar = new char[one_MB];
                    for (int i = 0; i < smallestFitableFileSize - expectSize; i++) {
                        fileWiter.write(arrayOfChar);
                        publishProgress((int) (i * 100 / (smallestFitableFileSize - expectSize)));
                    }
                    fileWiter.flush();
                    fileWiter.close();
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putLong(targetFile.getAbsolutePath(),(smallestFitableFileSize - expectSize));
                    editor.commit();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (params[0] == 2) {  //clear all
                Set set = mFileMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry tmpentry = (Map.Entry) it.next();
                    File file = new File((String) tmpentry.getKey());
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();

            }
            mFileMap = (Map<String, Long>) mSharedPreferences.getAll();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //Log.i("zhengyu","values:"+values[0]);
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            restateDataDir();

        }
    }



    /*
    private void initProcessDialog(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("Filling ...");
        mProgressDialog.setProgressStyle(0);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //TODO
                //update data view
            }
        });

        mProgressDialog.show();
    }
    */
}
