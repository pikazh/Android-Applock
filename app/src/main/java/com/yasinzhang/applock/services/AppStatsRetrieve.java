package com.yasinzhang.applock.services;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.yasinzhang.applock.OverLayPatternLockController;
import com.yasinzhang.applock.commons.Preference;

import androidx.annotation.Nullable;


public class AppStatsRetrieve extends Service implements Handler.Callback {

    private static final int FOREGROUND_APP_INFO_MESSAGE = 1;

    OverLayPatternLockController mLockController = new OverLayPatternLockController();

    HandlerThread queryAppStatsThread = null;
    QueryForeGroundAppInfoTask task = null;

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {

        beginToQuery();
        return START_STICKY;
    }

    protected void beginToQuery() {
        if (queryAppStatsThread == null) {
            queryAppStatsThread = new HandlerThread("queryapp");
            queryAppStatsThread.start();
            task = new QueryForeGroundAppInfoTask(queryAppStatsThread.getLooper(), this);
            task.startTask();
        }
    }

    protected void stopQuery() {
        if (queryAppStatsThread != null) {
            task.stopTask();
            task = null;
            queryAppStatsThread.quitSafely();
            queryAppStatsThread = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopQuery();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == FOREGROUND_APP_INFO_MESSAGE) {
            dealWith((ForegroundAppInfo)msg.obj);
            return true;
        }

        return false;
    }

    protected void dealWith(ForegroundAppInfo info) {

        Log.e("sdf", info.className + " " + " " + info.packageName);
        if (info.packageName.compareTo("com.android.settings") == 0) {
            Preference preference = Preference.getInstance();
            if (!mLockController.isShowing() &&
                    preference.encryptionPatternLock != null &&
                    !preference.encryptionPatternLock.isEmpty()) {
                PackageManager pm = getPackageManager();
                Drawable icon = null;
                try {
                    icon = pm.getApplicationIcon(info.packageName);
                } catch (Exception e) {
                    icon = null;
                    e.printStackTrace();
                }
                mLockController.showAndCheckPattern(preference.encryptionPatternLock, icon);
            }
        } else if (info.packageName.indexOf("com.android.launcher") == 0 || info.packageName.compareTo("com.android.systemui") == 0) {
            if (mLockController.isShowing()) {
                mLockController.close();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class QueryForeGroundAppInfoTask extends Handler {

        private static final long QUERY_INFO_INTERVAL = 800;

        private Handler callBackhandler = null;
        private boolean needStop = false;

        public QueryForeGroundAppInfoTask(Looper looper, Handler.Callback callback) {
            super(looper);
            callBackhandler = new Handler(callback);
        }

        public void stopTask() {
            needStop = true;
        }

        public void startTask() {
            this.post(() -> {
                UsageStatsManager usageStatsManager =
                        (UsageStatsManager) getSystemService(Service.USAGE_STATS_SERVICE);


                long now = System.currentTimeMillis();
                long lastTime = now - QUERY_INFO_INTERVAL;

                try {

                    while (!needStop) {
                        UsageEvents usageEvents = usageStatsManager.queryEvents(lastTime, now);
                        if (usageEvents != null) {

                            UsageEvents.Event event = new UsageEvents.Event();
                            UsageEvents.Event lastEvent = null;
                            while (usageEvents.getNextEvent(event)) {
                                if (event.getEventType() != UsageEvents.Event.MOVE_TO_FOREGROUND) {
                                    continue;
                                }

                                if (lastEvent == null || lastEvent.getTimeStamp() < event.getTimeStamp()) {
                                    lastEvent = event;
                                }
                            }

                            if (lastEvent != null) {
                                Message msg = Message.obtain(null, FOREGROUND_APP_INFO_MESSAGE, new ForegroundAppInfo(lastEvent.getPackageName(), lastEvent.getClassName()));
                                callBackhandler.sendMessage(msg);
                            }
                        }

                        Thread.sleep(800);

                        lastTime = now;
                        now = System.currentTimeMillis();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("fdfd", ".....................");
                }
            });
        }
    }

    static class ForegroundAppInfo {

        public ForegroundAppInfo(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String packageName;
        public String className;
    }
}
