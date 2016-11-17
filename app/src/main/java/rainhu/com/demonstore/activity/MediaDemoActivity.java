package rainhu.com.demonstore.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
            default:
                break;
        }
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
