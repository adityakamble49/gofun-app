package com.adityakamble49.ttl.network;

import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;

import com.adityakamble49.ttl.utils.DateTimeUtil;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "TTL";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("in_time") != null) {
            notificationText = intent.getStringExtra("in_time");
            notificationText = DateTimeUtil.getDateTimeFromCurrentTime(notificationText);
        }

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Get an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context
                .NOTIFICATION_SERVICE);

        // Build the notification and display it
        mNotifyMgr.notify(1, mBuilder.build());
    }
}