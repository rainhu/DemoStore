package rainhu.com.demonstore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
                break;
            default:
                break;
        }
    }

    private  class FillTask  extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            mShowProcess.setText("Loading ...");

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            //String data = mEditText.get
            char[] arrayOfChar = new char[one_MB];
            FileWriter localFileWriter = null;
            try {
                localFileWriter = new FileWriter(StorageFillerActivity.this.getFilesDir() + File.separator + StorageFillerActivity.this.FILENAME, true);

            localFileWriter.write(arrayOfChar);
            localFileWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
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
