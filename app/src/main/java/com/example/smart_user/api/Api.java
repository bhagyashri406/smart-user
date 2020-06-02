package com.example.smart_user.api;


import android.util.Log;

import com.example.smart_user.config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class Api {

    private static final String TAG = "URL";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;
    public static ApiInterface getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        // change your base URL
        retrofit = null;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.userServiceBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        //Creating object for our interface
        Log.e(TAG, "getClient: " + retrofit.baseUrl().toString());
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api; // return the APIInterface object
    }


    public static ApiInterface getPostServiceClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        // change your base URL
        retrofit1 = null;
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(Config.postServiceBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        //Creating object for our interface
        Log.e(TAG, "getClient: " + retrofit1.baseUrl().toString());
        ApiInterface api = retrofit1.create(ApiInterface.class);
        return api; // return the APIInterface object
    }
}