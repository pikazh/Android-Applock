package com.yasinzhang.applock;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.util.Log;

import com.yasinzhang.applock.db.AppDatabase;

public class AppLockApplication extends Application {

    private static AppLockApplication instance = null;

    public static AppLockApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        Log.e("app", "WindowChangeEventListener.onTrimMemory:"+level);

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
}
