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

    Intent ct = new Intent(Constants.BroadcastIntents.COUNTDOWN_SR);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        getApplicationContext().sendBroadcast(new Intent(Constants.BroadcastIntents
                .COUNTDOWN_KILL));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCountDownTimer(ct);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startCountDownTimer(final Intent intent) {
        long timeLeftInMillis = TimerManager.getInstance(this).getTimeLeftInMillis();
        CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra(Constants.Timer.KEY_COUNTDOWN, millisUntilFinished);
                getApplicationContext().sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                showCountDownNotification();
                removeDayInTime();
            }
        };

        if(timeLeftInMillis > 1000){
            countDownTimer.start();
            Log.d(TAG, "startCountDownTimer: CountDownStarted");
        }
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
                .setContentText("You Worked Hard. Have Fun")
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
