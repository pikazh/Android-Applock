package com.yasinzhang.applock.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.yasinzhang.applock.services.AppStatsRetrieve;

public class BootCompleteReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            Log.e("ssss", "boot completed..............");
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                context.startService(new Intent(context, AppStatsRetrieve.class));
            else
                context.startForegroundService(new Intent(context, AppStatsRetrieve.class));
        }
    }
}


