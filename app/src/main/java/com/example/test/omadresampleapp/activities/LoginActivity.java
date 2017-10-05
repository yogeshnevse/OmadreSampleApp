package com.example.test.omadresampleapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.omadresampleapp.R;
import com.example.test.omadresampleapp.services.Services;
import com.example.test.omadresampleapp.services.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText txtUsername;
    private EditText txtPassword;
    private View loginProgressView;
    private View loginScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        loginScrollView = findViewById(R.id.login_scrollview);
        loginProgressView = findViewById(R.id.login_progress);
        Button btnSignIn = (Button) findViewById(R.id.sign_in_button);

        if (btnSignIn != null) btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(txtUsername.getText().toString()) && !TextUtils.isEmpty(txtPassword.getText().toString())) {
                    attemptLogin();

                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid password, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Services services = RestClient.getInstance().getService();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String base = username + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        showProgress(true);
        services.loginNurse(authHeader,
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, NurseListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(LoginActivity.this, "Incorrect username & password", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

