package com.example.bruce.zhumeng;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       // final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("xuexiao");
        final SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("school");
        final SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("major");

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
                                                 public boolean onProfileChanged(View view, IProfile ipProfile,boolean curProfile){
                                                     return false;
                                                 }
                                             }
                ).build();

//        DrawerBuilder drawerBuilder = new DrawerBuilder()
//                .withActivity(this)

        Drawer result = new DrawerBuilder().withActivity(this)
                .addDrawerItems(
                       // item1,
                       // new DividerDrawerItem(),
                        item2,
                        //new SecondaryDrawerItem().withName("major")
                        item3
                )
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (iDrawerItem==item3) {
                            Fragment1 fragment1 = new Fragment1();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.main_layout, fragment1).commit();
                        }
                        if (iDrawerItem==item2) {
                            Fragment2 fragment2 = new Fragment2();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.main_layout,fragment2).commit();
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
