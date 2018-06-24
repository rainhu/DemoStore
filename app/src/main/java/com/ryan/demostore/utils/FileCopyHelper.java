package com.ryan.demostore.utils;

/**
 * Created by huzhengyu on 16-12-22.
 */
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



public class FileCopyHelper {
    /** The context. */
    private Context mContext;

    /** The files added. */
    private ArrayList<String> mFilesList;

    /**
     * Instantiates a new file copy helper.
     *
     * @param context the context
     */
    public FileCopyHelper(Context context) {
        mContext = context;
        mFilesList = new ArrayList<String>();
    }

    /**
     * Copy the file from the resources with a filename .
     *
     * @param resId the res id
     * @param fileName the file name
     *
     * @return the absolute path of the destination file
     * @throws IOException
     */
    public String copy(int resId, String fileName) throws IOException {
        InputStream source = mContext.getResources().openRawResource(resId);
        OutputStream target = mContext.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
        copyFile(source, target);
        mFilesList.add(fileName);
        return mContext.getFileStreamPath(fileName).getAbsolutePath();
    }

    public void copyToExternalStorage(int resId, File path) throws IOException {
        InputStream source = mContext.getResources().openRawResource(resId);
        OutputStream target = new FileOutputStream(path);
        copyFile(source, target);
    }

    private void copyFile(InputStream source, OutputStream target) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            for (int len = source.read(buffer); len > 0; len = source.read(buffer)) {
                target.write(buffer, 0, len);
            }
        } finally {
            if (source != null) {
                source.close();
            }
            if (target != null) {
                target.close();
            }
        }
    }

    /**
     * Delete all the files copied by the helper.
     */
    public void clear(){
        for (String path : mFilesList) {
            mContext.deleteFile(path);
        }
    }
}
