package com.example.smart_user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smart_user.api.Api;
import com.example.smart_user.config.Config;
import com.example.smart_user.model.Post;
import com.example.smart_user.model.SignInResponse;
import com.example.smart_user.model.SignUpResponse;
import com.example.smart_user.model.User;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostActivity extends AppCompatActivity {

    private EditText edt_post;
    private Button btn_post;
    private SharedPreferences sharedpreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        edt_post = (EditText) findViewById(R.id.edt_post);
        btn_post = (Button) findViewById(R.id.btn_post);


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validatePost(edt_post)){
                    try {
                        CreatingPost();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{

                }
            }
        });
    }



   private Boolean validatePost(EditText editText){

        if(editText.getText().toString().trim().length()>0){
            return true ;
        }else
            return false;

    }


    private void finishActivity(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }





    private void CreatingPost() throws ParseException {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(Config.pleaseWait); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data

         String s =   sharedpreferences.getString(Config.USER_ID ,"0") ;

        Post post = new Post(Integer.parseInt(s) ,edt_post.getText().toString().trim(),Util.getCurrentDate() , Util.getCurrentDate() );

        Call<SignUpResponse> postResponseCall = Api.getPostServiceClient().createPost(post);

        postResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {


                Log.e("response -", response.toString());
                Log.e("response -", call.request().body().toString());
                progressDialog.dismiss();


                if (response.code() == 200 || response.code() == 202 || response.isSuccessful()) {
                    Toast.makeText(PostActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    finishActivity();
                } else {
                    Toast.makeText(PostActivity.this, Config.PleaseTryLater, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });


    }
}
