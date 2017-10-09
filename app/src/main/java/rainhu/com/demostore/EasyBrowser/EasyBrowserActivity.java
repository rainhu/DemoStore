package rainhu.com.demostore.EasyBrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Stack;

import rainhu.com.demostore.R;

/**
 * Created by Ryan on 2017/8/22.
 */

public class EasyBrowserActivity extends Activity{
    public static final String TAG = "easybrowser";

    private ListView mListView = null;

    private EasyBrowserUtils mEasyBrowserUtils = null;
    private EasyBrowserAdapter mEasyBrowserAdapter = null;
    private Context mContext;

    private IndexView mIndexView = null;
    private IndexAdapter mIndexAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easybrowser_activity_main);
        mContext = this;
        init();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }


    private void init() {
        mListView = (ListView) findViewById(R.id.easybrowser_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EasyBrowserAdapter.ViewHolder holder = (EasyBrowserAdapter.ViewHolder) view.getTag();
                String filename = (String) holder.title.getText();
                Log.i(TAG,"fileName :"+filename);
                //mEasyBrowserUtils.gotoDir(mEasyBrowserUtils.getCurDir() + "/" + filename );
                mEasyBrowserUtils.goToSubDir(filename);
                mEasyBrowserAdapter.setItems(mEasyBrowserUtils.getSubDirs());
                mIndexAdapter.setIndexs(mEasyBrowserUtils.getIndexs());
            }
        });

        mEasyBrowserUtils = new EasyBrowserUtils();
        mEasyBrowserAdapter = new EasyBrowserAdapter(mContext, mEasyBrowserUtils.getSubDirs());
        mListView.setAdapter(mEasyBrowserAdapter);

        mIndexView = (IndexView) findViewById(R.id.easybrowser_indexview);
        mIndexAdapter = new IndexAdapter(mContext, mEasyBrowserUtils.getIndexs());
        mIndexAdapter.setOnIndexClicked(new IndexAdapter.OnIndexClicked() {
            @Override
            public void updateEasyBrowser(Stack indexs) {
                mEasyBrowserUtils.onIndexChanged(indexs);
                mEasyBrowserAdapter.setItems(mEasyBrowserUtils.getSubDirs());
            }
        });
        mIndexView.setAdapter(mIndexAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mEasyBrowserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(mEasyBrowserUtils.isTopDir()) {
            super.onBackPressed();
        } else {
            mEasyBrowserUtils.goToUpperDir();
            mEasyBrowserAdapter.setItems(mEasyBrowserUtils.getSubDirs());
            mIndexAdapter.setIndexs(mEasyBrowserUtils.getIndexs());
        }
    }

}
