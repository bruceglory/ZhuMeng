package com.example.bruce.zhumeng;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;


/**
 * Created by zhang on 2016/5/16.
 */
public class UserInformationActivity extends AppCompatActivity {

    private AVUser currentUser = AVUser.getCurrentUser();
    private String avatarUrl = null;
    private String interest;
    private RelativeLayout interestRL;
    private ImageView avatar;
    private TextView interestTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information_mainview);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser.getAVFile("avatar") == null) {
            avatar.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.person_image_empty));
        } else {
            avatarUrl = currentUser.getAVFile("avatar").getUrl();
            Picasso.with(this).load(avatarUrl).into(avatar);
        }

        interest = currentUser.getString("interest");
        if ( !TextUtils.isEmpty(currentUser.getString("interest"))) {
            interestRL.setVisibility(View.VISIBLE);
            interestTv.setText(interest);
        } else {
            interestRL.setVisibility(View.GONE);
        }

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_information_toolbar);
        avatar = (ImageView) findViewById(R.id.avatar);
        interestRL = (RelativeLayout) findViewById(R.id.interest_rl);
        interestTv = (TextView) findViewById(R.id.interest);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView userName = (TextView) findViewById(R.id.user_id);
        userName.setText(currentUser.getUsername());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                Intent editUserInfoIntent = new Intent(UserInformationActivity.this, EditUserInformationActivity.class);
                editUserInfoIntent.putExtra("avatar_url",avatarUrl);
                editUserInfoIntent.putExtra("interest",interest);
                startActivity(editUserInfoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
