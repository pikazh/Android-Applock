package com.yasinzhang.applock.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yasinzhang.applock.services.AppStatsRetrieve;

public class BootCompleteReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            context.startService(new Intent(context, AppStatsRetrieve.class));
        }
    }
}


