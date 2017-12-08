package com.adityakamble49.ttl.services;

import android.util.Log;
import android.widget.Toast;

import com.adityakamble49.ttl.network.NetworkKeys;
import com.adityakamble49.ttl.network.TTLNetwork;
import com.adityakamble49.ttl.network.VolleyCallback;
import com.adityakamble49.ttl.utils.SharedPrefUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInstanceIDSer";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        TTLNetwork.getInstance(this).updateDeviceToken(refreshedToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: Device token updated");
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError: " + error);
            }
        }, SharedPrefUtils.getStringFromPreferences(this, NetworkKeys.KEY_TOKEN, ""));
    }
}
