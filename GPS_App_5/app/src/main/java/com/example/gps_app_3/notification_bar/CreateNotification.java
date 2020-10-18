package com.example.gps_app_3.notification_bar;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.gps_app_3.R;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_START = "start";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_PAUSE_OR_RESUME = "pause_or_resume";

    public static Notification notification;

    public static void createNotification(Context context, boolean pause) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat =
                    new MediaSessionCompat(context, "tag");


            Intent startIntent = new Intent(context, NotificationBroadcast.class)
                    .setAction(CreateNotification.ACTION_START);
            PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context, 0,
                    startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent stopIntent = new Intent(context, NotificationBroadcast.class)
                    .setAction(CreateNotification.ACTION_STOP);
            PendingIntent pendingIntentStop = PendingIntent.getBroadcast(context, 0,
                    stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent pauseOrResumeIntent = new Intent(context, NotificationBroadcast.class)
                    .setAction(CreateNotification.ACTION_PAUSE_OR_RESUME);
            PendingIntent pendingIntentPauseOrResume = PendingIntent.getBroadcast(context, 0,
                    pauseOrResumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            int drwStart = R.drawable.ic_baseline_play_arrow_24;
            int drwStop = R.drawable.ic_baseline_stop_24;
            int drwPauseButton = R.drawable.ic_baseline_pause_24;


            if(!pause) {
                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Location Service")
                        .setOnlyAlertOnce(true)
                        .setShowWhen(false)
                        .addAction(drwStart, "Start", pendingIntentStart)
                        .addAction(drwPauseButton, "Pause", pendingIntentPauseOrResume)
                        .addAction(drwStop, "Stop", pendingIntentStop)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build();
            } else {

                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Location Service")
                        .setOnlyAlertOnce(true)
                        .setShowWhen(false)
                        .addAction(drwStart, "Start", pendingIntentStart)
                        .addAction(drwPauseButton, "Resume", pendingIntentPauseOrResume)
                        .addAction(drwStop, "Stop", pendingIntentStop)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build();
            }

            notificationManagerCompat.notify(1, notification);
        }

    }

}
