package rainhu.com.demostore.EasyBrowser;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Created by Ryan on 2017/8/22.
 */

public class EasyBrowserUtils {
    private boolean mTopDir; //flag to identify cur dir is top dir
    private String mCurDir;  //indicate current directory
    private List<String> mSubDirs = new ArrayList<>();  //store subDirectorys
    private EasyBrowserAdapter mEasyBrowserAdapter;
    private String mRootPath = "／";

    private Stack<String> mIndexs = new Stack<>(); //for directory navigation


    // SET ROOT PATH AS /storage/
    private String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public EasyBrowserUtils(){
        this(null);
    }

    public EasyBrowserUtils(String path){
        mTopDir = false;
        if(path == null){
            mCurDir = ROOT_PATH;
        } else {
            mCurDir = path;
        }
        mRootPath = mCurDir;
        mIndexs.push(new File(mCurDir).getName());
        gotoDir(mCurDir);
    }

    public List<String> getSubDirs(){
        return mSubDirs;
    }

    public String getCurDir(){
        return mCurDir;
    }

    public boolean isTopDir(){
        return mTopDir;
    }

    public Stack<String> getIndexs(){
        return mIndexs;
    }

    public void goToUpperDir(){
        if(isTopDir()){
            return;
        }
        if(!mIndexs.isEmpty()) {
            mIndexs.pop();
        }
        gotoDir(getParentPath(mCurDir));
    }

    public void goToSubDir(String filename){
        if(!mSubDirs.contains((new File(filename)).getName())){
            return;
        }
        mCurDir = mCurDir + "/" + filename;
        mIndexs.add(filename);
        gotoDir(mCurDir);
    }

    /**
     *  Get the sub dirs of identified path
     *
     * @param parentPath
     * @return return the sub dirs of the parentPath
     * */
    private List<String> getSubDirs(String parentPath){
        List<String> dirs = new ArrayList<>();
        File parentFile = new File(parentPath);

        //return null parentFile is not a directory
        if(!parentFile.exists() || !parentFile.isDirectory()){
            return dirs;
        }

        for(File file : parentFile.listFiles()){
            dirs.add(file.getName());
        }

        Collections.sort(dirs, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        return dirs;
    }


    public String getParentPath(String path){
        String parentPath = path.substring(0, path.lastIndexOf("/"));
        if(parentPath.equals("")){
            return "/";
        } else {
            return parentPath;
        }
    }

    /**
     *  goto identified path
     *
     *  @param destPath 绝对路径
     *
     * */
    private void gotoDir(String destPath) {
        mCurDir = destPath;
        mSubDirs = getSubDirs(destPath);

        if(mCurDir.equals("/") || mCurDir.equals(ROOT_PATH)){
            mTopDir = true;
        } else {
            mTopDir = false;
        }
    }

    public void onIndexChanged(Stack indexs){
        if (indexs.size() <= 1){
            gotoDir(mRootPath);
        } else {
            String filepath = mRootPath;
            for(int i = 1;i<indexs.size();i++){
                filepath += "/" +indexs.get(i);
            }
            mCurDir = filepath;
            gotoDir(mCurDir);
        }
    }
}
