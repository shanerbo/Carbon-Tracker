package com.example.olive.carbon_tracker.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts the service as soon as the phone boots
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent =
                    new Intent("com.example.olive.carbon_tracker.Model.NotifyService");
            context.startService(serviceIntent);
        }
    }
}
