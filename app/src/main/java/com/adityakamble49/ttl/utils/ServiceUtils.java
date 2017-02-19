package com.adityakamble49.ttl.utils;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtils {

    private static ServiceUtils instance = null;
    private static Context context = null;

    private ServiceUtils() {
    }

    public static ServiceUtils getInstance(Context givenContext) {
        if (instance == null) {
            instance = new ServiceUtils();
            context = givenContext;
        }

        return instance;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer
                .MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
