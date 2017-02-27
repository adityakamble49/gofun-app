package com.adityakamble49.ttl.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupUI();
    }

    private void setupUI() {

        mUsernameEditText = (EditText) findViewById(R.id.v_et_login_username);
        mPasswordEditText = (EditText) findViewById(R.id.v_et_login_password);
        mLoginButton = (Button) findViewById(R.id.v_bt_login);
        mRegisterButton = (Button) findViewById(R.id.v_bt_login_register);
        mRegisterButton.setOnClickListener(this);
        mLoginProgress = new ProgressDialog(this);
        mLoginProgress.setTitle(R.string.message_logging_in);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_bt_login:
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    return;
                }
                mLoginProgress.show();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                TTLNetwork.getInstance(LoginActivity.this).loginUser(user, deviceToken,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d(TAG, "onSuccess: " + response);
                                JSONObject tokenJson = null;
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
                                LoginActivity.this.startActivity(mainActivityIntent);
                                LoginActivity.this.finish();
                            }

                            @Override
                            public void onError(String error) {
                                Log.d(TAG, "onError: " + error);
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                                        .show();
                                mLoginProgress.dismiss();
                            }
                        });

                break;
            case R.id.v_bt_login_register:
                Intent registerActivityIntent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(registerActivityIntent);
                finish();
                break;
        }
    }
}
