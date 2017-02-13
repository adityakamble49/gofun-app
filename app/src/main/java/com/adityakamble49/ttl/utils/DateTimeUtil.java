package com.adityakamble49.ttl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static String getDateTimeFromCurrentTime(String currentTimeMillis) {
        long currentTime = Long.parseLong(currentTimeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy hh:mm a", Locale.UK);
        Date resultDate = new Date(currentTime);
        return simpleDateFormat.format(resultDate);
    }
}
