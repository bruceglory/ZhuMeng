package com.example.bruce.zhumeng.model.biz;

import com.avos.avoscloud.AVUser;

/**
 * Created by bruce on 2016/4/10.
 */
public interface OnLoginListener {
    void onLoginSuccess(AVUser user);
    void onLoginFailed(int errorCode);
}
