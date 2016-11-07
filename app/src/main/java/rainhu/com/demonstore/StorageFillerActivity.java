package rainhu.com.demonstore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

    private EditText mEditText;

    public final static int one_MB = 1024*1024;
    public String FILENAME = "mess.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storagefiller);
        mContext = this;
        mFillBtn = (Button) findViewById(R.id.storageFiller_fill);
        mFillBtn.setOnClickListener(this);
        mClearBtn = (Button) findViewById(R.id.storageFiller_clear);
        mClearBtn.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.storageFiller_storageOpData);

        mProgressBar = (ProgressBar) findViewById(R.id.storageFiller_processBar);
        mShowProcess = (TextView) findViewById(R.id.storageFiller_showProcess);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.storageFiller_fill:
                new FillTask().execute();
                break;
            case R.id.storageFiller_clear:
                new ClearTask().execute();
                break;
            default:
                break;
        }
    }



    public long getEditTextData(){
        return Long.parseLong(mEditText.getText().toString());
    }

    private  class FillTask extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            mShowProcess.setText("Loading ...");
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            //String data = mEditText.get

            long expectSize = getEditTextData();

            char[] arrayOfChar = new char[one_MB];
            FileWriter localFileWriter = null;
            File file = new File(StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                localFileWriter = new FileWriter(StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME, true);
                for  (int i = 0; i< expectSize ; i++) {
                    localFileWriter.write(arrayOfChar);
                    publishProgress( (int) (i * 100 / expectSize) );
                }
                localFileWriter.flush();
                localFileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("zhengyu","values:"+values[0]);
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }


    private class ClearTask extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {


            long expectSize = getEditTextData();

            FileWriter localFileWriter = null;
            File file = new File(StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            long length = file.length() / 1024 / 1024;
             Log.i("zhengyu","file Length:"+length+" clearSize:"+expectSize);
            try {
            localFileWriter = new FileWriter(StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME, true);

            if(expectSize >= length){
                localFileWriter.write("");
                Toast.makeText(mContext,"All Clear !!! ",Toast.LENGTH_SHORT).show();
            }else {



            }
                localFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
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
