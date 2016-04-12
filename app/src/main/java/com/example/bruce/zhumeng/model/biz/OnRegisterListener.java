package com.example.bruce.zhumeng.model.biz;

import com.avos.avoscloud.AVUser;

/**
 * Created by bruce on 2016/4/10.
 */
public interface OnRegisterListener {
    void registerSuccess(AVUser user);
    void registerFailed(int errorCode);

}
