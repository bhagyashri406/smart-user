package com.example.smart_user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_user.api.Api;
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
    private boolean signUp =false ;
    private String signUpText= "No account yet? Create one";
    private String signInText= "Already Account do SignIn";
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

        boolean x = false;
        if (x) {
            Log.d(TAG, "Logged in user: " + "");
            facebookLoginButton.setVisibility(View.GONE);
            googleLoginButton.setVisibility(View.GONE);
            customSigninButton.setVisibility(View.GONE);
            customSignupButton.setVisibility(View.GONE);
            emailTextInputLayout.setVisibility(View.GONE);
            passwordTextInputLayout.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

             signUp =false ;
            customSigninButton.setText("Sign In");
            customSignupButton.setText(signUpText);
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

                if(signUp){
                signUp();
                }else {
                signIn();
                }

            }
        });

        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign up
                Log.d(TAG, "Called signup button");
                signUp = !signUp ;

                if(signUp) {
                    customSigninButton.setText("Sign Up");
                    customSignupButton.setText(signInText);
                }else{
                    customSigninButton.setText("Sign In");
                    customSignupButton.setText(signUpText);
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
        facebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
        googleLoginButton = (Button) findViewById(R.id.google_login_button);
        customSigninButton = (Button) findViewById(R.id.custom_signin_button);
        customSignupButton =(TextView)findViewById(R.id.custom_signup_button);
        emailEditText = (TextInputEditText) findViewById(R.id.email_edittext);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password_text_input_layout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        passwordEditText = (TextInputEditText) findViewById(R.id.password_edittext);
        logoutButton = (Button) findViewById(R.id.logout_button);
    }

    private void signUp() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
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
                } else if (response.code() == 400) {
                    if (isEmailValid(emailEditText.getText().toString().trim()) && passwordEditText.getText().length() >= 8 && passwordEditText.getText().length() <= 12) {
                        Toast.makeText(MainActivity.this, "Please enter valid Email address or password" + response.raw().message(), Toast.LENGTH_SHORT).show();

                    } else {
                        if (isEmailValid(emailEditText.getText().toString().trim())) {
                            passwordEditText.setError("enter valid password");
                            Toast.makeText(MainActivity.this, "Please enter valid password" + response.raw().message(), Toast.LENGTH_SHORT).show();

                        } else if (passwordEditText.getText().length() >= 8 && passwordEditText.getText().length() <= 12) {
                            emailEditText.setError("enter valid email address");
                            Toast.makeText(MainActivity.this, "Please enter valid Email address" + response.raw().message(), Toast.LENGTH_SHORT).show();


                        } else {
                            emailEditText.setError("enter valid email address");
                            passwordEditText.setError("enter valid password");

                            Toast.makeText(MainActivity.this, "Please enter valid Email address or password" + response.raw().message(), Toast.LENGTH_SHORT).show();

                        }


                    }

                } else if (response.code() == 500) {
                    Toast.makeText(MainActivity.this, "User Already exist by this emailId", Toast.LENGTH_SHORT).show();
                    emailEditText.setError("Enter a different email address");
                    passwordEditText.setError(null);
                }


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
        progressDialog.setMessage("Please Wait"); // set message
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
                    Toast.makeText(MainActivity.this, response.body().token, Toast.LENGTH_SHORT).show();
                    passwordEditText.setError(null);
                    emailEditText.setError(null);
                } else if (response.code() == 401 || response.code() == 400) {
                    if (isEmailValid(emailEditText.getText().toString().trim()) && passwordEditText.getText().length() >= 8 && passwordEditText.getText().length() <= 12) {
                        Toast.makeText(MainActivity.this, "Please enter valid Email address or password" + response.raw().message(), Toast.LENGTH_SHORT).show();

                    } else {
                        if (isEmailValid(emailEditText.getText().toString().trim())) {
                            passwordEditText.setError("enter valid password");
                            Toast.makeText(MainActivity.this,  response.raw().message(), Toast.LENGTH_SHORT).show();

                        } else if (passwordEditText.getText().length() >= 8 && passwordEditText.getText().length() <= 12) {
                            emailEditText.setError("enter valid email address");
                            Toast.makeText(MainActivity.this,  response.raw().message(), Toast.LENGTH_SHORT).show();


                        } else {
                            emailEditText.setError("enter valid email address");
                            passwordEditText.setError("enter valid password");

                            Toast.makeText(MainActivity.this,  response.raw().message(), Toast.LENGTH_SHORT).show();

                        }


                    }

                } else if (response.code() == 500) {
                    Toast.makeText(MainActivity.this, response.raw().message(), Toast.LENGTH_SHORT).show();
                    passwordEditText.setError(null);
                }


            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}