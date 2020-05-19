package com.example.smart_user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;



public class MainActivity extends AppCompatActivity {
        private static final String TAG = MainActivity.class.getSimpleName();
        private Button facebookLoginButton, googleLoginButton, customSigninButton, logoutButton;
        private View customSignupButton;
        private TextInputLayout passwordTextInputLayout,emailTextInputLayout;
        private TextInputEditText emailEditText, passwordEditText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bindViews();
            setListeners();


        }

        @Override
        protected void onResume() {
            super.onResume();

            refreshLayout();
        }

        private void refreshLayout() {

            boolean x= false;
            if (x) {
                Log.d(TAG, "Logged in user: " + "");
                facebookLoginButton.setVisibility(View.GONE);
                googleLoginButton.setVisibility(View.GONE);
                customSigninButton.setVisibility(View.GONE);
                customSignupButton.setVisibility(View.GONE);
                emailTextInputLayout.setVisibility(View.GONE);
                passwordTextInputLayout.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
            } else {
                facebookLoginButton.setVisibility(View.VISIBLE);
                googleLoginButton.setVisibility(View.VISIBLE);
                customSigninButton.setVisibility(View.VISIBLE);
                customSignupButton.setVisibility(View.VISIBLE);
                passwordTextInputLayout.setVisibility(View.VISIBLE);
                emailTextInputLayout.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);


            }
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
                    // Perform custom sign in
                    emailTextInputLayout.setError("enter  valid email address");
                    passwordTextInputLayout.setError("enter valid password");

                }
            });

            customSignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform custom sign up
                    Log.d(TAG,"Called signup button");



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
            facebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
            googleLoginButton = (Button) findViewById(R.id.google_login_button);
            customSigninButton = (Button) findViewById(R.id.custom_signin_button);
            customSignupButton = findViewById(R.id.custom_signup_button);
            emailEditText = (TextInputEditText) findViewById(R.id.email_edittext);
            passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password_text_input_layout);
            emailTextInputLayout = (TextInputLayout) findViewById(R.id.email_text_input_layout);
            passwordEditText = (TextInputEditText) findViewById(R.id.password_edittext);
            logoutButton = (Button) findViewById(R.id.logout_button);
        }


    }

