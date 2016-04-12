package com.example.bruce.zhumeng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.example.bruce.zhumeng.fragment.BaseFragment;
import com.example.bruce.zhumeng.fragment.FragmentFactory;
import com.example.bruce.zhumeng.fragment.MajorsFragment;
import com.example.bruce.zhumeng.fragment.PsysFragment;
import com.example.bruce.zhumeng.fragment.SchoolsFragment;
import com.example.bruce.zhumeng.fragment.ScoreLinesFragment;
import com.example.bruce.zhumeng.views.LoginActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final int SCHOOL_FM = 1; //schoolFragment
    private final int MAJOR_FM  = 2; //majorFragment
    private final int SCORE_FM  = 3; //scoreFragment
    private final int PSY_FM    = 4; //psyFragment

    private Toolbar       toolbar;
    private AccountHeader headerResult;
    private Drawer        result;
    private LinearLayout  scoreProvinceSelected;
    private SecondaryDrawerItem drawerAccount1 = new SecondaryDrawerItem().
            withName(R.string.drawer_account1).withIdentifier(5);
    private BaseFragment currentFragment;


    SchoolsFragment schoolsFragment;
    MajorsFragment majorsFragment;
    ScoreLinesFragment scoreLinesFragment;
    PsysFragment psysFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "FFS0rxJBqrbQJ44HGKXrB4o0", "bsIgWjlXtj549JfqyWQ3ngdM");

        if (savedInstanceState != null) {
            FragmentManager fm = getSupportFragmentManager();
            schoolsFragment = (SchoolsFragment)fm.findFragmentByTag
                    ("school");
            majorsFragment = (MajorsFragment)fm.findFragmentByTag
                    ("major");
            scoreLinesFragment = (ScoreLinesFragment)fm.findFragmentByTag
                    ("scoreLine");
            psysFragment = (PsysFragment)fm.findFragmentByTag("psy");
            FragmentTransaction transaction = fm.beginTransaction();
            List<Fragment> list = fm.getFragments();
            if(list == null) {
                Log.d("bruce","list == null");
            } else {
                for(Fragment f : list) {
                    Log.d("bruce","f="+f);
                }
            }

            if(null != schoolsFragment){
                transaction.show(schoolsFragment);
            }
            if(null != majorsFragment) {
                transaction.hide(majorsFragment);
            }
            if(null != scoreLinesFragment) {
                transaction.hide(scoreLinesFragment);
            }
            if(null != psysFragment) {
                transaction.hide(psysFragment);
            }

            transaction.commit();
        }
//        else {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            schoolsFragment = (SchoolsFragment)FragmentFactory.createFragment(SCHOOL_FM);
//            transaction.add(R.id.frame_container, schoolsFragment, "school");
//            majorsFragment = (MajorsFragment)FragmentFactory.createFragment(MAJOR_FM);
//            transaction.add(R.id.frame_container, majorsFragment, "major");
//            scoreLinesFragment = (ScoreLinesFragment)FragmentFactory.createFragment(SCORE_FM);
//            transaction.add(R.id.frame_container,scoreLinesFragment,"scoreLine");
//            psysFragment = (PsysFragment)FragmentFactory.createFragment(PSY_FM);
//            transaction.add(R.id.frame_container,psysFragment,"psy");
//            transaction.show(schoolsFragment).hide(majorsFragment).hide(scoreLinesFragment).hide
//                    (psysFragment).commit();
//        }
//        currentFragment = schoolsFragment;
        setUpToolbar();
        setUpDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //to determine if the user is logged in
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
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
            if (headerResult != null) {
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "activity destory");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"activity onsaveInstanceState");
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("school");
        }
        scoreProvinceSelected = (LinearLayout) findViewById(R.id.score_province_select);
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
                .withHeaderBackground(R.drawable.image_nav_drawer_account_background)
                .build();

        result = new DrawerBuilder().withActivity(this)
                .addDrawerItems(drawerSchool, drawerMajor, drawerScore, drawerPsy,
                        new DividerDrawerItem(), drawerAccount1)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {

                        if (iDrawerItem == drawerSchool) {
                            switchFragment(SCHOOL_FM, false,"school");
                            toolbar.setTitle("School");
                        }
                        if (iDrawerItem == drawerMajor) {
                            switchFragment(MAJOR_FM, false,"major");
                            toolbar.setTitle("Major");
                        }
                        if (iDrawerItem == drawerScore) {
                            switchFragment(SCORE_FM, true,"scoreLine");
                            toolbar.setTitle("ScoreLine");
                        }
                        if (iDrawerItem == drawerPsy) {
                            switchFragment(PSY_FM, false,"psy");
                            toolbar.setTitle("Psy");
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
        result.setSelection(1);
    }

    /**
     * use hide() and show() to switch fragment
     *
     * @param position        fragment position
     * @param provinceVisible scoreLinesFragment provinceSelected isVisible
     */
    public void switchFragment(int position, boolean provinceVisible,String tag) {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> list = fm.getFragments();
        if(list != null) {
            for (Fragment f : list) {
                transaction.hide(f);
            }
        }
        switch (position) {
            case SCHOOL_FM:
                if(schoolsFragment == null){
                    schoolsFragment = new SchoolsFragment();
                    transaction.add(R.id.frame_container,schoolsFragment,tag).commit();
                } else {
                    transaction.show(schoolsFragment).commit();
                }
                break;
            case MAJOR_FM:
                if(majorsFragment == null) {
                    majorsFragment = new MajorsFragment();
                    transaction.add(R.id.frame_container,majorsFragment,tag).commit();
                } else {
                    transaction.show(majorsFragment).commit();
                }
                break;
            case SCORE_FM:
                if(scoreLinesFragment == null) {
                    scoreLinesFragment = new ScoreLinesFragment();
                    transaction.add(R.id.frame_container,scoreLinesFragment,tag).commit();

                } else {
                    transaction.show(scoreLinesFragment).commit();
                }
                break;
            case PSY_FM:
                if(psysFragment == null) {
                    psysFragment = new PsysFragment();
                    transaction.add(R.id.frame_container,psysFragment,tag).commit();
                } else {
                    transaction.show(psysFragment).commit();
                }
                break;
        }
//        if (currentFragment != fragment) {
//            if (!fragment.isAdded()) {
//                transaction.hide(currentFragment).add(R.id.frame_container, fragment,tag).commit();
//            } else {
//                transaction.hide(currentFragment).show(fragment).commit();
//            }
//            currentFragment = (BaseFragment)fragment;
//        }
        if (provinceVisible) {
            scoreProvinceSelected.setVisibility(View.VISIBLE);
        } else {
            scoreProvinceSelected.setVisibility(View.GONE);
        }

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
    private void jumpLogin() {
        Intent accountIntent = new Intent(MainActivity.this, LoginActivity
                .class);
        startActivityForResult(accountIntent, 1);
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

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
