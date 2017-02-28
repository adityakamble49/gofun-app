package com.adityakamble49.ttl.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mRegisterButton;
    private Button mLoginButton;
    private ProgressDialog mRegisterProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEditText = (EditText) findViewById(R.id.v_et_register_email);
        mPasswordEditText = (EditText) findViewById(R.id.v_et_register_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.v_et_register_confirm_password);

        mRegisterButton = (Button) findViewById(R.id.v_bt_register);
        mRegisterButton.setOnClickListener(this);

        mLoginButton = (Button) findViewById(R.id.v_bt_register_login);
        mLoginButton.setOnClickListener(this);

        mRegisterProgressDialog = new ProgressDialog(this);
        mRegisterProgressDialog.setTitle(getString(R.string.registering_user));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_bt_register:
                registerUser();
                break;
            case R.id.v_bt_register_login:
                Intent loginActivityIntent = new Intent(RegisterActivity.this, LoginActivity
                        .class);
                startActivity(loginActivityIntent);
                finish();
                break;
        }
    }

    private void registerUser() {
        String username = mEmailEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String confirmPassword = mConfirmPasswordEditText.getText().toString();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty()) {
            return;
        }
        if (!password.equals(confirmPassword)) {
            mConfirmPasswordEditText.setError(getString(R.string.error_password_match));
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        new RegisterUserTask().execute(user);
    }

    private class RegisterUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected void onPreExecute() {
            mRegisterProgressDialog.show();
        }

        @Override
        protected Void doInBackground(User... user) {
            String deviceToken = TTLNetwork.getInstance(getApplicationContext()).getDeviceToken();
            Log.d(TAG, "doInBackground: " + deviceToken);
            TTLNetwork.getInstance(getApplicationContext()).registerUser(user[0], deviceToken,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            mRegisterProgressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast
                                    .LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: " + response);
                            JSONObject tokenJson = null;
                            String userToken = "";
                            try {
                                tokenJson = new JSONObject(response);
                                userToken = tokenJson.getString(NetworkKeys.KEY_TOKEN);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SharedPrefUtils.putStringInPreferences(RegisterActivity.this,
                                    NetworkKeys
                                    .KEY_TOKEN, userToken);
                            Intent mainActivityIntent = new Intent(RegisterActivity.this,
                                    MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            mRegisterProgressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRegisterProgressDialog.dismiss();
        }
    }
}
