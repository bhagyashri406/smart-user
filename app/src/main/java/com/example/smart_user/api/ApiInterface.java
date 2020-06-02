package com.example.smart_user.api;

import com.example.smart_user.model.Post;
import com.example.smart_user.model.SignInResponse;
import com.example.smart_user.model.SignUpResponse;
import com.example.smart_user.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiInterface {

    // annotation used in POST type requests
    @POST("/sign-up")
    // API's endpoints
    Call<SignUpResponse> signUp(@Body User user);

    // annotation used in POST type requests
    @POST("/authenticate")
    // API's endpoints
    Call<SignInResponse> signIn(@Body User user);


    // annotation used in POST type requests
    @POST("/post")
    // API's endpoints for creating post
    Call<SignUpResponse> createPost(@Body Post post);

    // annotation used in POST type requests
    @GET("/post")
    // API's endpoints for getting all post
    Call<List<Post>> GetAllPost();

}
