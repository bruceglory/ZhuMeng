package com.example.bruce.zhumeng.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.SchoolDetailActivity;
import com.example.bruce.zhumeng.adapter.ListDropDownMenuAdapter;
import com.example.bruce.zhumeng.adapter.SchoolsAdapter;
import com.example.bruce.zhumeng.entities.School;
import com.example.bruce.zhumeng.mvp.presenters.SchoolsPresenter;
import com.example.bruce.zhumeng.mvp.views.SchoolsView;
import com.example.bruce.zhumeng.utils.RecyclerViewClickListener;
import com.example.bruce.zhumeng.views.custom_view.DropDownMenus;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bruce on 2016/1/1.
 */
public class SchoolsFragment extends BaseFragment
    implements SchoolsView,RecyclerViewClickListener,AdapterView.OnItemClickListener {

    private static final String TAG = SchoolsFragment.class.getSimpleName();

    private List<View> popupViews = new ArrayList<>();
    private String headers[] = {"地区", "学科", "类别","筛选"};
    private Activity activity;
    private static final int LOAD_DATA_SUCCESS = 0;
    private static final String TABLE_NAME = "school8";

    public final static String SHARED_ELEMENT_COVER         = "cover";
    public final static String EXTRA_MOVIE_POSITION         = "movie_position";
    private int currentPosition = 0;
    private int loadCount = 0;
    private List<School> schoolInfos;
    private SchoolsHandler schoolsHandler;
    private SchoolsPresenter schoolsPresenter;

    private ListView cityView;
    private ListView sexView;
    private ListView majorView;
    private ListView filterView;
    ListDropDownMenuAdapter citysAdapter;
    ListDropDownMenuAdapter sexsAdapter;
    ListDropDownMenuAdapter majorsAdapter;
    ListDropDownMenuAdapter filterAdapter;


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SchoolsAdapter schoolsAdapter;

    private ProgressBar   progressBar;
    private DropDownMenus dropDownMenus;

    private String[] citys = {"jiangxi","fujian","liaoning","beijing","shanghai","nanjing","guizhou","xian"};
    private String[] sexs = {"female","male"};
    private String[] majors = {"like","tiyu","wenke","yixue"};
    private String[] filters = {"fjdkgj","etekge","te","tejnd"};

    public static SchoolsFragment newInstance() {
        Log.d("zhang","create schoolFragment");
        return new SchoolsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ll_school_mainview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Log.d("zhang","schoolsFragment afterCreate");
        findView(mRootView);
        initialize();
        load(TABLE_NAME);
    }

    private void findView(View rootView){
        recyclerView = new RecyclerView(activity);
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
       // progressBar  = (ProgressBar) rootView.findViewById(R.id.activity_schools_progress);
        dropDownMenus = (DropDownMenus) rootView.findViewById(R.id.dropDownMenu);
    }

    private void initialize(){
        initializeData();
        initializeRecycler();
    }

    private void initializeData() {
        schoolsHandler = new SchoolsHandler(this);
        schoolInfos = new ArrayList<>();
        schoolsPresenter = new SchoolsPresenter();
        schoolsPresenter.attachView(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == cityView) {
            dropDownMenus.setTabText(citys[position]);
            citysAdapter.setCheckItemPosition(position);
        } else if(parent == majorView) {
            dropDownMenus.setTabText(majors[position]);
            majorsAdapter.setCheckItemPosition(position);
        } else if(parent == sexView) {
            dropDownMenus.setTabText(sexs[position]);
            sexsAdapter.setCheckItemPosition(position);
        } else if(parent == filterView) {
            dropDownMenus.setTabText(filters[position]);
            filterAdapter.setCheckItemPosition(position);
        }

        dropDownMenus.closeMenu();
    }

    private void initializeRecycler() {

        cityView = new ListView(activity);
        citysAdapter = new ListDropDownMenuAdapter(activity, Arrays.asList(citys));
        cityView.setDividerHeight(0);
        cityView.setAdapter(citysAdapter);
        sexView = new ListView(activity);
        sexsAdapter = new ListDropDownMenuAdapter(activity,Arrays.asList(sexs));
        sexView.setDividerHeight(0);
        sexView.setAdapter(sexsAdapter);
        majorView = new ListView(activity);
        majorsAdapter = new ListDropDownMenuAdapter(activity, Arrays.asList(majors));
        majorView.setDividerHeight(0);
        majorView.setAdapter(majorsAdapter);

        filterView = new ListView(activity);
        filterAdapter = new ListDropDownMenuAdapter(activity,Arrays.asList(filters));
        filterView.setDividerHeight(0);
        filterView.setAdapter(filterAdapter);

        cityView.setOnItemClickListener(this);
        sexView.setOnItemClickListener(this);
        majorView.setOnItemClickListener(this);
        filterView.setOnItemClickListener(this);

        popupViews.add(cityView);
        popupViews.add(sexView);
        popupViews.add(majorView);
        popupViews.add(filterView);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(recyclerViewScrollListener);
        schoolsAdapter = new SchoolsAdapter(schoolInfos);
        schoolsAdapter.setRecyclerListListener(this);
        recyclerView.setAdapter(schoolsAdapter);

        TextView contentView = new TextView(activity);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        contentView.setText("fdkjg");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        dropDownMenus.setDropDownMenu(Arrays.asList(headers),popupViews,recyclerView);
    }

    public  void load(String tableName) {
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        if(currentPosition+3 >= schoolInfos.size() && schoolInfos.size()<600) {
            loadCount++;
            query.whereGreaterThan("ranking", (loadCount - 1) * 3);
            query.whereLessThan("ranking", loadCount * 3 + 1);
            query.orderByAscending("ranking");

            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> school, AVException e) {
                    if (e == null) {
                        schoolInfos.addAll(parseAVObject(school));
                        if(school.size()>0){
                            schoolsHandler.sendEmptyMessage(LOAD_DATA_SUCCESS);
                        }
                    } else {
                        Log.d("zhang", "error:" + e.getMessage());
                    }
                }
            });

        }

    }

    public List<School> parseAVObject(List<AVObject> list) {
        Log.d("zhang", "parseAvobject");
        List<School> schools = new ArrayList<School>();
        for(int i=0;i<list.size();i++) {
            AVObject school = list.get(i);
            int ranking = school.getInt("ranking");
            String schoolName = school.getString("college_name");
            Log.d("zhang","schoolName="+schoolName);

            if(school.has("back_cover")) {
                JSONObject back_cover = school.getJSONObject("back_cover");
                try {
                    String pictureUrl = back_cover.getString("url");
                    School schoolInfo = new School(ranking, schoolName, pictureUrl);
                    schools.add(schoolInfo);
                } catch (JSONException e) {
                    Log.d("zhang","exception+"+e);
                }
            }
        }
        return schools;
    }

    @Override
    public void showSchools(List<School> movieList) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoadingLabel() {

    }

    @Override
    public void hideActionLabel() {

    }

    @Override
    public boolean isTheListEmpty() {
        return false;
    }

    @Override
    public void appendSchools(List<School> schoolList) {

    }

    @Override
    public void onClick(View touchedView, int schoolPosition, float x, float y) {
        Log.d("zhang","onClick");
        Intent schoolDetailActivityIntent = new Intent (
                getActivity(), SchoolDetailActivity.class);


        getActivity().startActivity(schoolDetailActivityIntent);
    }

    private RecyclerView.OnScrollListener recyclerViewScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView view, int dx, int dy) {
                    int visibleItem      = linearLayoutManager.getChildCount();
                    int totalItem        = linearLayoutManager.getItemCount();
                    int firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    currentPosition      = firstVisibleItem;
                    Log.d("zhang", "firstVisible=" + firstVisibleItem);
                    Log.d("zhang", "totalItem=" + totalItem);
                    Log.d("zhang", "visibleItem=" + visibleItem);
                    if (firstVisibleItem + visibleItem == totalItem) {
                        load(TABLE_NAME);
                    }
                }
            };

    static class SchoolsHandler extends Handler {
        WeakReference<SchoolsFragment> mSchoolsFragment;

        public SchoolsHandler(SchoolsFragment schoolsFragment) {
            mSchoolsFragment = new WeakReference<>(schoolsFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final SchoolsFragment schoolsFragment = mSchoolsFragment.get();
            if(schoolsFragment != null) {
                if(msg.what == LOAD_DATA_SUCCESS) {
                    schoolsFragment.schoolsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
