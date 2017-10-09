package rainhu.com.demostore.EasyBrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rainhu.com.demostore.R;

/**
 * Created by Ryan on 2017/8/22.
 */

public class EasyBrowserAdapter extends BaseAdapter {
    private List<String> items;
    private LayoutInflater mInflater;


    public EasyBrowserAdapter(Context mContext, List<String> items){
        this.items = items;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.easybrowser_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.easybrowser_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(items.get(position));

        return convertView;
    }


    public class ViewHolder{
        public TextView title;
    }
}
