package com.yasinzhang.applock.asynctasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.yasinzhang.applock.commons.AppInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppInfoRetrieveTask extends AsyncTaskWrapper<List<AppInfo>> {

    private Context mContext = null;

    public AppInfoRetrieveTask(Context context){
        super();
        mContext = context;
    }

    @Override
    protected List<AppInfo> process() {
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> installedAppInfos = pm.getInstalledApplications(0);
        Iterator<ApplicationInfo> it = installedAppInfos.iterator();

        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        while(it.hasNext()){
            ApplicationInfo info = it.next();
            AppInfo appInfo = new AppInfo();
            appInfo.appIcon = info.loadIcon(pm);
            appInfo.appName = info.loadLabel(pm).toString();
            appInfo.packageName = info.packageName;
            appInfos.add(appInfo);
            //appInfo.flag =
            Log.e("fdfd", info.loadLabel(pm).toString()+"----"+info.packageName+ info.flags);
        }


        return appInfos;
    }
}
