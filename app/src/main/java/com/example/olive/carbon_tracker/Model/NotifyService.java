package com.example.olive.carbon_tracker.Model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Establishes a Service for Notifications
 */

public class NotifyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}