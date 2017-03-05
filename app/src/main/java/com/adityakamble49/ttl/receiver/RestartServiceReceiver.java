package com.adityakamble49.ttl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.adityakamble49.ttl.services.CountDownService;

public class RestartServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Called");
        context.startService(new Intent(context.getApplicationContext(), CountDownService.class));
    }
}
