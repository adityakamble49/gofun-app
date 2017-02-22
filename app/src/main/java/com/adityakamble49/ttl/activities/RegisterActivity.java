package com.adityakamble49.ttl.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.model.User;
import com.adityakamble49.ttl.network.TTLNetwork;
import com.adityakamble49.ttl.network.VolleyCallback;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText mUsernameEditText;
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

        mUsernameEditText = (EditText) findViewById(R.id.v_et_register_username);
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
        String username = mUsernameEditText.getText().toString();
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
        mRegisterProgressDialog.show();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        new RegisterUserTask().execute(user);

    }

    private class RegisterUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... user) {
            String deviceToken = TTLNetwork.getInstance(getApplicationContext()).getDeviceToken();
            Log.d(TAG, "doInBackground: " + deviceToken);
            TTLNetwork.getInstance(getApplicationContext()).registerUser(user[0], deviceToken,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            Log.d(TAG, "onSuccess: " + response);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onError: " + error);
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
