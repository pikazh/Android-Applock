package com.yasinzhang.applock.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.yasinzhang.applock.OverLayPatternLockController;
import com.yasinzhang.applock.R;
import com.yasinzhang.applock.activities.LauncherActivity;
import com.yasinzhang.applock.commons.Preference;

import androidx.annotation.Nullable;


public class AppStatsRetrieve extends Service implements Handler.Callback {

    private static final int FOREGROUND_APP_INFO_MESSAGE = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "Applock_Channel";

    OverLayPatternLockController mLockController = new OverLayPatternLockController();

    private boolean mQueryServiceStarted = false;
    private UsageStatsManager mUsageStatsManager = null;
    private Handler mHandler = null;

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {

        beginToQuery();
        return START_STICKY;
    }

    protected void beginToQuery() {
        if (!mQueryServiceStarted) {
            mQueryServiceStarted = true;

            Intent nfIntent = new Intent(this, LauncherActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String appName = getResources().getString(R.string.app_name);
            Notification.Builder builder = new Notification.Builder(this)
                    .setContentIntent(PendingIntent.getActivity(this.getApplicationContext(), 0, nfIntent, 0))
                    .setSmallIcon(R.drawable.icon_tool_lock)
                    .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                    .setContentTitle(appName)
                    //.addAction(stopAction)
                    .setContentText(getResources().getText(R.string.app_description));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        appName,
                        NotificationManager.IMPORTANCE_NONE);
                notificationChannel.enableLights(false);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            }

            startForeground(1, builder.build());

            mHandler = new Handler(this);
            long interval = 800;
            mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            mHandler.postDelayed(new QueryForegroundTask(mUsageStatsManager, interval, mHandler), interval);
        }
    }

    protected void stopQuery() {
        if (mQueryServiceStarted) {
            mQueryServiceStarted = false;
            mUsageStatsManager = null;
            mHandler = null;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopQuery();
        if (mLockController.isShowing()) {
            mLockController.close();
        }

        Log.e("xxxxx", "onDestroy");
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == FOREGROUND_APP_INFO_MESSAGE) {
            dealWith((ForegroundAppInfo) msg.obj);
            return true;
        }

        return false;
    }

    protected void dealWith(ForegroundAppInfo info) {

        Log.e("sdf", info.className + " " + " " + info.packageName);

        Preference preference = Preference.getInstance();
        if (preference.encryptionPatternLock == null ||
                preference.encryptionPatternLock.isEmpty())
            return;

        if (info.packageName.compareTo("com.android.settings") == 0) {
            boolean isShowingLock = mLockController.isShowing();
            if (!isShowingLock) {
                String id = mLockController.getVerifiedIdentifier();
                if (id.isEmpty() || id.compareTo(info.packageName) != 0) {
                    PackageManager pm = getPackageManager();
                    Drawable icon = null;
                    try {
                        icon = pm.getApplicationIcon(info.packageName);
                    } catch (Exception e) {
                        icon = null;
                        e.printStackTrace();
                    }
                    mLockController.showAndCheckPattern(info.packageName, preference.encryptionPatternLock, icon);
                }
            }

        } else if (info.packageName.indexOf("com.android.launcher") == 0 ||
                info.packageName.compareTo("com.android.systemui") == 0 ||
                info.packageName.compareTo("com.google.android.apps.nexuslauncher") == 0) {
            if (mLockController.isShowing()) {
                mLockController.close();
            }
            mLockController.clearVerifiedIdentifier();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class QueryForegroundTask implements Runnable {
        UsageStatsManager mUsageStatsManager = null;
        long interval = 0;
        Handler mCallbackHander = null;

        public QueryForegroundTask(UsageStatsManager usageStatsManager, long inverval, Handler callBackHandler) {
            this.mUsageStatsManager = usageStatsManager;
            this.interval = inverval;
            this.mCallbackHander = callBackHandler;
        }

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(now - (long) (interval * 1.5), now);
            if (usageEvents != null) {
                Log.e("fdfd", "begin.....");
                UsageEvents.Event event = new UsageEvents.Event();
                UsageEvents.Event lastEvent = null;
                while (usageEvents.getNextEvent(event)) {
                    if (event.getEventType() != UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        Log.e("fdfd", "33 " + event.getEventType() + " " + event.getPackageName());
                        continue;
                    }
                    Log.e("fdfd", "begin.2....");

                    if (lastEvent == null || lastEvent.getTimeStamp() < event.getTimeStamp()) {
                        lastEvent = event;
                    } else {
                        Log.e("fdfd", "begin.2222");
                    }
                }

                if (lastEvent != null) {
                    Message msg = Message.obtain(null, FOREGROUND_APP_INFO_MESSAGE, new ForegroundAppInfo(lastEvent.getPackageName(), lastEvent.getClassName()));
                    mCallbackHander.sendMessage(msg);
                }
            }

            if (mQueryServiceStarted) {
                mCallbackHander.postDelayed(this, interval);
            }
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
