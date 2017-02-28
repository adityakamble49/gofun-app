package com.adityakamble49.ttl.network;

import android.content.Context;
import android.util.Log;

import com.adityakamble49.ttl.model.User;
import com.adityakamble49.ttl.utils.AppController;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;


public class TTLNetwork {

    /* Log TAG */
    private static final String TAG = "TTLNetwork";

    private static TTLNetwork instance;
    private static Context context;

    public static TTLNetwork getInstance(Context givenContext) {
        if (instance == null) {
            instance = new TTLNetwork();
        }
        context = givenContext;
        return instance;
    }

    public void loginUser(final User user, final String deviceToken, final VolleyCallback
            callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TTLEndpoints
                .URL_USER_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null) {
                            callback.onError(error.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(NetworkKeys.KEY_USERNAME, user.getUsername());
                param.put(NetworkKeys.KEY_PASSWORD, user.getPassword());
                param.put(NetworkKeys.KEY_DEVICE_TOKEN, deviceToken);
                return param;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void registerUser(final User user, final String deviceToken, final VolleyCallback
            callback) {

        Log.d(TAG, "registerUser: " + user);
        Log.d(TAG, "registerUser: " + deviceToken);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TTLEndpoints
                .URL_USER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(NetworkKeys.KEY_USERNAME, user.getUsername());
                param.put(NetworkKeys.KEY_EMAIL, user.getEmail());
                param.put(NetworkKeys.KEY_PASSWORD, user.getPassword());
                param.put(NetworkKeys.KEY_DEVICE_TOKEN, deviceToken);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public String getDeviceToken() {
        try {
            String deviceToken = Pushy.register(context.getApplicationContext());
            Log.d(TAG, "getDeviceToken: " + deviceToken);
            return deviceToken;
        } catch (PushyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
