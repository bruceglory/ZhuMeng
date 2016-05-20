package com.example.bruce.zhumeng.presenters;

import android.os.Handler;
import android.os.Looper;

import com.avos.avoscloud.AVUser;
import com.example.bruce.zhumeng.model.biz.IUserBiz;
import com.example.bruce.zhumeng.model.biz.OnLoginListener;
import com.example.bruce.zhumeng.model.biz.OnRegisterListener;
import com.example.bruce.zhumeng.model.biz.UserBiz;
import com.example.bruce.zhumeng.views.IUserLoginView;

/**
 * Created by bruce on 2016/4/10.
 */
public class UserLoginPresenter {

    private IUserBiz userBiz;
    private IUserLoginView userLoginView;
    private Handler handler;

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.handler = new Handler(Looper.getMainLooper());
        this.userBiz = new UserBiz();
        this.userLoginView = userLoginView;
    }

    public void login(String useremail,String password) {
        userBiz.login(useremail,password, new OnLoginListener() {
            @Override
            public void onLoginSuccess(AVUser user) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.jumpMainActivity();
                    }
                });
            }

            @Override
            public void onLoginFailed(final int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.showFailedError(errorCode);
                    }
                });
            }
        });
    }

    public void register(String useremail,String password,String username,int tag) {
        userBiz.register(useremail,password,username,tag,new OnRegisterListener() {
            @Override
            public void registerSuccess(AVUser user) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.jumpMainActivity();
                    }
                });
            }

            @Override
            public void registerFailed(final int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.showFailedError(errorCode);
                    }
                });
            }
        });
    }

}
