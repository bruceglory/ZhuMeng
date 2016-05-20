package com.example.bruce.zhumeng;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bruce.zhumeng.entities.Major;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by GKX100127 on 2016/1/27.
 */
public class MajorDetailActivity extends AppCompatActivity {

    private Major major;
    private Toolbar  toolbar;
    private TextView majorCourse;
    private TextView majorOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_info_layout);
        major = (Major) getIntent().getSerializableExtra("major");
        findView();
        initialize();

    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.school_info_toolbar);
        majorCourse = (TextView) findViewById(R.id.major_course);
        majorOverview = (TextView) findViewById(R.id.major_overview);

    }

    private void initialize() {
        setUpToolbar();
        setUpCollapsingToolbarLayout();

        majorCourse.setText(major.getMajorCourse());
        majorOverview.setText(major.getMajorOverview());
        //initializeStartAnimation();
    }

    private void setUpToolbar() {
        //our toolbar's height has to include the padding of the statusBar so the
        // ColapsingToolbarLayout and the Toolbar can position the arrow/title/... correct
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams)
                toolbar.getLayoutParams();
        lp.height = lp.height + UIUtils.getStatusBarHeight(this);
        toolbar.setLayoutParams(lp);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpCollapsingToolbarLayout() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(major.getMajorName());
        new MaterializeBuilder().withActivity(this).withFullscreen(true).build();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
