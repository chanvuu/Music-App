package com.example.musicapp.Model;
public class User {
    private String userId;
    private String userName;
    private String accessToken;
    private String imgURL;

    public User() {
        // Empty constructor required for Firebase
    }

    public User(String userId, String userName, String accessToken, String imgURL) {
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
        this.imgURL = imgURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
