package com.example.smart_user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_user.api.Api;
import com.example.smart_user.config.Config;
import com.example.smart_user.model.SignInResponse;
import com.example.smart_user.model.SignUpResponse;
import com.example.smart_user.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button facebookLoginButton, googleLoginButton, customSigninButton, logoutButton;
    private TextView customSignupButton;
    private TextInputLayout passwordTextInputLayout, emailTextInputLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private boolean signUp = false;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if (!sharedpreferences.getString(Config.TOKEN, "").isEmpty()) {
            Intent intent = new Intent(getBaseContext(), UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        bindViews();
        setListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshLayout();
    }

    private void refreshLayout() {

//        boolean x = false;
//        if (x) {
//            Log.d(TAG, "Logged in user: " + "");
//            facebookLoginButton.setVisibility(View.GONE);
//            googleLoginButton.setVisibility(View.GONE);
//            customSigninButton.setVisibility(View.GONE);
//            customSignupButton.setVisibility(View.GONE);
//            emailTextInputLayout.setVisibility(View.GONE);
//            passwordTextInputLayout.setVisibility(View.GONE);
//            logoutButton.setVisibility(View.VISIBLE);
//
//            signUp = false;
//            customSigninButton.setText(Config.signIN);
//            customSignupButton.setText(Config.signUpText);
//        } else {
        facebookLoginButton.setVisibility(View.VISIBLE);
        googleLoginButton.setVisibility(View.VISIBLE);
        customSigninButton.setVisibility(View.VISIBLE);
        customSignupButton.setVisibility(View.VISIBLE);
        passwordTextInputLayout.setVisibility(View.VISIBLE);
        emailTextInputLayout.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);

        signUp = false;
        customSigninButton.setText(Config.signIN);
        customSignupButton.setText(Config.signUpText);

//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setListeners() {
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Google login

            }
        });

        customSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditText.setError(null);
                emailEditText.setError(null);

                if (isEmailValid(emailEditText.getText().toString().trim()) && isValidPassword(passwordEditText.getText().toString().trim()))
                    if (signUp) {
                        signUp();
                    } else {
                        signIn();
                    }

            }
        });

        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign up
                Log.d(TAG, "Called signup button");
                signUp = !signUp;

                if (signUp) {
                    customSigninButton.setText(Config.signUP);
                    customSignupButton.setText(Config.signInText);
                } else {
                    customSigninButton.setText(Config.signIN);
                    customSignupButton.setText(Config.signUpText);
                }


            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout();
                Toast.makeText(MainActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void bindViews() {
        facebookLoginButton = findViewById(R.id.facebook_login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        customSigninButton = findViewById(R.id.custom_signin_button);
        customSignupButton = findViewById(R.id.custom_signup_button);
        emailEditText = findViewById(R.id.email_edittext);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        passwordEditText = findViewById(R.id.password_edittext);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void signUp() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(Config.pleaseWait); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data
        Call<SignUpResponse> signUpResponseCall = Api.getClient().signUp(
                new User(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim()));

        signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {


                Log.e("response -", response.toString());
                Log.e("response -", call.request().method());
                progressDialog.dismiss();

                if (response.code() == 200 || response.code() == 202 || response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    passwordEditText.setError(null);
                    emailEditText.setError(null);
                } else {

//                    if (response.code() == 400) {
                    Toast.makeText(MainActivity.this, Config.validEmailOrPassword + response.raw().message(), Toast.LENGTH_SHORT).show();
                }

//                else if (response.code() == 500) {
//                    Toast.makeText(MainActivity.this, "User Already exist by this emailId", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.e("error", t.getMessage());
                Log.e("error -", call.request().body().toString());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void signIn() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(Config.pleaseWait); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data
        Call<SignInResponse> signInResponseCall = Api.getClient().signIn(
                new User(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim()));

        signInResponseCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {


                Log.e("response -", response.toString());
                Log.e("response -", call.request().method());
                progressDialog.dismiss();


                if (response.code() == 200 || response.code() == 202 || response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();

                    editor.putString(Config.TOKEN, response.body().getToken());
                    editor.putString(Config.USER_ID, response.body().getUserId());
                    editor.commit();
                    passwordEditText.setError(null);
                    emailEditText.setError(null);

                    Intent intent = new Intent(getBaseContext(), UserProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, Config.Unauthorized, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });


    }

    boolean isEmailValid(CharSequence email) {

        Boolean valid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (valid) {
            return true;
        } else {
            emailEditText.setError(Config.validEmail);
            return false;
        }

    }

    boolean isValidPassword(CharSequence password) {

        if (password.length() >= 8 && password.length() <= 12) {
            return true;
        } else {
            passwordEditText.setError(Config.validPassword);
            return false;
        }

    }
}