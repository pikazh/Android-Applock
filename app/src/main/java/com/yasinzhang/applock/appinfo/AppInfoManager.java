package com.yasinzhang.applock.appinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public class AppInfoManager extends AsyncTask<Void, Void, List<AppInfo>> {

    private Context mContext = null;
    private CallBackWarpper mCallbackWarpper = null;

    public AppInfoManager(Context context){
        super();
        mContext = context;
    }

    @Override
    protected List<AppInfo> doInBackground(Void... voids) {
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> installedAppInfos = pm.getInstalledApplications(0);
        Iterator<ApplicationInfo> it = installedAppInfos.iterator();

        List<AppInfo> appInfos = new ArrayList<>();
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

    @Override
    protected void onPostExecute(List<AppInfo> infos) {
        super.onPostExecute(infos);

        if(mCallbackWarpper != null && mCallbackWarpper.owner != null && mCallbackWarpper.cb != null){
            if(mCallbackWarpper.owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)){
                mCallbackWarpper.cb.onFinish(infos);
            }
        }

        mCallbackWarpper = null;

    }

    public void getAppinfos(@NonNull LifecycleOwner lifeCycleOwner, @NonNull CallBack callBack){

        mCallbackWarpper = new CallBackWarpper();
        mCallbackWarpper.cb = callBack;
        mCallbackWarpper.owner = lifeCycleOwner;
        this.execute();

    }


    public interface CallBack{
        void onFinish(@NonNull List<AppInfo> infos);
    }

    class CallBackWarpper{
        public CallBack cb;
        public LifecycleOwner owner;
    }

}
