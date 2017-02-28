package com.adityakamble49.ttl.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.activities.MainActivity;
import com.adityakamble49.ttl.utils.Constants;
import com.adityakamble49.ttl.utils.SharedPrefUtils;
import com.adityakamble49.ttl.utils.TimerManager;

public class CountDownService extends Service {
    private static final String TAG = "CountDownService";
    private CountDownTimer mCountDownTimer;

    public static final String COUNTDOWN_SR = "com.adityakamble49.ttl.countdown_sr";
    Intent ct = new Intent(COUNTDOWN_SR);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCountDownTimer = startCountDownTimer(ct);
        startForeground(Constants.Timer.KEY_NOTIFICATION_ID, getTimerNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public CountDownTimer startCountDownTimer(final Intent intent) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerManager.getInstance(this)
                .getTimeLeftInMillis(), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra(Constants.Timer.KEY_COUNTDOWN, millisUntilFinished);
                getApplicationContext().sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                showCountDownNotification();
                removeDayInTime();
                stopForeground(true);
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }

    private Notification getTimerNotification() {
        Intent timerIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingTimerIntent = PendingIntent.getActivity(this, 0, timerIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Timer Service")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingTimerIntent);
        Notification timerNotification = mBuilder.build();

        return timerNotification;
    }

    private void showCountDownNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Time Over")
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotifyMgr = (NotificationManager) this.getSystemService(Context
                .NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, mBuilder.build());
    }

    private void removeDayInTime() {
        SharedPrefUtils.putLongInPreferences(CountDownService.this, Constants.Timer.KEY_IN_TIME,
                Constants.Timer.IN_TIME_EMPTY);
        Log.d(TAG, "removeDayInTime: Day InTime Removed");
    }


}
