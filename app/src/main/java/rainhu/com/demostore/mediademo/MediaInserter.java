package rainhu.com.demostore.mediademo;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huzhengyu on 16-12-9.
 */

public class MediaInserter {
    private final HashMap<Uri, List<ContentValues>> mRowMap =
            new HashMap<Uri, List<ContentValues>>();
    private final HashMap<Uri, List<ContentValues>> mPriorityRowMap =
            new HashMap<Uri, List<ContentValues>>();

    private final ContentProviderClient mProvider;
    private final int mBufferSizePerUri;

    public MediaInserter(ContentProviderClient provider, int bufferSizePerUri) {
        mProvider = provider;
        mBufferSizePerUri = bufferSizePerUri;
    }

    public void insert(Uri tableUri, ContentValues values) throws RemoteException {
        insert(tableUri, values, false);
    }

    public void insertwithPriority(Uri tableUri, ContentValues values) throws RemoteException {
        insert(tableUri, values, true);
    }

    private void insert(Uri tableUri, ContentValues values, boolean priority) throws RemoteException {
        HashMap<Uri, List<ContentValues>> rowmap = priority ? mPriorityRowMap : mRowMap;
        List<ContentValues> list = rowmap.get(tableUri);
        if (list == null) {
            list = new ArrayList<ContentValues>();
            rowmap.put(tableUri, list);
        }
        list.add(new ContentValues(values));
        if (list.size() >= mBufferSizePerUri) {
            flushAllPriority();
            flush(tableUri, list);
        }
    }

    public void flushAll() throws RemoteException {
        flushAllPriority();
        for (Uri tableUri : mRowMap.keySet()){
            List<ContentValues> list = mRowMap.get(tableUri);
            flush(tableUri, list);
        }
        mRowMap.clear();
    }

    private void flushAllPriority() throws RemoteException {
        for (Uri tableUri : mPriorityRowMap.keySet()){
            List<ContentValues> list = mPriorityRowMap.get(tableUri);
            flush(tableUri, list);
        }
        mPriorityRowMap.clear();
    }

    private void flush(Uri tableUri, List<ContentValues> list) throws RemoteException {
        if (!list.isEmpty()) {
            ContentValues[] valuesArray = new ContentValues[list.size()];
            valuesArray = list.toArray(valuesArray);
            mProvider.bulkInsert(tableUri, valuesArray);
            list.clear();
        }
    }
}