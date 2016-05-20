package com.example.bruce.zhumeng;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
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
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final int SCHOOL_FM = 1; //schoolFragment
    private final int MAJOR_FM  = 2; //majorFragment
    private final int SCORE_FM  = 3; //scoreFragment
    private final int PSY_FM    = 4; //psyFragment

    private Toolbar        toolbar;
    private AccountHeader headerResult;
    private Drawer         result;
    private LinearLayout  scoreProvinceSelected;
    private SecondaryDrawerItem drawerAccount1 = new SecondaryDrawerItem().
            withName(R.string.drawer_account1).withIdentifier(5);

    FragmentFactory      fragmentFactory = new FragmentFactory();
    SchoolsFragment    schoolsFragment;
    MajorsFragment     majorsFragment;
    ScoreLinesFragment scoreLinesFragment;
    PsysFragment       psysFragment;

    private AvatarChangeReceiver avatarChangeLocalReceive;
    private IProfile newProfile;
    private int currentFragmentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "FFS0rxJBqrbQJ44HGKXrB4o0", "bsIgWjlXtj549JfqyWQ3ngdM");

        if (savedInstanceState != null) {
            Log.d(TAG,"savedInstanceStace != null");
            FragmentManager fm = getSupportFragmentManager();
            schoolsFragment = (SchoolsFragment) fm.findFragmentByTag("school");
            majorsFragment = (MajorsFragment) fm.findFragmentByTag("major");
            scoreLinesFragment = (ScoreLinesFragment) fm.findFragmentByTag("scoreLine");
            psysFragment = (PsysFragment) fm.findFragmentByTag("psy");
            FragmentTransaction transaction = fm.beginTransaction();
            List<Fragment> list = fm.getFragments();
            if (list == null) {
                Log.d("bruce", "list == null");
            } else {
                for (Fragment f : list) {
                    Log.d("bruce", "f=" + f);
                }
            }

            if (null != schoolsFragment) {
                transaction.show(schoolsFragment);
                currentFragmentPosition = SCHOOL_FM;
            }
            if (null != majorsFragment) {
                transaction.hide(majorsFragment);
            }
            if (null != scoreLinesFragment) {
                transaction.hide(scoreLinesFragment);
            }
            if (null != psysFragment) {
                transaction.hide(psysFragment);
            }

            transaction.commit();
        }
        Log.d(TAG,"schoolFragment="+schoolsFragment);
        setUpToolbar();
        setUpDrawer();
        initDrawerImageLoader();
        initAvatarReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //to determine if the user is logged in
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            if (headerResult != null) {
                if(headerResult.getProfiles() == null || headerResult.getProfiles().size() == 0) {
                    newProfile = new ProfileDrawerItem().withName(currentUser.getUsername())
                            .withEmail(currentUser.getEmail())
                            .withIdentifier(100);
                    if(currentUser.getAVFile("avatar") != null) {
                        String url = currentUser.getAVFile("avatar").getUrl();
                        newProfile.withIcon(url);
                    } else {
                        newProfile.withIcon(R.drawable.person_image_empty);
                    }
                    headerResult.addProfiles(newProfile);
                }
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
        if(result != null) {
            if (currentFragmentPosition == 0) {
                result.setSelection(SCHOOL_FM);
            } else {
                result.setSelection(currentFragmentPosition);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(avatarChangeLocalReceive);
        Log.d(TAG, "activity destory");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "activity onSaveInstanceState");
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("school");
        }
        scoreProvinceSelected = (LinearLayout) findViewById(R.id.score_province_select);
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
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Log.d(TAG,"profile image click");
                        Intent userInformationIntent = new Intent(MainActivity.this,UserInformationActivity.class);
                        startActivity(userInformationIntent);
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();


        result = new DrawerBuilder().withActivity(this)
                .addDrawerItems(drawerSchool, drawerMajor, drawerScore, drawerPsy,
                        new DividerDrawerItem(), drawerAccount1)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {

                        if (iDrawerItem == drawerSchool) {
                            switchFragment(SCHOOL_FM, false, "school");
                            toolbar.setTitle(getResources().getString(R.string.drawer_school));
                        }
                        if (iDrawerItem == drawerMajor) {
                            switchFragment(MAJOR_FM, false, "major");
                            toolbar.setTitle(getResources().getString(R.string.drawer_major));
                        }
                        if (iDrawerItem == drawerScore) {
                            switchFragment(SCORE_FM, true, "scoreLine");
                            toolbar.setTitle(getResources().getString(R.string.drawer_score));
                        }
                        if (iDrawerItem == drawerPsy) {
                            switchFragment(PSY_FM, false, "psy");
                            toolbar.setTitle(getResources().getString(R.string.drawer_psy));
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

    private void initAvatarReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.bruce.zhumeng.AVATAR_CHANGE");
        avatarChangeLocalReceive = new AvatarChangeReceiver();
        registerReceiver(avatarChangeLocalReceive,intentFilter);
    }

    private void initDrawerImageLoader() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });
    }

    /**
     * use hide() and show() to switch fragment
     *
     * @param position        fragment position
     * @param provinceVisible scoreLinesFragment provinceSelected isVisible
     * @param tag             fragment tag
     */
    public void switchFragment(int position, boolean provinceVisible, String tag) {
        currentFragmentPosition = position;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> list = fm.getFragments();
        if (list != null) {
            for (Fragment f : list) {
                transaction.hide(f);
            }
        }
        switch (position) {
            case SCHOOL_FM:
                Log.d(TAG,"fragment position="+position);
                Log.d(TAG,"schoolFragment2 = "+schoolsFragment);
                if (schoolsFragment == null) {
                    schoolsFragment = (SchoolsFragment)fragmentFactory.createFragment(SCHOOL_FM);
                    transaction.add(R.id.frame_container, schoolsFragment, tag).commit();
                } else {
                    transaction.show(schoolsFragment).commit();
                }
                break;
            case MAJOR_FM:
                if (majorsFragment == null) {
                    majorsFragment = (MajorsFragment)fragmentFactory.createFragment(MAJOR_FM);
                    transaction.add(R.id.frame_container, majorsFragment, tag).commit();
                } else {
                    transaction.show(majorsFragment).commit();
                }
                break;
            case SCORE_FM:
                if (scoreLinesFragment == null) {
                    scoreLinesFragment = (ScoreLinesFragment)fragmentFactory.createFragment
                            (SCORE_FM);
                    transaction.add(R.id.frame_container, scoreLinesFragment, tag).commit();

                } else {
                    transaction.show(scoreLinesFragment).commit();
                }
                break;
            case PSY_FM:
                if (psysFragment == null) {
                    psysFragment = (PsysFragment)fragmentFactory.createFragment(PSY_FM);
                    transaction.add(R.id.frame_container, psysFragment, tag).commit();
                } else {
                    transaction.show(psysFragment).commit();
                }
                break;
        }

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
                        headerResult.removeProfileByIdentifier(100);
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
        startActivity(accountIntent);
    }

    @Override
    public void onBackPressed() {
        if(currentFragmentPosition == 4) {
            if(psysFragment.onBackPressed()){
                return;
            }
        }
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    class AvatarChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"onReceiver");
            newProfile.withIcon(AVUser.getCurrentUser().getAVFile("avatar").getUrl());
            headerResult.updateProfile(newProfile);
        }
    }

}
