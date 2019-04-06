package com.yasinzhang.applock.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


public class WindowChangeEventListener extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Log.w("appLockservice", event.getPackageName().toString());

        }
    }

    @Override
    public void onInterrupt(){
    }

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();

        Log.w("appLockservice", "WindowChangeEventListener.onServiceConnected");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("appLockservice", "WindowChangeEventListener.onDestroy:");
    }
}
