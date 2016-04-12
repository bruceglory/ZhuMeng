package com.example.bruce.zhumeng.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.presenters.UserLoginPresenter;

/**
 * Created by bruce on 2016/3/28.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,IUserLoginView {

    private static final int INVALID_EMAIL_ADDRESS = 1;
    public static final int REGISTER_SUCC_RES = 2;
    public static final int SIGN_SUCC_RES = 3;
    private UserLoginPresenter userLoginPresenter;
    private Handler handler;
    private TextView usernameTv;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private TextView registerTv;
    private TextView signTv;
    private Button signBt;
    private Button registerBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //AVOSCloud.initialize(this, "FFS0rxJBqrbQJ44HGKXrB4o0", "bsIgWjlXtj549JfqyWQ3ngdM");
        init();
    }

    private void init() {
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                int what = msg.what;
//                switch (what) {
//                    case INVALID_EMAIL_ADDRESS:
//                        Toast.makeText(getApplicationContext(),"invalid email address",Toast
//                                .LENGTH_LONG).show();
//                        break;
//                }
//            }
//        };
        userLoginPresenter = new UserLoginPresenter(this);
        usernameTv = (TextView) findViewById(R.id.login_username);
        usernameEt = (EditText) findViewById(R.id.username_edit);
        passwordEt = (EditText) findViewById(R.id.password_edit);
        emailEt = (EditText) findViewById(R.id.user_email_edit);
        registerTv = (TextView) findViewById(R.id.register);
        signTv = (TextView) findViewById(R.id.signin);
        signBt = (Button) findViewById(R.id.signin_button);
        registerBt = (Button) findViewById(R.id.register_button);
        signTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        signBt.setOnClickListener(this);
        registerBt.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int what = view.getId();
        switch (what){
            case R.id.register:
                registerReset();
                break;
            case R.id.signin:
                //userLoginPresenter.login();
                signInReset();
                break;
            case R.id.register_button:
                //register();
                break;
            case R.id.signin_button:
                //signIn();
                userLoginPresenter.login(emailEt.getText().toString(),passwordEt.getText()
                        .toString());
                break;
            default:
                break;
        }
    }
    private void registerReset() {

        if(usernameTv.getVisibility()==View.GONE) {
            usernameTv.setVisibility(View.VISIBLE);
            usernameEt.setVisibility(View.VISIBLE);
            signBt.setVisibility(View.GONE);
            registerBt.setVisibility(View.VISIBLE);
            emailEt.setText(null);
            passwordEt.setText(null);
            usernameEt.setText(null);
        }
    }

    private void signInReset() {
        if(usernameTv.getVisibility()!=View.GONE) {
            usernameTv.setVisibility(View.GONE);
            usernameEt.setVisibility(View.GONE);
            signBt.setVisibility(View.VISIBLE);
            registerBt.setVisibility(View.GONE);
            emailEt.setText(null);
            passwordEt.setText(null);
            usernameEt.setText(null);
        }
    }
//    private void register() {
//        final AVUser user = new AVUser();
//        user.setUsername(usernameEt.getText().toString());
//        user.setPassword(passwordEt.getText().toString());
//        user.setEmail(emailEt.getText().toString());
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    Log.d("zhang", "success");
//                    Toast.makeText(getApplicationContext(),R.string.register_success_mes,
//                            Toast.LENGTH_SHORT);
//                    Intent userIntent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("success_user",user);
//                    userIntent.putExtras(bundle);
//                    LoginActivity.this.setResult(REGISTER_SUCC_RES,userIntent);
//                    LoginActivity.this.finish();
//                } else {
//                    if (e.getCode() == AVException.INVALID_EMAIL_ADDRESS) {
//                        //handler.sendEmptyMessage(INVALID_EMAIL_ADDRESS);
//                    }
//                    Log.d("zhang", "failed" + e.getCause() + e.getCode());
//                }
//            }
//        });
//    }
//
//    private void signIn() {
//        AVUser.logInInBackground(emailEt.getText().toString(), passwordEt.getText().toString(), new
//                LogInCallback<AVUser>() {
//            @Override
//            public void done(AVUser avUser, AVException e) {
//                if(e == null) {
//                    Log.d("zhang","login success");
//                    Toast.makeText(getApplicationContext(), R.string.register_success_mes,
//                            Toast.LENGTH_SHORT);
//                    LoginActivity.this.setResult(SIGN_SUCC_RES);
//                    LoginActivity.this.finish();
//                } else {
//                    Log.d("zhang","login failed "+ "error code " + e.getCode());
//                }
//            }
//        });
//    }


    @Override
    public void jumpMainActivity() {
        this.finish();
    }

    @Override
    public void showFailedError(int errorCode) {
        Log.d("LoginActivity","login failed");
    }
}
