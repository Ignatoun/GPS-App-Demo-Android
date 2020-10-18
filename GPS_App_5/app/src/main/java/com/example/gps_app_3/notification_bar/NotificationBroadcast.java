package com.example.gps_app_3.notification_bar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        context.sendBroadcast(new Intent("LOCATION_SERVICE")
            .putExtra("actionname", intent.getAction()));

    }
}
