package com.example.bruce.zhumeng.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.ProvinceAdapter;
import com.example.bruce.zhumeng.adapter.ScoreLineAdapter;
import com.example.bruce.zhumeng.utils.ResourceGetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GKX100127 on 2016/1/13.
 */
public class ScoreLinesFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemClickListener{
    private List<String> years       = new ArrayList<>();
    private List<String> firstScore  = new ArrayList<>();
    private List<String> secondScore = new ArrayList<>();
    private List<String> thirdScore  = new ArrayList<>();
    private List<String> province    = new ArrayList<>();

    private final String []years1 ={" ","2011","2012","2013","2014","2015"};
    private ListView scienceListView;
    private ListView artListView;
    private ListView provinceListView;

    private ProvinceAdapter provinceAdapter;
    private ScoreLineAdapter scienceAdapter;
    private ScoreLineAdapter artAdapter;
    private PopupWindow provincePopupWindow;
    private Toolbar toolbar;
    private LinearLayout scoreProvinceSelected;
    private TextView provinceName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.score_line_mainview,null);
        findView(rootView);
        initialize();
        if(artAdapter == null)
            artAdapter = new ScoreLineAdapter(getActivity(), years, firstScore, secondScore,thirdScore);
        if(scienceAdapter == null)
            scienceAdapter = new ScoreLineAdapter(getActivity(), years, firstScore, secondScore,thirdScore);
        if(provinceAdapter == null)
            provinceAdapter = new ProvinceAdapter(getActivity(), province);
        scienceListView.setAdapter(scienceAdapter);
        artListView.setAdapter(artAdapter);
        return rootView;
    }


    private void findView(View rootView) {
        scienceListView = (ListView)rootView.findViewById(R.id.science_listview);
        artListView = (ListView) rootView.findViewById(R.id.art_human_science_listView);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("scoreLine");
        scoreProvinceSelected = (LinearLayout) toolbar.findViewById(R.id.score_province_select);
        provinceName = (TextView) toolbar.findViewById(R.id.score_province);
        scoreProvinceSelected.setVisibility(View.VISIBLE);
        scoreProvinceSelected.setOnClickListener(this);
    }

    private void initialize() {
        initializeData();

        years.add(" ");
        for(int year = 2011;year < 2016;year++) {
            years.add(String.valueOf(year));
        }
        firstScore.add(getContext().getResources().getString(R.string.first_batch));
        firstScore.add("541");
        firstScore.add("535");
        firstScore.add("555");
        firstScore.add("589");
        firstScore.add("576");

        secondScore.add(getContext().getResources().getString(R.string.second_batch));
        secondScore.add("678");
        secondScore.add("643");
        secondScore.add("598");
        secondScore.add("580");
        secondScore.add("610");

        thirdScore.add(getContext().getResources().getString(R.string.third_batch));
        thirdScore.add("353");
        thirdScore.add("342");
        thirdScore.add("535");
        thirdScore.add("353");
        thirdScore.add("289");

    }

    private void initializeData() {
        loadProvinceData();
    }
    private void loadProvinceData() {
        AVQuery<AVObject> provinceQuery = new AVQuery<>("Province");
        provinceQuery.whereGreaterThan("provinceId",0);
        provinceQuery.whereLessThan("provinceId", 32);
        provinceQuery.orderByAscending("provinceId");
        provinceQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> provinceList, AVException e) {
                if(e ==null) {
                    province.clear();
                    for(AVObject pro : provinceList){
                        province.add(pro.getString("provinceName"));
                    }
                } else {
                    Log.d("zhang","query fail");
                }
            }
        });
    }
    private void showPopupWindow() {
        if(provincePopupWindow == null) {

            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.simple_popupwindow_view, null);
            provinceListView = (ListView) view.findViewById(R.id.popupwindow_listView);
            provinceListView.setAdapter(provinceAdapter);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    provincePopupWindow.dismiss();
                }
            });
            view.startAnimation(AnimationUtils.
                    loadAnimation(getContext().getApplicationContext(), R.anim.fade_in));


            provincePopupWindow = new PopupWindow(getActivity());
            provincePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            provincePopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            ColorDrawable cd = new ColorDrawable(0x7dc0c0c0);
            provincePopupWindow.setBackgroundDrawable(cd);

            provincePopupWindow.setOutsideTouchable(true);
            provincePopupWindow.setFocusable(true);
            provincePopupWindow.setContentView(view);
            provinceListView.setOnItemClickListener(this);
        }
        provincePopupWindow.showAsDropDown(toolbar, 0, 0);
        //provincePopupWindow.update();
    }

    @Override
    public void onClick(View view) {
        if(view == scoreProvinceSelected) {
            //31 provinces
           if(province.size() == 31) {
               showPopupWindow();
           } else {
               Toast.makeText(getActivity(),"network error",Toast.LENGTH_SHORT);
               loadProvinceData();
           }

        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("zhang", "position=" + position);
        provinceName.setText(province.get(position));
        provinceAdapter.setSelectedItem(position);
        provinceAdapter.notifyDataSetChanged();
        if(provincePopupWindow != null) {
           provincePopupWindow.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(scoreProvinceSelected != null) {
            scoreProvinceSelected.setVisibility(View.GONE);
        }
    }

}
