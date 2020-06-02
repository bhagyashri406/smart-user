package com.example.smart_user.model;

import java.util.Date;

public class Post {

    private int postId;
    private int userId;

    private String postDiscription;

    private String createdTime;

    private String updateTime;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPostDiscription() {
        return postDiscription;
    }

    public void setPostDiscription(String postDiscription) {
        this.postDiscription = postDiscription;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Post() {
        super();

    }

    public Post(int userId, String postDiscription, String createdTime, String updateTime) {
        this.userId = userId;
        this.postDiscription = postDiscription;
        this.createdTime = createdTime;
        this.updateTime = updateTime;

    }
}
