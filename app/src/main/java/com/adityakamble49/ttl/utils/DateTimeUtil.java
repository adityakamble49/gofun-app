package com.adityakamble49.ttl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {
    public static String getDateTimeFromCurrentTime(String currentTimeMillis) {
        long currentTime = Long.parseLong(currentTimeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy hh:mm a", Locale.UK);
        Date resultDate = new Date(currentTime);
        return simpleDateFormat.format(resultDate);
    }

    public static String getHrsMinSecFromMillis(long millis) {
        return String.format(Locale.UK, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit
                        .MILLISECONDS.toHours(millis)), TimeUnit
                        .MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit
                        .MILLISECONDS.toMinutes(millis)));
    }

    public static String getTimeFromMillis(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.Timer.TIME_FORMAT,
                Locale.UK);
        Date date = new Date(millis);
        return simpleDateFormat.format(date);
    }
}
