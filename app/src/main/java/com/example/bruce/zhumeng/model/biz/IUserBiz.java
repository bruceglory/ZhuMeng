package com.example.bruce.zhumeng.model.biz;

/**
 * Created by bruce on 2016/4/10.
 */
public interface IUserBiz {
    void login(String email,String password,OnLoginListener loginListener);
    void register(String email,String password,String username,int tag,OnRegisterListener registerListener);
}
