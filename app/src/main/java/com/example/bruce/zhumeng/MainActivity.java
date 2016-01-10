package com.example.bruce.zhumeng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVOSCloud;
import com.example.bruce.zhumeng.fragment.MajorsFragment;
import com.example.bruce.zhumeng.fragment.PsysFragment;
import com.example.bruce.zhumeng.fragment.SchoolsFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;


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
        toolbar.setTitle("");

        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }
//        toolbar.setTitle("");

    }

    private void setUpDrawer() {
        //Drawer item
        final SecondaryDrawerItem drawerSchool = new SecondaryDrawerItem().
                withName(R.string.drawer_school);
        final SecondaryDrawerItem drawerMajor = new SecondaryDrawerItem().
                withName(R.string.drawer_major);
        final SecondaryDrawerItem drawerScore = new SecondaryDrawerItem().
                withName(R.string.drawer_score);
        final SecondaryDrawerItem drawerPsy = new SecondaryDrawerItem().
                withName(R.string.drawer_psy);

        //create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.image_nav_drawer_account_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("bruceJohn").
                                withEmail("zhangxrzero@gmail.com")
                                .withIcon(getResources().getDrawable(R.drawable.person_image_empty))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                                                 @Override
                                                 public boolean onProfileChanged(View view, IProfile
                                                         ipProfile, boolean curProfile) {
                                                     return false;
                                                 }
                                             }
                ).build();

        Drawer result = new DrawerBuilder().withActivity(this)
                .addDrawerItems(drawerSchool, drawerMajor, drawerScore, drawerPsy)
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
                        if (iDrawerItem == drawerPsy) {

                            Fragment fragment = new PsysFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, fragment).commit();

                        }

                        return false;
                    }
                })
                .withAccountHeader(headerResult)
                .build();
        result.setSelection(1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
