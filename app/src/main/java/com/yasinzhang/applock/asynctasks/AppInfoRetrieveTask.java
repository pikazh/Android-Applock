package com.yasinzhang.applock.asynctasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.yasinzhang.applock.AppLockApplication;
import com.yasinzhang.applock.R;
import com.yasinzhang.applock.commons.AppInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AppInfoRetrieveTask extends AsyncTaskWrapper<List<AppInfo>> {

    private Set<String> blacklist = new HashSet<>();
    private ArrayList<String> partBlackList = new ArrayList<>();

    protected void loadBlackListApps(){
        Context context = AppLockApplication.getInstance();

        try {
            InputStream in = context.getResources().openRawResource(R.raw.sysapp);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            in.close();

            JSONObject root = new JSONObject(sb.toString());
            JSONArray jsonBlackList = root.getJSONArray("blacklist");
            for (int i = 0; i< jsonBlackList.length(); i++) {
                String str = jsonBlackList.getString(i);
                if(str.charAt(str.length()-1) == '*'){
                    partBlackList.add(str.substring(0, str.length()-1));
                }else{
                    blacklist.add(str);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean checkPackage(String packageName){
        if(blacklist.contains(packageName))
            return false;

        Iterator<String> it = partBlackList.iterator();
        while(it.hasNext()){
            String filter = it.next();
            if(packageName.indexOf(filter) == 0){
                return false;
            }
        }

        return true;
    }

    @Override
    protected List<AppInfo> process() {

        loadBlackListApps();

        PackageManager pm = AppLockApplication.getInstance().getPackageManager();
        List<ApplicationInfo> installedAppInfos = pm.getInstalledApplications(0);
        Iterator<ApplicationInfo> it = installedAppInfos.iterator();

        List<AppInfo> appInfos = new ArrayList<>();

        while(it.hasNext()){
            ApplicationInfo info = it.next();
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0){

                if(info.icon != 0 && checkPackage(info.packageName)){
                    AppInfo appInfo = new AppInfo();
                    appInfo.appIcon = info.loadIcon(pm);
                    appInfo.flag = AppInfo.FLAG_GENERAL_SYSTEM_APP;
                    appInfo.appName = info.loadLabel(pm).toString();
                    appInfo.packageName = info.packageName;
                    appInfos.add(appInfo);
                    Log.e("fdfd", info.packageName+" "+appInfo.appName);
                }
            }
            else{
                if(checkPackage(info.packageName)){
                    AppInfo appInfo = new AppInfo();
                    appInfo.appIcon = info.loadIcon(pm);
                    appInfo.appName = info.loadLabel(pm).toString();
                    appInfo.packageName = info.packageName;
                    appInfos.add(appInfo);
                    Log.e("fdfd", info.packageName+" "+appInfo.appName);
                }
            }
        }

        return appInfos;
    }
}
