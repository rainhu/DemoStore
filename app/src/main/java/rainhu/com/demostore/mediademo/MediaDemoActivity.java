package rainhu.com.demostore.mediademo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rainhu.com.demostore.R;

/**
 * Created by huzhengyu on 16-11-17.
 */

public class MediaDemoActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "mediademo";

    private Button mShareBtn;
    private Button mQueryBtn;
    private EditText mOpText;
    private Button mDeleteBtn;

    private Context mContext;

    private Button mSaveMediaProviderDBBtn;
    private ContentProviderClient mMediaProvider;
    private Button mCancleCrawlBtn;

    private ProgressBar mProgressBar;

    private TextView mDisplayBoards;
    private Button mClearBtn;

    CrawlMediaDbTask mCrawlTask = null;

    private Button mOpenBtn;

    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediademo);
        mContext = this;

        mShareBtn = (Button) findViewById(R.id.mediademo_shareBtn);
        mShareBtn.setOnClickListener(this);

        mQueryBtn = (Button) findViewById(R.id.mediademo_queryBtn);
        mQueryBtn.setOnClickListener(this);

        mOpText = (EditText) findViewById(R.id.mediademo_opNumber);
        mDeleteBtn = (Button) findViewById(R.id.mediademo_deleteBtn);
        mDeleteBtn.setOnClickListener(this);

        mSaveMediaProviderDBBtn = (Button) findViewById(R.id.mediademo_saveMediaProviderDB);
        mSaveMediaProviderDBBtn.setOnClickListener(this);

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MediaDemoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Log.i("zhengyu", "have permission");
        } else {
            Log.i("zhengyu", "do not have permission");
            //ActivityCompat.shouldShowRequestPermissionRationale(MediaDemoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        }

        mProgressBar = (ProgressBar) findViewById(R.id.mediademo_processBar);
        mDisplayBoards = (TextView) findViewById(R.id.mediademo_displayBoards);

        mClearBtn = (Button) findViewById(R.id.mediademo_clearBtn);
        mClearBtn.setOnClickListener(this);

        mCancleCrawlBtn = (Button) findViewById(R.id.mediademo_cancle_CrawlDbTask);
        mCancleCrawlBtn.setOnClickListener(this);

        mOpenBtn = (Button) findViewById(R.id.mediademo_open);
        mOpenBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("zhengyu", "onRequestPermissionsResult" + grantResults[0]);
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted 用户允许权限 继续执行（

                }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.mediademo_clearBtn:
                clearDisplayBoards();
                break;
            case R.id.mediademo_cancle_CrawlDbTask:
                cancleCrawlTask();
                break;
            case R.id.mediademo_open:
                openFileBySAF();
            default:
                break;
        }
    }

    private void openFileBySAF() {

        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE); //获取选择目录
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE); //显示结果只有可以打开的文件
        intent.setType("image/*");
        //intent.setFlags(Intent.EXTRA_ALLOW_MULTIPLE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    // Here are some examples of how you might call this method.
// The first parameter is the MIME type, and the second parameter is the name
// of the file you are creating:
//
// createFile("text/plain", "foobar.txt");
// createFile("image/png", "mypicture.png");
// Unique request code.
    private void createFileBySAF(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    // 删除文件
//    前提是Document.COLUMN_FLAGS包含SUPPORTS_DELETE
//
//    DocumentsContract.deleteDocument(getContentResolver(), uri);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri treeUri = data.getData();
            Log.i(TAG, "Uri: " + treeUri.toString());

//                //for directory choose
//                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
//
//                // List all existing files inside picked directory
//                for (DocumentFile file : pickedDir.listFiles()) {
//                    Log.i(TAG, "Found file " + file.getName() + " with size " + file.length());
//                }
//
//                // Create a new file and write into it
//                OutputStream out = null;
//                try {
//                    DocumentFile dcimFolder = pickedDir.findFile("DCIM");
//                    if (dcimFolder != null && dcimFolder.isDirectory()) {
//                        Log.i(TAG, "Found DCIM Directory");
//                    } else {
//                        Log.i(TAG, "Not found DCIM Directory");
//                    }
//
//                    //Create file
//                    DocumentFile newFile = pickedDir.createFile("text/plain", "new_file");
//                } catch (Exception e) {
//                    Log.i(TAG, e.toString());
//                }
        }
    }

    private void cancleCrawlTask() {
        if (mCrawlTask != null) {
            mCrawlTask.cancel(true);
            mProgressBar.setProgress(0);

            mCancleCrawlBtn.setEnabled(false);
            mSaveMediaProviderDBBtn.setEnabled(true);
            Toast.makeText(mContext, "crawl MediaProvider DB cancle !", Toast.LENGTH_SHORT).show();
        }

    }


    class MyDbHelper extends SQLiteOpenHelper {

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


    private class CrawlMediaDbTask extends AsyncTask<Void, Integer, Void> {
        Cursor c = null;
        String where = null;
        String[] selectionArgs = null;

        //content://media/external/file/
        Uri filesUri = MediaStore.Files.getContentUri("external");
        long lastId = Long.MIN_VALUE;
        Uri limitUri = filesUri.buildUpon().appendQueryParameter("limit", "1000").build();

        final String EXTERNAL_DATABASE_NAME = "external.db";

        private final String[] FILES_PROJECTION = new String[]{
                MediaStore.Files.FileColumns._ID, // 0
                MediaStore.Files.FileColumns.DATA, // 1
                "format",
                MediaStore.Files.FileColumns.DATE_MODIFIED, // 2
        };


        SQLiteDatabase db = null;

        @Override
        protected void onPreExecute() {
            mMediaProvider = mContext.getContentResolver()
                    .acquireContentProviderClient(MediaStore.AUTHORITY);

            where = MediaStore.Files.FileColumns._ID + ">?";
            selectionArgs = new String[]{""};
            mProgressBar.setProgress(0);


            mCancleCrawlBtn.setEnabled(true);
            mSaveMediaProviderDBBtn.setEnabled(false);
        }


        //12-09 14:29:27.827 22128 22128 I zhengyu : have permission
//        12-09 14:29:28.906 22128 22158 I zhengyu : db path :/data/user/0/rainhu.com.demonstore/databases/external.db
//        12-09 14:29:28.991 22128 22158 I zhengyu : countQuery takes :0 seconds
//        12-09 14:29:28.991 22128 22158 I zhengyu : count:51145
//                12-09 14:38:11.260 22128 22158 I zhengyu : insert DB takes :522 seconds
//        12-09 14:38:11.263 22128 22158 I zhengyu : now begin to copy file
//        12-09 14:38:11.367 22128 22158 I zhengyu : copy takes : 0 seconds


        @Override
        protected Void doInBackground(Void... params) {
            File dbFile = mContext.getDatabasePath(EXTERNAL_DATABASE_NAME);
            ///data/user/0/rainhu.com.demonstore/databases/external.db
            Log.i("zhengyu", "db path :" + dbFile.getPath());
            if (dbFile.exists()) {
                dbFile.delete();
            }
            long countQueryTime = -1;

            try {
                long start = System.currentTimeMillis();

                //Log.i("zhengyu", "dbPath :"+dbFile.getAbsolutePath());
                MyDbHelper dbHelper = new MyDbHelper(mContext, EXTERNAL_DATABASE_NAME, null, 1);

                db = dbHelper.getWritableDatabase();
                c = mMediaProvider.query(filesUri, new String[]{"count(*) AS count"},
                        null, null, null);

                countQueryTime = System.currentTimeMillis();
                Log.i("zhengyu", "countQuery takes :" + (System.currentTimeMillis() - start) / 1000 + " seconds");

                c.moveToFirst();
                long count = c.getInt(0);
                Log.i("zhengyu", "count:" + count);
                int i = 0;
                while (true) {

                    publishProgress((int) (i++ * 100 / (count / 1000)));

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
                        values.put("_id", rowId);
                        values.put("_data", path);
                        values.put("format", format);
                        values.put("date_modified", lastModified);
                        db.insert("files", null, values);
                    }


                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    c.close();
                }
                if (db != null) {
                    db.close();
                }
            }
            long insertDatabaseTime = System.currentTimeMillis();
            Log.i("zhengyu", "insert DB takes :" + (insertDatabaseTime - countQueryTime) / 1000 + " seconds");

            Log.i("zhengyu", "now begin to copy file");

            //copy external.db to /storage/emulated/0/external.db
            String target = Environment.getExternalStorageDirectory() + "/" + EXTERNAL_DATABASE_NAME;
            if (dbFile.exists()) {
                copyFile(dbFile.getPath(), target);
            }

            Log.i("zhengyu", "copy takes : " + (System.currentTimeMillis() - insertDatabaseTime) / 1000 + " seconds");
            publishProgress(100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mCancleCrawlBtn.setEnabled(false);
            mSaveMediaProviderDBBtn.setEnabled(true);

            Toast.makeText(mContext, "crawl MediaProvider DB Success !", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyFile(String oldPath, String newPath) {
        InputStream fis = null;
        OutputStream fos = null;

        try {
            fis = new FileInputStream(oldPath);
            fos = new FileOutputStream(newPath);
            byte[] buf = new byte[4096];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            Log.i("zhengyu", e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSaveMediaProviderDBClicked() {
        mCrawlTask = new CrawlMediaDbTask();
        mCrawlTask.execute();

    }

    private void onDeleteBtnClicked() {

        String number = mOpText.getText().toString();

        ContentResolver resolver = getApplicationContext().getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        Log.i("zhengyu", "uri---->" + uri.toString());

        String projection[] = {MediaStore.Files.FileColumns.DATA};
        String where = MediaStore.Files.FileColumns._ID + "=?";
        //String selectArgs[]={"8"};
        String selectArgs[] = {number};
        Cursor c = null;
        c = resolver.query(uri, projection, where, selectArgs, MediaStore.Files.FileColumns._ID);

        if (c != null && c.getCount() > 0) {
            c.moveToNext();
            int index = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String data = c.getString(index);
        }

        //  ArrayMap<String,String> d = new ArrayMap<String, String>();

        int i = resolver.delete(MediaStore.Files.getContentUri("external"), MediaStore.Files.FileColumns._ID + "=?", selectArgs);
        if (i == 0) {
            setTextToDisplayBoards("no rowId# " + number + " record exists");
        } else {
            setTextToDisplayBoards("Delete rowId " + number + " succeed !");
        }
    }

    private void onQueryBtnClicked() {
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        Log.i("zhengyu", "uri---->" + uri.toString());

        String projection[] = {MediaStore.Files.FileColumns.DATA};
        String where = MediaStore.Files.FileColumns._ID + "=?";

        String selectArgs[] = {mOpText.getText().toString()};
        Cursor c = resolver.query(uri, projection, where, selectArgs, MediaStore.Files.FileColumns._ID);
        if (c != null && c.getCount() > 0) {
            c.moveToNext();
            int index = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String data = c.getString(index);
            setTextToDisplayBoards(data);
        } else {
            setTextToDisplayBoards("no rowId# " + mOpText.getText().toString() + " record exists");
        }

        c.close();
    }

    private void onshareBtnClicked() {
        //Uri imageUri = Uri.parse("content://com.android.externalstorage.documents/document/4945-1AE7%3AIMG_20161102_151551.jpg");
        String rowId = mOpText.getText().toString();

        //   content://media/external//images/media/rowid
        Uri imageUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + rowId);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "share"));

        setTextToDisplayBoards("Intent to share " + imageUri);

    }


    private void setTextToDisplayBoards(String text) {
        mDisplayBoards.setText(text + "\n" + mDisplayBoards.getText());
    }

    private void clearDisplayBoards() {
        mDisplayBoards.setText("");
    }
}
