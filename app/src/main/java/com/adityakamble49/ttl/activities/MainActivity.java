package com.adityakamble49.ttl.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.adityakamble49.ttl.CountDownService;
import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.network.NetworkKeys;
import com.adityakamble49.ttl.utils.Constants;
import com.adityakamble49.ttl.utils.DateTimeUtil;
import com.adityakamble49.ttl.utils.SharedPrefUtils;

import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTimeLeftTextView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.Timer.KEY_COUNTDOWN)) {
                String timeLeft = DateTimeUtil.getHrsMinSecFromMillis(intent.getLongExtra(Constants
                        .Timer.KEY_COUNTDOWN, 0));
                mTimeLeftTextView.setText(timeLeft);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isValidUser()) {
            setupUI();
            setupPushy();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(CountDownService.COUNTDOWN_SR));
    }

    private boolean isValidUser() {
        String userToken = SharedPrefUtils.getStringFromPreferences(this, NetworkKeys.KEY_TOKEN,
                "");
        if (userToken.isEmpty() || userToken.equals("")) {
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
            finish();
        } else {
            return true;
        }
        return false;
    }

    private void setupUI() {
        mTimeLeftTextView = (TextView) findViewById(R.id.v_tv_time_left);
    }

    private void setupPushy() {
        Pushy.listen(this);
        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPrefUtils.putStringInPreferences(this, NetworkKeys.KEY_TOKEN, "");
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
        }
        return true;
    }
}
