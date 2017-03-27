package com.example.olive.carbon_tracker.Model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Launch a notification once the alarm goes off
 */

public class AlarmReceiver extends BroadcastReceiver {
    private Singleton singleton = Singleton.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = singleton.getNotification();
        notificationManager.notify(1, notification);
//        Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show();
    }
}
