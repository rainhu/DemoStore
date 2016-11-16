package rainhu.com.demonstore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

    }


    @Override
    protected void onResume() {
        super.onResume();

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


            if(params[0] == 0) { //FillStorage


                char[] arrayOfChar = new char[one_MB];
                FileWriter localFileWriter = null;
                String fileName = StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME;
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

            }else if (params[0] == 2){  //clear all





            }

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
