package com.adityakamble49.ttl.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.services.CountDownService;
import com.adityakamble49.ttl.utils.Constants;
import com.adityakamble49.ttl.utils.DateTimeUtil;
import com.adityakamble49.ttl.utils.SharedPrefUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";

    private boolean firstEntry = false;

    String notificationTitle = "TTL";
    String notificationText = "Test notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get(Constants.Timer.KEY_IN_TIME) != null) {
                notificationText = remoteMessage.getData().get(Constants.Timer.KEY_IN_TIME);

                // Check if in time is there for day already
                if (SharedPrefUtils.getLongFromPreferences(this, Constants.Timer.KEY_IN_TIME,
                        Constants.Timer.IN_TIME_EMPTY) == Constants.Timer.IN_TIME_EMPTY) {
                    SharedPrefUtils.putLongInPreferences(this, Constants.Timer.KEY_IN_TIME, Long
                            .valueOf(notificationText));
                    firstEntry = true;
                }
                notificationText = DateTimeUtil.getDateTimeFromCurrentTime(notificationText);
            }
            if (firstEntry) {
                // Prepare a notification with vibration, sound and lights
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_timer)
                        .setContentTitle(notificationTitle)
                        .setContentText("In Time : " + notificationText)
                        .setLights(Color.RED, 1000, 1000)
                        .setVibrate(new long[]{0, 400, 250, 400})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                // Get an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context
                        .NOTIFICATION_SERVICE);

                // Build the notification and display it
                mNotifyMgr.notify(1, mBuilder.build());

                startService(new Intent(this, CountDownService.class));
                firstEntry = false;
            }
        }
    }
}
