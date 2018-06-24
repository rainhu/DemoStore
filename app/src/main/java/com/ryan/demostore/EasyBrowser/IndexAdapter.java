package com.ryan.demostore.EasyBrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Stack;

import com.ryan.demostore.R;

/**
 * Created by Ryan on 2017/8/22.
 */

public class IndexAdapter extends BaseAdapter {
    private Stack<String> mIndexs;
    private Context mContext;
    private LayoutInflater mInflater;
    OnIndexClicked mOnIndexClicked;

    public IndexAdapter(Context context, Stack<String> indexs){
        this.mIndexs = indexs;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setIndexs(Stack indexs){
        this.mIndexs = indexs;
        notifyDataSetChanged();
    }

    public void setOnIndexClicked(OnIndexClicked mOnIndexClicked) {
        this.mOnIndexClicked = mOnIndexClicked;
    }

    @Override
    public int getCount() {
        return mIndexs.size();
    }

    @Override
    public Object getItem(int position) {
        return mIndexs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IndexviewHolder indexViewHolder = null;
        if(convertView == null){
            indexViewHolder = new IndexviewHolder();

            convertView = mInflater.inflate(R.layout.easy_browser_index_item, null);
            indexViewHolder.icon = (TextView) convertView.findViewById(R.id.easybrowser_index_icon);
            indexViewHolder.dir = (TextView) convertView.findViewById(R.id.easybrowser_index_dir);

            String icon = "";
            String dir = "Home";

            if(position != 0) {
                icon = " > ";
                dir = mIndexs.get(position);
            }
            indexViewHolder.icon.setText(icon);
            indexViewHolder.dir.setText(dir);
            indexViewHolder.dir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //click the last item of the index
                    if(position == mIndexs.size() -1){
                        return;
                    }

                    for(int i = mIndexs.size()-1 ; i > position ; i-- ) {
                        mIndexs.pop();
                        mOnIndexClicked.updateEasyBrowser(mIndexs);
                        notifyDataSetChanged();
                    }
                }
            });

        } else {
            indexViewHolder = (IndexviewHolder) convertView.getTag();
        }

        return convertView;
    }

    public interface OnIndexClicked{
       public void updateEasyBrowser(Stack indexs);
    }

    public class IndexviewHolder{
        public TextView icon;
        public TextView dir;

    }

}
