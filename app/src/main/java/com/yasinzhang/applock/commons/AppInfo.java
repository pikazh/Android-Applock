package com.yasinzhang.applock.commons;

import android.graphics.drawable.Drawable;

public class AppInfo {
    public static final int FLAG_LICENSE_CHECKER = 1;
    public static final int FLAG_PRIVATE_NOTIFICATION = 1<<1;
    public static final int FLAG_SETTINGS = 1<<2;
    public static final int FLAG_SYSTEM_UI = 1<<3;
    public static final int FLAG_ADVANCED = FLAG_LICENSE_CHECKER|FLAG_PRIVATE_NOTIFICATION|FLAG_SETTINGS|FLAG_SYSTEM_UI;

    public static final int FLAG_BLUETOOTH_SWITCH = 1<<4;
    public static final int FLAG_WIFI_SWITCH = 1<<5;
    public static final int FLAG_SWITCHS = FLAG_BLUETOOTH_SWITCH|FLAG_WIFI_SWITCH;

    public static final int FLAG_GENERAL_SYSTEM_APP = 1<<6;

    public String appName = null;
    public String packageName = null;
    public Drawable appIcon = null;
    public boolean isLocked = false;
    public int flag = 0;

    public String getDescription(){

        return null;
    }
}
