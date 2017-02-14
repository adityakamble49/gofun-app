package com.adityakamble49.ttl.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;

public class TimerManager {

    private static final String TAG = "TimerManager";
    private static TimerManager instance;
    private static Context context;

    public TimerManager(Context givenContext) {
        context = givenContext;
    }

    public static TimerManager getInstance(Context context) {
        if (instance == null) {
            instance = new TimerManager(context);
        }
        return instance;
    }

    public CountDownTimer startCountDownTimer(final Intent intent) {
        CountDownTimer countDownTimer = new CountDownTimer(getTimerMinutes() * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra(Constants.Timer.KEY_COUNTDOWN, millisUntilFinished);
                context.sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                showCountDownNotification();
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }

    private long getTimerMinutes() {
        long timerMinutes = SharedPrefUtils.getLongegerFromPreferences(context, Constants.Timer
                .KEY_TIMER_MINUTES, Constants.Timer.TIMER_MINUTES_DEFAULT);
        return timerMinutes;
    }

    private void showCountDownNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context
                .getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("TTL")
                .setContentText("Time Over")
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotifyMgr = (NotificationManager) context.getApplicationContext()
                .getSystemService(
                        context.getApplicationContext().NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, mBuilder.build());
    }
}
