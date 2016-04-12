package com.example.bruce.zhumeng;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;
import com.example.bruce.zhumeng.R;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by GKX100127 on 2016/1/27.
 */
public class MajorDetailActivity extends AppCompatActivity {

    private static final int LOAD_INTRO_SUCC = 0;
    private static final int LOAD_DES_SUCC = 1;
    private OkHttpClient client = new OkHttpClient();
    private static Handler majorDetailHandler;
    private String introText;
    private String desText;
    private Toolbar toolbar;
    private ImageView schoolPicture;
    private TextView schoolName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout schoolDetailInfo;
    private TextView majorIntro;
    private TextView majorIntroMore;
    private TextView majorDes;
    private Button loadMore;
    private Button loadLess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_info_layout);
        findView();
        initialize();
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (majorIntro.getMaxLines() <= 4) {
                    majorIntro.setMaxLines(60);
                } else {
                    majorIntro.setMaxLines(4);
                }
            }
        });

        loadLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLess();
            }
        });
    }

    private void findView() {

        toolbar = (Toolbar) findViewById(R.id.school_info_toolbar);
        schoolPicture = (ImageView)findViewById(R.id.school_picture);
        schoolName = (TextView) findViewById(R.id.school_name);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        schoolDetailInfo = (LinearLayout) findViewById(R.id.school_detail_info);
        majorIntro = (TextView) findViewById(R.id.major_introduce);
        majorIntroMore = (TextView) findViewById(R.id.major_introduce_more);
        majorDes = (TextView) findViewById(R.id.major_des);
        loadMore = (Button) findViewById(R.id.load_more);
        loadLess = (Button) findViewById(R.id.load_less);

    }

    private void loadMore() {
        majorIntro.setVisibility(View.GONE);
        majorIntroMore.setVisibility(View.VISIBLE);
        loadMore.setText("less");
        //loadLess.setVisibility(View.VISIBLE);
    }

    private void loadLess() {
        majorIntroMore.setVisibility(View.GONE);
        majorIntro.setVisibility(View.VISIBLE);
        loadMore.setText("More");
    }
    private void initialize() {
        majorDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            if(msg.what == LOAD_INTRO_SUCC) {
                majorIntro.setText(introText);
                majorIntroMore.setText(introText);
            } else if(msg.what == LOAD_DES_SUCC) {
                majorDes.setText(desText);
            }
            }
        };
        setUpToolbar();
        setUpCollapsingToolbarLayout();
        loadIntroData("http://www.diyigaokao.com/major/010101/");
        loadDesData("http://www.gaokaopai.com/zhuanye-jianjie-010101.html");
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

    private void loadIntroData(String url) {
        final StringBuilder introBuild = new StringBuilder();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("zhang", "load Fail");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    introText = response.body().string();
                    Document doc = Jsoup.parse(introText);
                    Elements ctn = doc.getElementsByClass("ctn");
                    if (ctn != null) {
                        Element parent = ctn.first();
                        if(parent != null) {
                            Elements childs = parent.children();
                            for (Element child : childs) {
                                introBuild.append(child.text());
                                if (child != childs.last())
                                    introBuild.append("\n\n");
                            }
                        }
                    }
                    introText = introBuild.toString();
                    majorDetailHandler.sendEmptyMessage(LOAD_INTRO_SUCC);
                }
            }
        });
    }

    private void loadDesData(String url) {
        final StringBuilder desBuild = new StringBuilder();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("zhang","load Fail");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    desText = response.body().string();
                    Document doc = Jsoup.parse(desText);
                    Elements ctn = doc.getElementsByClass("majorCon");
                    if(ctn != null) {
                        Element parent = ctn.first();
                        Elements childs = parent.children();
                        int i =0;
                        for (Element child : childs) {
                            desBuild.append(child.text());
                            i++;
                            if(i%2==0)
                            desBuild.append("\n\n");
                            else
                                desBuild.append("\n");
                        }
                        desText = desBuild.toString();
                    }
                    majorDetailHandler.sendEmptyMessage(LOAD_DES_SUCC);
                }
            }
        });
    }
}
