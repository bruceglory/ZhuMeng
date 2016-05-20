package com.example.bruce.zhumeng.model.biz;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.bruce.zhumeng.views.LoginActivity;

/**
 * Created by bruce on 2016/4/10.
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(String userEmail, String password, final OnLoginListener loginListener) {

        AVUser.logInInBackground(userEmail, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException loginException) {
                if (loginException == null) {
                    Log.d("Login", "login success");
                    loginListener.onLoginSuccess(avUser);
                } else {
                    int errorCode = loginException.getCode();
                    Log.d("Login", "login failed" + " " + loginException.getCode() + loginException
                            .getCause());
                    loginListener.onLoginFailed(errorCode);
                }
            }
        });

    }

    @Override
    public void register(String emailOrPhone, String password,String username,int Tag,final OnRegisterListener registerListener) {
        final AVUser user = new AVUser();
        if(Tag == LoginActivity.EMAIL_REGISTER) {
            user.setEmail(emailOrPhone);
        } else if(Tag == LoginActivity.PHONE_REGISTER) {
            user.setMobilePhoneNumber(emailOrPhone);
        }
        user.setPassword(password);
        user.setUsername(username);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException registerException) {
                if(registerException == null) {
                    Log.d("Register","register succ");
                    registerListener.registerSuccess(user);
                } else {
                    int errorCode = registerException.getCode();
                    Log.d("Register", "register failed" + " " + registerException.getCode() + registerException
                            .getCause());
                    registerListener.registerFailed(errorCode);
                }
            }
        });
    }
}
