package com.example.bruce.zhumeng.model.biz;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by bruce on 2016/4/10.
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(String useremail, String passwrod, final OnLoginListener loginListener) {
        final AVUser user = new AVUser();

        user.logInInBackground(useremail, passwrod, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException loginException) {
                if (loginException == null) {
                    Log.d("Login", "login success");
                    loginListener.onLoginSuccess(user);
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
    public void register(String username, String password, String usermail,final OnRegisterListener
                         registerListener) {

    }
}
