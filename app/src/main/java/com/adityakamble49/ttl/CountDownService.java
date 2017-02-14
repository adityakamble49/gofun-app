package com.adityakamble49.ttl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.adityakamble49.ttl.utils.TimerManager;

public class CountDownService extends Service {
    private CountDownTimer mCountDownTimer;

    public static final String COUNTDOWN_SR = "com.adityakamble49.ttl.countdown_sr";
    Intent ct = new Intent(COUNTDOWN_SR);

    @Override
    public void onCreate() {
        super.onCreate();
        mCountDownTimer = TimerManager.getInstance(getApplicationContext()).startCountDownTimer(ct);
    }

    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
