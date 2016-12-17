package rainhu.com.demostore.applock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import rainhu.com.demostore.R;
import rainhu.com.demostore.logger.Log;

/**
 * Created by hu on 16-12-14.
 */

public class AppListAdapter extends BaseAdapter {
    private List<AppInfo> appInfoList;
    private Context mContext;
    private LayoutInflater mInflater;

    public AppListAdapter(Context context, List<AppInfo> appInfoList) {
        mContext = context;
        this.appInfoList = appInfoList;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppInfoViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.applock_item, null);
            viewHolder = new AppInfoViewHolder();
            viewHolder.app_icon = (ImageView) convertView.findViewById(R.id.applock_applist_icon);
            viewHolder.app_name = (TextView) convertView.findViewById(R.id.applock_applist_name);
            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.applock_applist_switch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AppInfoViewHolder) convertView.getTag();
        }

        AppInfo appInfo = getItem(position);
        viewHolder.app_icon.setImageDrawable(appInfo.getAppIcon());
        viewHolder.app_name.setText(appInfo.getAppLabel());
        viewHolder.aSwitch.setChecked(appInfo.getStatus() == 0 ? false : true);
        viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int newStatus;
//                if (isChecked) {
//                    newStatus = AppLockMetadata.NEED_APPLOCK;
//                } else {
//                    newStatus = AppLockMetadata.NEED_NOT_APPLOCK;
//                }
//
//                appInfoList.get(position).setStatus(newStatus);
//                AppLockUtils.updateAppStatus(mContext, appInfoList.get(position).getPackageName(), newStatus);
            }
        });

        return convertView;
    }

    public class AppInfoViewHolder {
        public ImageView app_icon;
        public TextView app_name;
        public Switch aSwitch;
    }
}
