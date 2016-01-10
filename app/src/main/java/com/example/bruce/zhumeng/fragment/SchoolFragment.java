package com.example.bruce.zhumeng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.SchoolsAdapter;
import com.example.bruce.zhumeng.data.SchoolDataAccess;
import com.example.bruce.zhumeng.entities.School;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bruce on 2015/12/30.
 */
public class SchoolFragment extends Fragment {

    private static final int LOAD_DATA_SUCCESS = 0;
    private Handler schoolHandler;
    private List<School> schoolInfos = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int currentPosition = 0;
    private int loadCount = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ll_school_mainview,container,false);
        initData();
        load("school");

        setView(rootView);
        return rootView;
    }

    private void initData() {

        schoolHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == LOAD_DATA_SUCCESS) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }
    private void  setView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        if(adapter == null) {
            adapter = new SchoolsAdapter(schoolInfos);
            Log.d("zhang","schoolinfo size="+schoolInfos.size());
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                int visibleItem = linearLayoutManager.getChildCount();
                int totalItem = linearLayoutManager.getItemCount();
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                currentPosition = firstVisibleItem;
                Log.d("zhang", "firstVisible=" + firstVisibleItem);
                Log.d("zhang", "totalItem=" + totalItem);
                Log.d("zhang", "visibleItem=" + visibleItem);
                if (firstVisibleItem + visibleItem == totalItem) {
                    load("school");
                }

            }
        });
    }

    public void loadData() {
        Log.d("zhang", "schoolFragment load");
        SchoolDataAccess schoolDataAccess = new SchoolDataAccess(this);
        //schoolInfos.addAll(schoolDataAccess.loadData(currentPosition, "school"));

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    public void setSchoolInfos(List<School> schools){
        schoolInfos = schools;
        Log.d("zhang", "schoolInfos.size" + schoolInfos.size());
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

                            parseAVObject(school);
                            if(school.size()>0){
                                schoolHandler.sendEmptyMessage(LOAD_DATA_SUCCESS);
                            }
                        } else {
                            Log.d("zhang", "error:" + e.getMessage());
                        }
                    }
                });

        }


    }

    public void parseAVObject(List<AVObject> list) {
        List<School> schoolInfoList = new ArrayList<>();
        Log.d("zhang","parseAvobject");
        for(int i=0;i<list.size();i++) {

            int id = list.get(i).getInt("schoolId");
            String schoolName = list.get(i).getString("SchoolName");
            Log.d("zhang","schoolName="+schoolName);
            String pictureUrl = list.get(i).getAVFile("photo").getUrl();
            boolean _985 = list.get(i).getBoolean("985");
            boolean _211 = list.get(i).getBoolean("211");

            School schoolInfo = new School(id, schoolName, pictureUrl, _985, _211);
            schoolInfos.add(schoolInfo);
        }
    }

}
