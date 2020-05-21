package com.example.smart_user.api;

import com.example.smart_user.model.SignInResponse;
import com.example.smart_user.model.SignUpResponse;
import com.example.smart_user.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {

   // annotation used in POST type requests
    @POST("/sign-up")     // API's endpoints
    Call<SignUpResponse> signUp(@Body User user);

    // annotation used in POST type requests
    @POST("/authenticate")     // API's endpoints
    Call<SignInResponse> signIn(@Body User user);
}
