package com.example.bruce.zhumeng.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.bruce.zhumeng.R;

/**
 * Created by bruce on 2016/3/28.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int INVALID_EMAIL_ADDRESS = 1;
    private Handler handler;
    private TextView usernameTv;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private TextView registerTv;
    private TextView signTv;
    private Button regAndSignBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        AVOSCloud.initialize(this, "FFS0rxJBqrbQJ44HGKXrB4o0", "bsIgWjlXtj549JfqyWQ3ngdM");
        init();
    }

    private void init() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case INVALID_EMAIL_ADDRESS:
                        Toast.makeText(getApplicationContext(),"invalid email address",Toast
                                .LENGTH_LONG).show();
                        break;
                }
            }
        };
        usernameTv = (TextView) findViewById(R.id.login_username);
        usernameEt = (EditText) findViewById(R.id.username_edit);
        passwordEt = (EditText) findViewById(R.id.password_edit);
        emailEt = (EditText) findViewById(R.id.user_email_edit);
        registerTv = (TextView) findViewById(R.id.register);
        signTv = (TextView) findViewById(R.id.signin);
        regAndSignBt = (Button) findViewById(R.id.signin_button);
        signTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        regAndSignBt.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int what = view.getId();
        switch (what){
            case R.id.register:
                usernameTv.setVisibility(View.VISIBLE);
                usernameEt.setVisibility(View.VISIBLE);
                regAndSignBt.setText(R.string.login_register_link);
                break;
            case R.id.signin:
                usernameTv.setVisibility(View.GONE);
                usernameEt.setVisibility(View.GONE);
                regAndSignBt.setText(R.string.login_label_signin);
                break;
            case R.id.signin_button:
                final AVUser user = new AVUser();
                user.setUsername(usernameEt.getText().toString());
                user.setPassword(passwordEt.getText().toString());
                user.setEmail(emailEt.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d("zhang", "success");
                            Intent userIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("success_user",user);
                            userIntent.putExtras(bundle);
                            LoginActivity.this.setResult(RESULT_OK,userIntent);
                            LoginActivity.this.finish();
                        } else {
                            if (e.getCode() == AVException.INVALID_EMAIL_ADDRESS) {
                                handler.sendEmptyMessage(INVALID_EMAIL_ADDRESS);

                            }
                            Log.d("zhang", "failed" + e.getCause() + e.getCode());
                        }
                    }
                });
            default:
                break;
        }
    }
}
