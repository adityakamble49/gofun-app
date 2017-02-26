package com.adityakamble49.ttl.utils;

import android.content.Context;
import android.widget.Toast;

public class TimerManager {

    private static final String TAG = "TimerManager";
    private static TimerManager instance;
    private static Context context;

    private TimerManager(Context givenContext) {
        context = givenContext;
    }

    public static TimerManager getInstance(Context context) {
        if (instance == null) {
            instance = new TimerManager(context);
        }
        return instance;
    }

    private long getTimerMillis() {
        return SharedPrefUtils.getLongFromPreferences(context, Constants.Timer
                .KEY_TIMER_MILLIS, Constants.Timer.TIMER_MILLIS_DEFAULT);
    }

    /**
     * Get actual In time from Preferences and remove the padding from TimerTime
     *
     * @return timerMillis
     */
    public long getTimeLeftInMillis() {
        long timerMillis = getTimerMillis();
        long inTime = SharedPrefUtils.getLongFromPreferences(context, Constants.Timer
                .KEY_IN_TIME, Constants.Timer.IN_TIME_EMPTY);
        long currentTime = System.currentTimeMillis();
        long padding = currentTime - inTime;
        return timerMillis - padding;
    }

    public float getPercentLeft(long timeLeftInMillis) {
        long timerMillis = getTimerMillis();
        float percentLeft = ((float) timeLeftInMillis / (float) timerMillis) * 100;
        return percentLeft;
    }

    public long getTimeInMillis(){
        long timeInMillis = SharedPrefUtils.getLongFromPreferences(context, Constants.Timer.KEY_IN_TIME,
                Constants.Timer.IN_TIME_EMPTY);
        return timeInMillis;
    }

    public long getTimeOutMillis(){
        long timeInMillis = SharedPrefUtils.getLongFromPreferences(context, Constants.Timer.KEY_IN_TIME,
                Constants.Timer.IN_TIME_EMPTY);
        long timeOutMillis = timeInMillis + getTimerMillis();
        return timeOutMillis;
    }

}
