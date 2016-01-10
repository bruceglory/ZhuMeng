package com.example.bruce.zhumeng.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.SchoolDetailActivity;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.SchoolsAdapter;
import com.example.bruce.zhumeng.entities.School;
import com.example.bruce.zhumeng.mvp.presenters.SchoolsPresenter;
import com.example.bruce.zhumeng.mvp.views.SchoolsView;
import com.example.bruce.zhumeng.utils.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 2016/1/1.
 */
public class SchoolsFragment extends Fragment
    implements SchoolsView,RecyclerViewClickListener {

    private static final int LOAD_DATA_SUCCESS = 0;
    private static final String TABLE_NAME = "school";
    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<Bitmap>(1);

    public final static String EXTRA_MOVIE_ID               = "movie_id";
    public final static String EXTRA_MOVIE_LOCATION         = "view_location";
    public final static String SHARED_ELEMENT_COVER         = "cover";
    public final static String EXTRA_MOVIE_POSITION         = "movie_position";
    private int currentPosition = 0;
    private int loadCount = 0;
    private List<School> schoolInfos;
    private Handler schoolsHandler;
    private SchoolsPresenter schoolsPresenter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SchoolsAdapter schoolsAdapter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ll_school_mainview,container,false);

        findView(rootView);
        initialize();
        load(TABLE_NAME);
        return rootView;
    }

    private void findView(View rootView){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        progressBar  = (ProgressBar) rootView.findViewById(R.id.activity_schools_progress);
    }

    private void initialize(){
        initilizeData();
        initializeRecycler();
    }

    private void initilizeData() {
        schoolsHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == LOAD_DATA_SUCCESS) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
        schoolInfos = new ArrayList<>();
        schoolsPresenter = new SchoolsPresenter();
        schoolsPresenter.attachView(this);
    }

    private void initializeRecycler() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setOnScrollListener(recyclerViewScrollListener);
        schoolsAdapter = new SchoolsAdapter(schoolInfos);
        schoolsAdapter.setRecyclerListListener(this);
        recyclerView.setAdapter(schoolsAdapter);
    }

    public  void load(String tableName) {
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        if(currentPosition+3 >= schoolInfos.size() && schoolInfos.size()<10) {
            loadCount++;
            query.whereGreaterThan("schoolId", (loadCount - 1) * 3);
            query.whereLessThan("schoolId", loadCount * 3 + 1);
            query.orderByAscending("schoolId");

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

            int id = list.get(i).getInt("schoolId");
            String schoolName = list.get(i).getString("SchoolName");
            Log.d("zhang","schoolName="+schoolName);
            String pictureUrl = list.get(i).getAVFile("photo").getUrl();
            boolean _985 = list.get(i).getBoolean("985");
            boolean _211 = list.get(i).getBoolean("211");

            School schoolInfo = new School(id, schoolName, pictureUrl, _985, _211);
            schools.add(schoolInfo);
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

//        int movieID = schoolsAdapter.getMovieList().get(schoolPosition).getId();
//        schoolDetailActivityIntent.putExtra(EXTRA_MOVIE_ID, movieID);
//        schoolDetailActivityIntent.putExtra(EXTRA_MOVIE_POSITION, schoolPosition);
//
//        ImageView schoolPicture = (ImageView) touchedView.findViewById(R.id.school_picture);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) schoolPicture.getDrawable();
//
//        if (schoolsAdapter.isSchoolReady(schoolPosition) || bitmapDrawable != null) {
//
//            sPhotoCache.put(0, bitmapDrawable.getBitmap());
//
//
//                startDetailActivityBySharedElements(touchedView, schoolPosition,
//                        schoolDetailActivityIntent);
//
//
//        } else {
//
//            Toast.makeText(getActivity(), getString(R.string.activity_movies_message_loading_film),
//                    Toast.LENGTH_SHORT).show();
//        }
        getActivity().startActivity(schoolDetailActivityIntent);
    }

    private void startDetailActivityByAnimation(View touchedView,
                                                int touchedX, int touchedY, Intent movieDetailActivityIntent) {

        int[] touchedLocation = {touchedX, touchedY};
        int[] locationAtScreen = new int [2];
        touchedView.getLocationOnScreen(locationAtScreen);

        int finalLocationX = locationAtScreen[0] + touchedLocation[0];
        int finalLocationY = locationAtScreen[1] + touchedLocation[1];

        int [] finalLocation = {finalLocationX, finalLocationY};
        movieDetailActivityIntent.putExtra(EXTRA_MOVIE_LOCATION,
                finalLocation);

        startActivity(movieDetailActivityIntent);
    }

    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startDetailActivityBySharedElements(View touchedView,
                                                     int moviePosition, Intent movieDetailActivityIntent) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), new Pair<>(touchedView, SHARED_ELEMENT_COVER + moviePosition));

        getActivity().startActivity(movieDetailActivityIntent, options.toBundle());
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
}
