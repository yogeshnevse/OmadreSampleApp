package com.example.test.omadresampleapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.omadresampleapp.R;
import com.example.test.omadresampleapp.model.Address;
import com.example.test.omadresampleapp.model.Location;
import com.example.test.omadresampleapp.model.Nurses;
import com.example.test.omadresampleapp.services.Services;
import com.example.test.omadresampleapp.services.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtPassword;
    private EditText txtUsername;
    private View signUpProgressView;
    private View signUpScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtName = (EditText) findViewById(R.id.name);
        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        signUpScrollView = findViewById(R.id.sign_up_scrollview);
        signUpProgressView = findViewById(R.id.sign_up_progress);

        Button signUp = (Button) findViewById(R.id.sign_up_button);
        Button signIn = (Button) findViewById(R.id.sign_in_button);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtName.getText().toString()) &&
                        !TextUtils.isEmpty(txtUsername.getText().toString()) &&
                        !TextUtils.isEmpty(txtPassword.getText().toString())) {
                    signUp();

                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp() {
        //TODO Some values are added manually for now. We will create UI later for these fields
        Services services = RestClient.getInstance().getService();
        List<String> roles = new ArrayList<String>();
        roles.add("admin");

        Location location = new Location();
        location.setLatitude(23.11087);
        location.setLongitude(91.28060);

        Address address = new Address();
        address.setAddress("Sangavi, Pune");
        address.setCity("Pune");
        address.setPincode("411037");
        address.setState("Maharashtra");
        address.setLocation(location);

        Nurses nurses = new Nurses();
        nurses.setName(txtName.getText().toString());
        nurses.setUsername(txtUsername.getText().toString());
        nurses.setPassword(txtPassword.getText().toString());
        nurses.setRoles(roles);
        nurses.setAddress(address);

        showProgress(true);
        services.createNurse(nurses, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                showProgress(false);
                Toast.makeText(SignUpActivity.this, "Nurse Created Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(SignUpActivity.this, "Sign-up error, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows the progress UI and hides the Sign-up form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signUpScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            signUpScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signUpScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            signUpProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            signUpProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signUpProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signUpProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            signUpScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
