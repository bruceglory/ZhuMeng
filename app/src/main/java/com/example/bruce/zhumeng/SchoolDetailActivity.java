package com.example.bruce.zhumeng;

import android.animation.Animator;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.example.bruce.zhumeng.fragment.SchoolsFragment;
import com.example.bruce.zhumeng.utils.GUIUtils;
import com.example.bruce.zhumeng.utils.TransitionUtils;
import com.example.bruce.zhumeng.views.custom_listeners.AnimatorAdapter;
import com.example.bruce.zhumeng.views.custom_listeners.TransitionAdapter;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by bruce on 2015/12/16.
 */
public class SchoolDetailActivity extends AppCompatActivity {

    String[] year = new String[]{" ","2013","2014","2015","2016"," "};
    float[] score = new float[]{655,655,634,678,621,621};
    LineSet schoolScoreData = new LineSet(year,score);

    private Toolbar toolbar;
    private ImageView schoolPicture;
    private TextView schoolName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout schoolDetailInfo;
    private LineChartView lineChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info_layout);
        findView();
        initialize();

    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.school_info_toolbar);
        schoolPicture = (ImageView)findViewById(R.id.school_picture);
        schoolName = (TextView) findViewById(R.id.school_name);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        schoolDetailInfo = (LinearLayout) findViewById(R.id.school_detail_info);
        lineChartView = (LineChartView) findViewById(R.id.school_score_line);
    }

    private void initialize() {
        setUpToolbar();
        setUpCollapsingToolbarLayout();
        setUpLineChartView();
        loadBackdrop();
        //initializeStartAnimation();
    }

    private void setUpToolbar(){
        //our toolbar's height has to include the padding of the statusBar so the
        // ColapsingToolbarLayout and the Toolbar can position the arrow/title/... correct
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams)
                toolbar.getLayoutParams();
        lp.height = lp.height + UIUtils.getStatusBarHeight(this);
        toolbar.setLayoutParams(lp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpCollapsingToolbarLayout() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.default_school_name));
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(true).build();

    }

    private void setUpLineChartView(){

        schoolScoreData.setDotsColor(getResources().getColor(R.color.md_red_300));
        schoolScoreData.setThickness(5);
        schoolScoreData.setColor(getResources().getColor(R.color.md_blue_grey_300));
        schoolScoreData.beginAt(1);
        schoolScoreData.endAt(5);
        Paint paint = new Paint();
        lineChartView.addData(schoolScoreData);
        lineChartView.setXAxis(true);
        lineChartView.setYAxis(true);
        lineChartView.setAxisBorderValues(0, 750);
        lineChartView.setAxisColor(getResources().getColor(R.color.md_blue_grey_300));
        lineChartView.setGrid(ChartView.GridType.HORIZONTAL, paint);
        lineChartView.setXLabels(AxisController.LabelPosition.OUTSIDE);
        lineChartView.setYLabels(AxisController.LabelPosition.NONE);
        lineChartView.setLabelsColor(getResources().getColor(R.color.md_blue_grey_300));
        lineChartView.show();
    }

    private void initializeStartAnimation() {
        configureEnterTransition ();
    }

    private void configureEnterTransition() {
        Log.d("zhang", "enter transition");
        getWindow().setSharedElementEnterTransition(
                TransitionUtils.makeSharedElementEnterTransition(this));

        postponeEnterTransition();

        int moviePosition = getIntent().getIntExtra(
                SchoolsFragment.EXTRA_MOVIE_POSITION, 0);

        schoolPicture.setTransitionName(SchoolsFragment.SHARED_ELEMENT_COVER + moviePosition);
        collapsingToolbarLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {

                        collapsingToolbarLayout.getViewTreeObserver()
                                .removeOnPreDrawListener(this);

                        startPostponedEnterTransition();
                        return true;
                    }
                }
        );

        getWindow().getSharedElementEnterTransition().addListener(
                new TransitionAdapter() {

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        super.onTransitionEnd(transition);
                        animateElementsByScale();
                    }
                }
        );
    }

    private void animateElementsByScale() {

        GUIUtils.showViewByScaleY(schoolName, new AnimatorAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                GUIUtils.showViewByScale(schoolDetailInfo);
            }
        });
    }

    private void loadBackdrop() {

        Picasso.with(this).
                load("http://www.ruc.edu.cn/wp-content/themes/rucweb/images/ruc_bg_178.jpg").into(schoolPicture);
    }

    private void fillFab() {
        //final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        //fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
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
