package com.example.bruce.zhumeng.model.biz;

/**
 * Created by bruce on 2016/4/10.
 */
public interface IUserBiz {
    void login(String username,String password,OnLoginListener loginListener);
    void register(String username,String password,String usermail,OnRegisterListener
            registerListener);
}
