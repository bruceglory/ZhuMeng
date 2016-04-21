package com.example.bruce.zhumeng;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.example.bruce.zhumeng.entities.School;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

/**
 * Created by bruce on 2015/12/16.
 */
public class SchoolDetailActivity extends AppCompatActivity {

    String[] year            = new String[]{" ", "2013", "2014", "2015", "2016", " "};
    float[]  score           = new float[]{655, 655, 634, 678, 621, 621};
    LineSet  schoolScoreData = new LineSet(year, score);

    private Toolbar   toolbar;
    private ImageView schoolPicture;
    private ImageView schoolIcon;
    private TextView  schoolProvince;
    private TextView  schoolName;
    private TextView  schoolBelongTo;
    private TextView  schoolAddress;
    private TextView  schoolTel;

    private TextView academicianNumber;
    private TextView masterNumber;
    private TextView doctorNumber;
    private TextView importMajorNum;

    private TextView description;
    private TextView descriptionHint;
    private TextView description2;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout            schoolDetailInfo;
    private LineChartView           lineChartView;

    private School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info_layout);
        school = (School) getIntent().getSerializableExtra("school");
//        Bundle schoolBundle = this.getIntent().getExtras();
//        school = schoolBundle.getParcelable("school");
        Log.d("SchoolDetailActivity", "school=" + school + "school.name=" + school.getSchoolName() + school
                .getAddress());
        findView();
        initialize();

    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.school_info_toolbar);
        toolbar.setTitle(R.string.drawer_school);
        //基本信息
        schoolPicture = (ImageView) findViewById(R.id.school_picture);
        schoolIcon = (ImageView) findViewById(R.id.school_detail_info_badge);
        schoolProvince = (TextView) findViewById(R.id.school_type);
        schoolBelongTo = (TextView) findViewById(R.id.school_belong);
        schoolAddress = (TextView) findViewById(R.id.school_address);
        schoolTel = (TextView) findViewById(R.id.school_tel);
        schoolName = (TextView) findViewById(R.id.school_detail_info_name);

        //科研力量
        academicianNumber = (TextView) findViewById(R.id.school_academician);
        masterNumber = (TextView) findViewById(R.id.school_master);
        doctorNumber = (TextView) findViewById(R.id.school_doctoral);
        importMajorNum = (TextView) findViewById(R.id.school_emphasis_subject);

        //description
        description = (TextView) findViewById(R.id.college_description1);
        descriptionHint = (TextView) findViewById(R.id.description_hint);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        schoolDetailInfo = (LinearLayout) findViewById(R.id.school_detail_info);
        lineChartView = (LineChartView) findViewById(R.id.school_score_line);
    }

    private void initialize() {
        setUpToolbar();
        setUpCollapsingToolbarLayout();
        setUpLineChartView();
        loadBackdrop();
        setIntroduce();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpCollapsingToolbarLayout() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(school.getSchoolName());
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(true).build();

    }

    private void setUpLineChartView() {

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

    private void loadBackdrop() {
        String picUrl = school.getPictureUrl();
        if (picUrl.equals("no_picture")) {
            schoolPicture.setImageResource(R.drawable.img_defaultpicture_school);
        } else {
            Picasso.with(this).load(picUrl).into(schoolPicture);
        }
    }

    private void setIntroduce() {
        Picasso.with(this).load(school.getIconUrl()).into(schoolIcon);
        schoolName.setText(school.getSchoolName());
        schoolProvince.setText(school.getType());
        schoolBelongTo.setText(school.getBelongto());
        schoolAddress.setText(school.getAddress());
        schoolTel.setText(school.getTel());

        academicianNumber.setText("-");
        masterNumber.setText(String.valueOf(school.getMasterNumber()));
        doctorNumber.setText(String.valueOf(school.getDoctorNumber()));
        importMajorNum.setText(String.valueOf(school.getImportantMajorNumber()));

        description.setText(school.getDescription());
//        ViewTreeObserver vto = description.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.d("SchoolDetailActivity","onGlobalLayout");
//
//                int lines = description.getLineCount();
//                Log.d("SchoolDetailActivity","lines="+lines);
//                if(lines > 30) {
//                    description.setMaxLines(30);
//                    descriptionHint.setText("查看全部");
//                    descriptionHint.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
//                            ContextCompat.getDrawable(SchoolDetailActivity.this, R.mipmap
//                                    .drop_down_unselected_icon), null);
//                }
//
//            }
//        });

        descriptionHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionHint.getText().equals("查看更多")) {
                    description.setSingleLine(false);
                    descriptionHint.setText("收起");
                    descriptionHint.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(SchoolDetailActivity.this, R.mipmap.drop_down_selected_icon),
                            null);
                } else {
                    description.setMaxLines(12);
                    descriptionHint.setText("查看更多");
                    descriptionHint.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(SchoolDetailActivity.this, R.mipmap.drop_down_unselected_icon),
                            null);
                    description.setMaxLines(12);
                }
            }
        });

    }

    private void fillFab() {
        //final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        //fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color
        // .WHITE));
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
