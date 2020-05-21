package com.example.smart_user.api;


import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class Api {

    private static final String TAG = "URL";
    private static Retrofit retrofit = null;
    public static ApiInterface getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();



        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.69:8081")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        //Creating object for our interface
        Log.e(TAG, "getClient: "+retrofit.baseUrl().toString() );
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api; // return the APIInterface object
    }
}