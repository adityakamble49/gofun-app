package com.adityakamble49.ttl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.network.NetworkKeys;
import com.adityakamble49.ttl.utils.SharedPrefUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser();
    }

    private void authenticateUser() {
        String userToken = SharedPrefUtils.getStringFromPreferences(this, NetworkKeys.KEY_TOKEN,
                "");
        if (userToken.isEmpty() || userToken.equals("")) {
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
            finish();
        } else {
            setupUI();

        }
    }

    private void setupUI() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
