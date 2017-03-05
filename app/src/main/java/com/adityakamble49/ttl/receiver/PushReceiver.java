package com.adityakamble49.ttl.receiver;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.activities.MainActivity;
import com.adityakamble49.ttl.services.CountDownService;
import com.adityakamble49.ttl.utils.Constants;
import com.adityakamble49.ttl.utils.DateTimeUtil;
import com.adityakamble49.ttl.utils.SharedPrefUtils;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";

    private boolean firstEntry = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = context.getString(R.string.app_name);
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra(Constants.Timer.KEY_IN_TIME) != null) {
            notificationText = intent.getStringExtra(Constants.Timer.KEY_IN_TIME);

            // Check if in time is there for day already
            if (SharedPrefUtils.getLongFromPreferences(context, Constants.Timer.KEY_IN_TIME,
                    Constants.Timer.IN_TIME_EMPTY) == Constants.Timer.IN_TIME_EMPTY) {
                SharedPrefUtils.putLongInPreferences(context, Constants.Timer.KEY_IN_TIME, Long
                        .valueOf(notificationText));
                firstEntry = true;
            }
            notificationText = DateTimeUtil.getDateTimeFromCurrentTime(notificationText);
        }

        if (firstEntry) {
            // Prepare a notification with vibration, sound and lights
            Intent timerIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingTimerIntent = PendingIntent.getActivity(context, 0, timerIntent, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_timer)
                    .setContentTitle(notificationTitle)
                    .setContentText("Time : " + notificationText)
                    .setLights(Color.RED, 1000, 1000)
                    .setVibrate(new long[]{0, 400, 250, 400})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingTimerIntent);

            // Get an instance of the NotificationManager service
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context
                    .NOTIFICATION_SERVICE);

            // Build the notification and display it
            mNotifyMgr.notify(1, mBuilder.build());

            context.startService(new Intent(context, CountDownService.class));
            firstEntry = false;

            Log.d(TAG, "onReceive: First Time Time In");
        }
    }
}