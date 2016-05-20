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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bruce on 2016/1/1.
 */
public class SchoolsFragment extends BaseFragment
    implements SchoolsView,RecyclerViewClickListener,AdapterView.OnItemClickListener {

    private static final String TAG = SchoolsFragment.class.getSimpleName();

    private List<View> popupViews = new ArrayList<>();
    private String headers[] = {"地区", "类别","筛选"};
    private Activity activity;
    private static final int LOAD_DATA_SUCCESS = 0;
    private static final String TABLE_NAME = "school8";

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

    private String[] citys = {"不限","北京","上海","广东","江苏","浙江","山东","河北","河南","湖南","四川","湖北","福建","黑龙江",
            "辽宁","陕西","安徽","广西","贵州", "山西","云南","重庆","江西","吉林","内蒙","天津","甘肃","新疆","海南",
            "宁夏","青海","西藏"};
    private String[] sexs = {"不限","female","male"};
    private String[] majors = {"不限","财经类","军事类","理工类","林业类","民族类","农林类","商学院","师范类","体育类",
            "医药类","艺术类","语文类","政法类","综合类"};
    private String[] filters = {"不限","985","211","国防生","研究计划","卓越计划"};

    private int currentProvinceId = 0;
    private int currentSexId = 0;
    private int currentMajorId = 0;
    private int currentFilterId = 0;

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
        //load(TABLE_NAME);
        loadDataFromProvince(currentProvinceId,currentSexId,currentMajorId,currentFilterId);
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
        currentPosition = 0;
        loadCount = 0;
        schoolInfos.clear();
        if(parent == cityView) {

            //loadDataFromProvince(position);
            currentProvinceId = position;
            dropDownMenus.setTabText(position == 0 ? headers[0] : citys[position]);
            citysAdapter.setCheckItemPosition(position);
        } else if(parent == sexView) {
            dropDownMenus.setTabText(position == 0 ? headers[1] : sexs[position]);
            sexsAdapter.setCheckItemPosition(position);
            currentSexId = position;
        } else if(parent == majorView) {
            dropDownMenus.setTabText(position == 0 ? headers[1] : majors[position]);
            majorsAdapter.setCheckItemPosition(position);
            currentMajorId = position;
        } else if(parent == filterView) {
            dropDownMenus.setTabText(position == 0 ? headers[2] : filters[position]);
            filterAdapter.setCheckItemPosition(position);
            currentFilterId = position;
        }
        Log.d("SchoolsFragment","currentMajorId="+currentMajorId);
        loadDataFromProvince(currentProvinceId,currentSexId,currentMajorId,currentFilterId);

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
        popupViews.add(majorView);
        popupViews.add(filterView);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(recyclerViewScrollListener);
        schoolsAdapter = new SchoolsAdapter(schoolInfos);
        schoolsAdapter.setRecyclerListListener(this);
        recyclerView.setAdapter(schoolsAdapter);

        if(dropDownMenus != null)
            dropDownMenus.setDropDownMenu(Arrays.asList(headers),popupViews,recyclerView);
    }

//    public  void load(String tableName) {
//        AVQuery<AVObject> query = new AVQuery<>(tableName);
//        if(currentPosition+3 >= schoolInfos.size() && schoolInfos.size()<600) {
//            loadCount++;
//            query.whereGreaterThan("ranking", (loadCount - 1) * 3);
//            query.whereLessThan("ranking", loadCount * 3 + 1);
//            query.orderByAscending("ranking");
//
//            query.findInBackground(new FindCallback<AVObject>() {
//                @Override
//                public void done(List<AVObject> school, AVException e) {
//                    if (e == null) {
//                        schoolInfos.addAll(parseAVObject(school));
//                        if(school.size()>0){
//                            schoolsHandler.sendEmptyMessage(LOAD_DATA_SUCCESS);
//                        }
//                    } else {
//                        Log.d("zhang", "error:" + e.getMessage());
//                    }
//                }
//            });
//
//        }
//
//    }

    private void loadDataFromProvince(int provinceId,int sexId,int majorId,int filterId) {
        AVQuery<AVObject> query = new AVQuery<>("school8");
        if(currentPosition+3 >= schoolInfos.size() && schoolInfos.size()<600) {
            loadCount++;
            Log.d("SchoolsFragment", "schoolInfos.size()=" + schoolInfos.size()+"loadcount="+loadCount);
            if (provinceId != 0) {
                query.whereEqualTo("provinceId", provinceId);
            }
            if (majorId != 0) {
                query.whereEqualTo("college_type", majors[majorId]);
            }
            if (filterId != 0) {
                query.whereEqualTo("tags", filters[filterId]);
            }

            query.whereLessThan("ranking", 762);
            query.orderByAscending("ranking");
            query.setLimit(10);
            query.setSkip((loadCount-1)*10);
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
        List<School> schools = new ArrayList<>();

        String objectId = "";
        String iconUrl ="-";
        String type = "-";
        String belongTo = "-";
        String address = "-";
        String tel = "-";

        int doctorNumber ;
        int masterNumber = 0;
        int importantMajorNumber = 0;

        String description = "-";

        for(int i=0;i<list.size();i++) {
            AVObject school = list.get(i);

            if(school.has("college_icon")) {
                JSONObject college_icon = school.getJSONObject("college_icon");
                try {
                    iconUrl = college_icon.getString("url");
                } catch (JSONException e) {
                    Log.d("SchoolsFragment","JsonException");
                }
            }
            objectId = school.getObjectId();
            type = school.getString("college_type");
            belongTo = school.getString("belong_to");
            address = school.getString("address");
            tel = school.getString("telnum");

            masterNumber = school.getInt("master_num");
            doctorNumber = school.getInt("doctor_num");
            importantMajorNumber = school.getInt("important_major_num");

            description = school.getString("college_description");
            int ranking = school.getInt("ranking");
            if(ranking == 0) {
                ranking = Integer.MAX_VALUE;
            }
            String schoolName = school.getString("college_name");
            Log.d("SchoolsFragment","schoolName="+schoolName+",ranking="+ranking+"address="+address);

            String pictureUrl = null;
            if(school.has("back_cover")) {
                JSONObject back_cover = school.getJSONObject("back_cover");
                try {
                    pictureUrl = back_cover.getString("url");
                } catch (JSONException e) {
                    Log.d("zhang","exception+"+e);
                }
            } else {
                pictureUrl = "no_picture";
            }
            School schoolInfo = new School(ranking, schoolName, pictureUrl);

            schoolInfo.setObjectId(objectId);
            schoolInfo.setIconUrl(iconUrl);
            schoolInfo.setType(type);
            schoolInfo.setAddress(address);
            schoolInfo.setBelongto(belongTo);
            schoolInfo.setTel(tel);

            schoolInfo.setDoctorNumber(doctorNumber);
            schoolInfo.setImportantMajorNumber(importantMajorNumber);
            schoolInfo.setMasterNumber(masterNumber);

            schoolInfo.setDescription(description);

            schools.add(schoolInfo);
        }

        Collections.sort(schools, new Comparator<School>() {
            @Override
            public int compare(School ob1, School ob2) {
                if(ob1.getPictureUrl().equals("no_picture") && ob2.getPictureUrl().equals("no_picture")) {
                    return 0;
                } else if(ob1.getPictureUrl().equals("no_picture")) {
                    return 1;
                } else if(ob2.getPictureUrl().equals("no_picture")) {
                    return -1;
                } else {
                    return ((Integer) ob1.getRanking()).compareTo(ob2.getRanking());
                }
            }
        });

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
        School clickSchool = schoolInfos.get(schoolPosition);
//        clickSchool.setPictureUrl(schoolInfos.get(schoolPosition).getPictureUrl());
//        clickSchool.setSchoolName(schoolInfos.get(schoolPosition).getSchoolName());
        Log.d("SchoolsFragment","clickSchool="+clickSchool+"clickSchool.name="+clickSchool.getSchoolName());
        //Bundle schoolBundle = new Bundle();
        //schoolBundle.putParcelable("school",clickSchool);
        Intent schoolDetailActivityIntent = new Intent (
                getActivity(), SchoolDetailActivity.class);
        //schoolDetailActivityIntent.putExtra("school",clickSchool);
        schoolDetailActivityIntent.putExtra("school",clickSchool);
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
                    Log.d("SchoolsFragment", "totalItem=" + totalItem);
                    Log.d("zhang", "visibleItem=" + visibleItem);
                    if (firstVisibleItem + visibleItem == loadCount*10) {
                        Log.d("zhang","dropmenutext="+dropDownMenus.getTabText());
                        loadDataFromProvince(currentProvinceId,currentSexId,currentMajorId,currentFilterId);
//                        if(dropDownMenus.getTabText().equals(headers[0])) {
//                            load(TABLE_NAME);
//                        }
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
