package com.example.bruce.zhumeng.model.entities;

/**
 * Created by bruce on 2016/4/10.
 */
public class User {
    private String username;
    private String password;
    private String usermail;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }
}
