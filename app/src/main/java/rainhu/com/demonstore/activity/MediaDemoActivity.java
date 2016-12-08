package rainhu.com.demonstore.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.File;

import rainhu.com.demonstore.R;

/**
 * Created by huzhengyu on 16-11-17.
 */

public class MediaDemoActivity extends Activity implements View.OnClickListener{
    private Button mShareBtn;
    private Button mQueryBtn;
    private EditText mDeleteNumber;
    private Button mDeleteBtn;

    private Context mContext;

    private Button mSaveMediaProviderDBBtn;
    private  ContentProviderClient mMediaProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediademo);
        mContext = this;

        mShareBtn = (Button) findViewById(R.id.mediademo_shareBtn);
        mShareBtn.setOnClickListener(this);

        mQueryBtn = (Button) findViewById(R.id.mediademo_queryBtn);
        mQueryBtn.setOnClickListener(this);

        mDeleteNumber = (EditText) findViewById(R.id.mediademo_deleteNumber);
        mDeleteBtn = (Button) findViewById(R.id.mediademo_deleteBtn);
        mDeleteBtn.setOnClickListener(this);

        mSaveMediaProviderDBBtn = (Button) findViewById(R.id.mediademo_saveMediaProviderDB);
        mSaveMediaProviderDBBtn.setOnClickListener(this);

        if(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MediaDemoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            Log.i("zhengyu", "have permission");
        }else{
            Log.i("zhengyu", "do not have permission");
            //ActivityCompat.shouldShowRequestPermissionRationale(MediaDemoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        }




    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("zhengyu","onRequestPermissionsResult"+grantResults[0]);
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted 用户允许权限 继续执行（

                }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mediademo_shareBtn:
                onshareBtnClicked();
                break;
            case R.id.mediademo_queryBtn:
                onQueryBtnClicked();
                break;
            case R.id.mediademo_deleteBtn:
                onDeleteBtnClicked();
                break;
            case R.id.mediademo_saveMediaProviderDB:
                onSaveMediaProviderDBClicked();
                break;
            default:
                break;
        }
    }


    class MyDbHelper extends SQLiteOpenHelper{

        public static final String CREATE_TABLE_FILES = "CREATE TABLE IF NOT EXISTS files(" +
                "_id INTEGER PRIMARY KEY," +
                "_data TEXT NOT NULL," +
                "format INTEGER," +
                "date_modified INTEGER" +
                ")";

        public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_FILES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    private class CrawlMediaDbTask extends AsyncTask<Void,Integer,Void>{
        Cursor c = null;
        String where = null;
        String[] selectionArgs = null;

        //content://media/external/file/
        Uri filesUri = MediaStore.Files.getContentUri("external");
        long lastId = Long.MIN_VALUE;
        Uri limitUri = filesUri.buildUpon().appendQueryParameter("limit", "1000").build();

        final String EXTERNAL_DATABASE_NAME = "external.db";

        private final String[] FILES_PROJECTION = new String[] {
                MediaStore.Files.FileColumns._ID, // 0
                MediaStore.Files.FileColumns.DATA, // 1
                "format",
                MediaStore.Files.FileColumns.DATE_MODIFIED, // 2
        };

        @Override
        protected void onPreExecute() {
            mMediaProvider = mContext.getContentResolver()
                    .acquireContentProviderClient(MediaStore.AUTHORITY);

            where = MediaStore.Files.FileColumns._ID + ">?";
            selectionArgs = new String[] { "" };




        }

        @Override
        protected Void doInBackground(Void... params) {

            File dbFile = mContext.getDatabasePath(EXTERNAL_DATABASE_NAME);
            ///data/user/0/rainhu.com.demonstore/databases/external.db
            Log.i("zhengyu","db path :"+ dbFile.getPath());
            if(dbFile.exists()){
                dbFile.delete();
            }

            //Log.i("zhengyu", "dbPath :"+dbFile.getAbsolutePath());
            MyDbHelper dbHelper = new MyDbHelper(mContext, EXTERNAL_DATABASE_NAME, null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            while (true) {
                publishProgress(1);

                selectionArgs[0] = "" + lastId;
                if (c != null) {
                    c.close();
                    c = null;
                }

                try {
                    c = mMediaProvider.query(limitUri, FILES_PROJECTION,
                            where, selectionArgs, MediaStore.Files.FileColumns._ID, null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (c == null) {
                    break;
                }
                int num = c.getCount();
                if (num == 0) {
                    break;
                }
                while (c.moveToNext()) {
                    long rowId = c.getLong(0);
                    String path = c.getString(1);
                    int format = c.getInt(2);
                    long lastModified = c.getLong(3);
                    lastId = rowId;

                    ContentValues values = new ContentValues();
                    values.put("_id" , rowId);
                    values.put("_data" , path);
                    values.put("format" , format);
                    values.put("date_modified" , lastModified);
                    db.insert("files", null ,values);
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("zhengyu","still insert ...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(mContext , "crawl MediaProvider DB Success !" , Toast.LENGTH_SHORT).show();
        }
    }

    private void onSaveMediaProviderDBClicked() {

        new CrawlMediaDbTask().execute();

    }

    private void onDeleteBtnClicked() {

        String number = mDeleteNumber.getText().toString();

        ContentResolver resolver = getApplicationContext().getContentResolver();
        Uri uri=MediaStore.Files.getContentUri("external");
        Log.i("zhengyu","uri---->"+uri.toString());

        String projection[]={MediaStore.Files.FileColumns.DATA};
        String where=MediaStore.Files.FileColumns._ID+"=?";
        //String selectArgs[]={"8"};
        String selectArgs[] = {number};
        Cursor c = null;
        c = resolver.query(uri,projection,where,selectArgs,MediaStore.Files.FileColumns._ID);

                if(c!=null){
                    c.moveToNext();
                    int index=c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    String data=c.getString(index);
                    Log.i("zhengyu","data:"+data);
                }

        //  ArrayMap<String,String> d = new ArrayMap<String, String>();

        int i=resolver.delete(MediaStore.Files.getContentUri("external"),MediaStore.Files.FileColumns._ID+"=?", selectArgs);
        //Log.i("zhengyu","return:"+i
    }

    private void onQueryBtnClicked() {
        ContentResolver resolver=mContext.getContentResolver();
        Uri uri= MediaStore.Files.getContentUri("external");
        Log.i("zhengyu","uri---->"+uri.toString());

        String projection[]={MediaStore.Files.FileColumns.DATA};
        String where=MediaStore.Files.FileColumns._ID+"=?";
        String selectArgs[]={"8"};
        Cursor c=resolver.query(uri,projection,where,selectArgs,MediaStore.Files.FileColumns._ID);
        if(c!=null) {
            c.moveToNext();
            int index = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String data = c.getString(index);
            Log.i("zhengyu", "data:" + data);
        }
    }

    private void onshareBtnClicked() {
        Uri imageUri = Uri.parse("content://com.android.externalstorage.documents/document/4945-1AE7%3AIMG_20161102_151551.jpg");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "share"));
    }





}
