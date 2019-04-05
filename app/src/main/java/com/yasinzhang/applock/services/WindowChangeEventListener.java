package com.yasinzhang.applock.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


public class WindowChangeEventListener extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Log.w("applock", event.getPackageName().toString());

        }
    }

    @Override
    public void onInterrupt() {
        Log.w(null, "WindowChangeEventListener.onInterrupt:");
    }

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();

        Log.i(null, "WindowChangeEventListener.onServiceConnected");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.w(null, "WindowChangeEventListener.onDestroy:");
    }

    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
        Log.w(null, "WindowChangeEventListener.onTrimMemory:"+level);

    }
}
