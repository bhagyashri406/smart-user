package com.example.smart_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_user.api.Api;
import com.example.smart_user.config.Config;
import com.example.smart_user.model.Post;
import com.example.smart_user.model.SignUpResponse;
import com.example.smart_user.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Post> rowsArrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    Button btn_whats_on_your_mind;
    int pageNo = 0;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        btn_whats_on_your_mind = (Button) findViewById(R.id.btn_whats_on_your_mind);

        onPost();
        populateData();
        initAdapter();
        initScrollListener();


    }


    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    // rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }


    private void populateData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(Config.pleaseWait); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data
        Call<List<Post>> postCall = Api.getPostServiceClient().GetAllPost();

        postCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                Log.e("response -", response.toString());
                progressDialog.dismiss();

                if (response.code() == 200 || response.code() == 202 || response.isSuccessful()) {
                    rowsArrayList.addAll(response.body());
                    Toast.makeText(UserProfile.this, "" + rowsArrayList.size(), Toast.LENGTH_SHORT).show();
                    initAdapter();
                } else {
                    Toast.makeText(UserProfile.this, Config.PleaseTryLater, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("error", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(UserProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onRefresh() {
//        itemCount = 0;
//        currentPage = PAGE_START;
        boolean isLoading = false;
        rowsArrayList.clear();
        recyclerViewAdapter.Clear();
        recyclerViewAdapter.notifyDataSetChanged();
        populateData();
        initAdapter();
    }

    private void onPost() {

        btn_whats_on_your_mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivityForResult(intent, Config.LAUNCH_POST_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==Config.LAUNCH_POST_ACTIVITY && resultCode== Activity.RESULT_OK){
            this.onRefresh();
        }
    }
}
