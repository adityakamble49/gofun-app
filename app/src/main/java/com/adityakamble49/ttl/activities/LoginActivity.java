package com.adityakamble49.ttl.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.model.User;
import com.adityakamble49.ttl.network.NetworkKeys;
import com.adityakamble49.ttl.network.TTLNetwork;
import com.adityakamble49.ttl.network.VolleyCallback;
import com.adityakamble49.ttl.utils.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupUI();
        setupPushy();
    }

    private void setupUI() {

        mEmailEditText = (EditText) findViewById(R.id.v_et_login_email);
        mPasswordEditText = (EditText) findViewById(R.id.v_et_login_password);
        mLoginButton = (Button) findViewById(R.id.v_bt_login);
        mRegisterButton = (Button) findViewById(R.id.v_bt_login_register);
        mRegisterButton.setOnClickListener(this);
        mLoginProgress = new ProgressDialog(this);
        mLoginProgress.setTitle(R.string.message_logging_in);
        mLoginButton.setOnClickListener(this);
    }

    private void setupPushy() {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_bt_login:
                String username = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    return;
                }

                mLoginProgress.show();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                Log.d(TAG, "onClick: " + user);

                new LoginUserTask().execute(user);

                break;
            case R.id.v_bt_login_register:
                Intent registerActivityIntent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(registerActivityIntent);
                finish();
                break;
        }
    }

    private class LoginUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... user) {
            String deviceToken = TTLNetwork.getInstance(getApplicationContext()).getDeviceToken();
            Log.d(TAG, "doInBackground: " + deviceToken);
            TTLNetwork.getInstance(LoginActivity.this).loginUser(user[0], deviceToken,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d(TAG, "onSuccess: " + response);
                            JSONObject tokenJson;
                            String userToken = "";
                            try {
                                tokenJson = new JSONObject(response);
                                userToken = tokenJson.getString(NetworkKeys.KEY_TOKEN);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SharedPrefUtils.putStringInPreferences(LoginActivity.this,
                                    NetworkKeys
                                            .KEY_TOKEN, userToken);
                            Intent mainActivityIntent = new Intent(LoginActivity.this,
                                    MainActivity
                                            .class);
                            mLoginProgress.dismiss();
                            startActivity(mainActivityIntent);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Log.d(TAG, "onError: " + error);
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                                    .show();
                            mLoginProgress.dismiss();
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoginProgress.dismiss();
        }
    }
}
