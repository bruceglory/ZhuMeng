package com.example.bruce.zhumeng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.example.bruce.zhumeng.fragment.MajorsFragment;
import com.example.bruce.zhumeng.fragment.PsysFragment;
import com.example.bruce.zhumeng.fragment.SchoolsFragment;
import com.example.bruce.zhumeng.fragment.ScoreLinesFragment;

import com.example.bruce.zhumeng.views.LoginActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    SecondaryDrawerItem drawerAccount1 = new SecondaryDrawerItem().
            withName(R.string.drawer_account1).withIdentifier(5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "FFS0rxJBqrbQJ44HGKXrB4o0", "bsIgWjlXtj549JfqyWQ3ngdM");

        setUpToolbar();
        setUpDrawer();
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("school");
        }
//        toolbar.setTitle("");

    }

    private void setUpDrawer() {
        //Drawer item
        final SecondaryDrawerItem drawerSchool = new SecondaryDrawerItem().
                withName(R.string.drawer_school).withIcon(GoogleMaterial.Icon.gmd_cloud)
                .withIdentifier(1);
        final SecondaryDrawerItem drawerMajor = new SecondaryDrawerItem().
                withName(R.string.drawer_major).withIcon(GoogleMaterial.Icon.gmd_book)
                .withIdentifier(2);
        final SecondaryDrawerItem drawerScore = new SecondaryDrawerItem().
                withName(R.string.drawer_score).withIcon(GoogleMaterial.Icon.gmd_alarm)
                .withIdentifier(3);
        final SecondaryDrawerItem drawerPsy = new SecondaryDrawerItem().
                withName(R.string.drawer_psy).withIcon(GoogleMaterial.Icon.gmd_adjust)
                .withIdentifier(4);


        //create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ruc_bg_174)
                .build();

        result = new DrawerBuilder().withActivity(this)
                .addDrawerItems(drawerSchool, drawerMajor, drawerScore, drawerPsy,
                        new DividerDrawerItem(),drawerAccount1)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {

                        if (iDrawerItem == drawerSchool) {
                            Log.d("zhang", "i==0");
                            Fragment fragment = new SchoolsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();


                        }
                        if (iDrawerItem == drawerMajor) {
                            Fragment fragment = new MajorsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();

                        }
                        if (iDrawerItem == drawerScore) {
                            Fragment fragment = new ScoreLinesFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();
                        }
                        if (iDrawerItem == drawerPsy) {

                            Fragment fragment = new PsysFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();

                        }
                        if (iDrawerItem == drawerAccount1) {
                            AVUser currentUser = AVUser.getCurrentUser();
                            if (currentUser != null) {
                                confirmBack();
                            } else {
                                jumpLogin();
                            }

                        }

                        return false;
                    }
                })
                .withAccountHeader(headerResult)
                .build();
        //result.setSelection(1);

    }

    private void confirmBack() {
        new AlertDialog.Builder(this).setMessage(R.string.confirm_back_mes)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AVUser.logOut();
                        jumpLogin();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                })
                .show();

    }

    //jump login activity
    private void jumpLogin(){
        Intent accountIntent = new Intent(MainActivity.this,LoginActivity
                .class);
        startActivityForResult(accountIntent,1);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //to determine if the user is logged in
        AVUser currentUser = AVUser.getCurrentUser();
        if(currentUser != null) {
            IProfile newProfile = new ProfileDrawerItem().withName(currentUser.getUsername()).
                    withEmail(currentUser.getEmail()).withIcon(R.drawable.person_image_empty)
                    .withIdentifier(100);
            if (headerResult != null) {
                headerResult.addProfiles(newProfile);
            }
            //show switch account drawer
            drawerAccount1.withName(R.string.drawer_account2).withIcon(GoogleMaterial.Icon.gmd_account_box);
            result.updateItem(drawerAccount1);
        } else {
            if(headerResult != null) {
                if (headerResult.getProfiles() != null) {
                    headerResult.removeProfileByIdentifier(100);
                }
            }
            //show account drawer
            drawerAccount1.withName(R.string.drawer_account1).withIcon(GoogleMaterial.Icon.gmd_alarm_off);
            result.updateItem(drawerAccount1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == LoginActivity.REGISTER_SUCC_RES) {
//            Log.d("zhang", "result ok");
//            result.closeDrawer();
//            AVUser success_user = (AVUser) data.getParcelableExtra("success_user");
//            IProfile newProfile = new ProfileDrawerItem().withName(success_user.getUsername()).
//                    withEmail(success_user.getEmail()).withIcon(R.drawable.person_image_empty)
//                    .withIdentifier(100);
//            if (headerResult != null) {
//                headerResult.addProfiles(newProfile);
//            }
//            drawerAccount1.withName(R.string.drawer_account2);
//            result.updateItem(drawerAccount1);
//            //result.removeItem(5);
//        } else if(resultCode == LoginActivity.SIGN_SUCC_RES) {
//            Log.d("zhang", "login success signsuccres");
//            result.closeDrawer();
//            AVUser currentUser = AVUser.getCurrentUser();
//            if(currentUser != null) {
//                IProfile newProfile = new ProfileDrawerItem().withName(currentUser.getUsername()).
//                        withEmail(currentUser.getEmail()).withIcon(R.drawable.person_image_empty)
//                        .withIdentifier(100);
//                if (headerResult != null) {
//                    headerResult.addProfiles(newProfile);
//                }
//                drawerAccount1.withName(R.string.drawer_account2);
//                result.updateItem(drawerAccount1);
//            }
//
//        }
//    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
