package com.yasinzhang.applock;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yasinzhang.applock.activities.LauncherActivity;
import com.yasinzhang.applock.asynctasks.AsyncTaskWrapper;
import com.yasinzhang.applock.commons.Preference;
import com.yasinzhang.applock.db.AppDatabase;
import com.yasinzhang.applock.db.ConfigDao;
import com.yasinzhang.applock.db.ConfigRecord;
import com.yasinzhang.applock.services.AppStatsRetrieve;

import java.util.HashSet;
import java.util.Set;

public class AppLockApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AppLockApplication instance = null;

    private Set<Activity> mStartedActivities = new HashSet<>();
    private boolean mJustFirstActivityStartedWhenReturnFromBackground = true;

    public static AppLockApplication getInstance(){
        return instance;
    }

    public boolean isAppAtForeground(){
        return !mStartedActivities.isEmpty();
    }

    public boolean onJustFirstActivityStartedWhenReturnFromBackGround(){
        return mJustFirstActivityStartedWhenReturnFromBackground;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        registerActivityLifecycleCallbacks(this);

        AsyncTaskWrapper<ConfigRecord> task = new AsyncTaskWrapper<ConfigRecord>() {
            @Override
            protected ConfigRecord process() {
                ConfigDao configDao = AppDatabase.getDatabase().configModel();
                ConfigRecord record = configDao.getConfig();
                if(record == null){
                    record = new ConfigRecord();
                    configDao.insertRecord(record);
                }

                return record;
            }
        };

        task.registerCallback(conf->{
            Preference preference = Preference.getInstance();
            preference.encryptionPatternLock = conf.encryptionPatternLock;
            preference.isInitialized.setValue(true);
        });

        task.start();

        startService(new Intent(this, AppStatsRetrieve.class));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        Log.e("app", "AppStatsRetrieve.onTrimMemory:"+level);

        if(level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE){
            AppDatabase.destroyInstance();
        }
        else if(level < ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            if(level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW){
                AppDatabase.destroyInstance();
            }

            if(level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL){

            }
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(mStartedActivities.add(activity)){
            if(mStartedActivities.size() == 1){
                mJustFirstActivityStartedWhenReturnFromBackground = true;
            }else{
                mJustFirstActivityStartedWhenReturnFromBackground = false;
            }
        }

        if(onJustFirstActivityStartedWhenReturnFromBackGround() &&
                activity.getClass() != LauncherActivity.class){
            startActivity(new Intent(this, LauncherActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mStartedActivities.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
