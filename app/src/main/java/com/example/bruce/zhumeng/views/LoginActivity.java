package com.example.bruce.zhumeng.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.presenters.UserLoginPresenter;
import com.example.bruce.zhumeng.utils.RegexUtils;

/**
 * Created by bruce on 2016/3/28.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,IUserLoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final int EMAIL_REGISTER = 1;
    public static final int PHONE_REGISTER = 2;
    private UserLoginPresenter userLoginPresenter;
    private TextView usernameTv;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private ImageButton forgetPasswdIb;
    private Button signBt;
    private Button registerBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    private void init() {
        TextView registerTv;
        TextView signTv;
        usernameTv = (TextView) findViewById(R.id.login_username);
        usernameEt = (EditText) findViewById(R.id.username_edit);
        userLoginPresenter = new UserLoginPresenter(this);
        passwordEt = (EditText) findViewById(R.id.password_edit);
        emailEt = (EditText) findViewById(R.id.user_email_edit);
        registerTv = (TextView) findViewById(R.id.register);
        signTv = (TextView) findViewById(R.id.signIn);
        signBt = (Button) findViewById(R.id.signin_button);
        registerBt = (Button) findViewById(R.id.register_button);
        forgetPasswdIb = (ImageButton) findViewById(R.id.password_forget);
        signTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        signBt.setOnClickListener(this);
        registerBt.setOnClickListener(this);
        forgetPasswdIb.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int what = view.getId();
        String account = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String username = usernameEt.getText().toString();
        switch (what){
            case R.id.register:
                registerReset();
                break;
            case R.id.signIn:
                signInReset();
                break;
            case R.id.signin_button:
                Log.d(TAG,"signIn click");
                if(checkInput(account,password)) {
                    userLoginPresenter.login(account, password);
                }
                break;
            case R.id.register_button:
                if(checkInput(account,password,username)) {
                    if(RegexUtils.checkEmail(account)) {
                        userLoginPresenter.register(account, password, username, EMAIL_REGISTER);
                    } else if(RegexUtils.checkPhone(account)) {
                        userLoginPresenter.register(account,password,username,PHONE_REGISTER);
                    }
                }
                break;
            case R.id.password_forget:
                Log.d(TAG,"password_clicked");
                resetPassword();
                break;
            default:
                break;
        }
    }

    public boolean checkInput(String account,String password) {
        if(TextUtils.isEmpty(account)) {
            Toast.makeText(this,R.string.login_account_null_error,Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,R.string.login_password_null_error,Toast.LENGTH_SHORT).show();
        } else if(!(RegexUtils.checkEmail(account) || RegexUtils.checkPhone(account))){
            Toast.makeText(this,R.string.login_account_invaild,Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }


    public boolean checkInput(String account,String password,String username) {
        if(TextUtils.isEmpty(account)) {
            Toast.makeText(this,R.string.login_account_null_error,Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,R.string.login_password_null_error,Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(username)) {
            Toast.makeText(this,R.string.login_username_null_error,Toast.LENGTH_SHORT).show();
        } else if(!(RegexUtils.checkEmail(account) || RegexUtils.checkPhone(account))){
            Toast.makeText(this,R.string.login_account_invaild,Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void registerReset() {
        if(usernameTv.getVisibility()==View.GONE) {
            usernameTv.setVisibility(View.VISIBLE);
            usernameEt.setVisibility(View.VISIBLE);
            signBt.setVisibility(View.GONE);
            forgetPasswdIb.setVisibility(View.GONE);
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
            forgetPasswdIb.setVisibility(View.VISIBLE);
            emailEt.setText(null);
            passwordEt.setText(null);
            usernameEt.setText(null);
        }
    }

    private void resetPassword() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.reset_password_dialog,null);
        final EditText emailEt = (EditText) view.findViewById(R.id.email);
        new AlertDialog.Builder(this)
                //.setView(mEditText)
                .setView(view,64,64,64,32)
                .setPositiveButton(R.string.reset_password, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailEt.getText().toString();
                        sendResetEmail(email);
                    }
                }).show();
    }

    private void sendResetEmail(String email) {
        if(!TextUtils.isEmpty(email)) {
            if(RegexUtils.checkEmail(email)) {
                AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d(TAG,"send command success");
                            Toast.makeText(LoginActivity.this,R.string.reset_success,Toast.LENGTH_SHORT).show();
                            // 已发送一份重置密码的指令到用户的邮箱
                        } else {
                            Log.d(TAG,"reset password error");
                            Toast.makeText(LoginActivity.this,R.string.reset_failed,Toast.LENGTH_SHORT).show();
                            // 重置密码出错。
                        }
                    }
                });
            }
        }
    }

    @Override
    public void jumpMainActivity() {
        this.finish();
    }

    @Override
    public void showFailedError(int errorCode) {
        Log.d("LoginActivity","login failed,errorCode="+errorCode);
        switch (errorCode) {
            case AVException.USER_DOESNOT_EXIST:
                Toast.makeText(this,R.string.login_account_or_password_error,Toast.LENGTH_LONG).show();
                break;
            case AVException.USERNAME_MISSING:
                Toast.makeText(this,"username_missing",Toast.LENGTH_LONG).show();
                break;
            case AVException.INTERNAL_SERVER_ERROR:
                Toast.makeText(this,"INTERNAL_SERVER_ERROR",Toast.LENGTH_SHORT).show();
                break;
            case AVException.EMAIL_TAKEN:
                Toast.makeText(this,R.string.email_taken_mes,Toast.LENGTH_SHORT).show();
                break;
            case AVException.USERNAME_PASSWORD_MISMATCH:
                Toast.makeText(this,R.string.login_account_invaild,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
