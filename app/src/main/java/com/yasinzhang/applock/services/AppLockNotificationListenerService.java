package com.yasinzhang.applock.services;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class AppLockNotificationListenerService extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerDisconnected(){
        super.onListenerDisconnected();
    }

}
