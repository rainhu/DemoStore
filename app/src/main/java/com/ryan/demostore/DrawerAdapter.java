package com.ryan.demostore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 2018/5/13.
 */

class DemoStoreMenuItem {
    String menuTitle;
    int menuIcon;

    public DemoStoreMenuItem(String title, int icon){
        menuTitle = title;
        menuIcon = icon;
    }
}

class DrawViewHolder {
    TextView drawViewId;
}


public class DrawerAdapter extends BaseAdapter {
    List<DemoStoreMenuItem> menuItems = new ArrayList<DemoStoreMenuItem>();
    Context mContext;

    public DrawerAdapter(Context context){
        mContext = context;

        menuItems.add(new DemoStoreMenuItem("数据填充",R.drawable.ic_storagefillter));
        //menuItems.add(new DemoStoreMenuItem("数据填充",R.drawable.ic_storagefillter));
        //menuItems.add(new DemoStoreMenuItem("数据填充",R.drawable.ic_storagefillter));
        menuItems.add(new DemoStoreMenuItem("设置",R.drawable.ic_storagefillter));

    }



    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DrawViewHolder holder;
        if(convertView == null){
            holder = new DrawViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menudrawer_item, parent, false);
            holder.drawViewId = (TextView) convertView.findViewById(R.id.drawerItem);
            convertView.setTag(holder);
        } else {
            holder = (DrawViewHolder) convertView.getTag();
        }

        DemoStoreMenuItem item = menuItems.get(position);
        //holder.

        return convertView;
    }
}
