package com.example.bruce.zhumeng.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.MajorCategoryAdapter;
import com.example.bruce.zhumeng.entities.MajorCategory;
import com.example.bruce.zhumeng.entities.MajorDetailCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorsFragment extends Fragment {

    private ExpandableListView expandableListView;
    private MajorCategoryAdapter adapter;
    private List<MajorCategory> categoryList = new ArrayList<>();
    private List<MajorDetailCategory> detailCategoryList = new ArrayList<>();
    private List<MajorDetailCategory> detailCategoryList1 = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.major_main_view,container,false);
        findView(rootView);
        initialize();
        return rootView;
    }

    private void findView(View rootView) {
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.major_list);
    }

    private void initialize() {
        detailCategoryList.add(new MajorDetailCategory(2,1,"jingjixue"));
        detailCategoryList.add(new MajorDetailCategory(2,2,"caizhengxue"));
        detailCategoryList.add(new MajorDetailCategory(2,3,"jinrongxue"));
        detailCategoryList1.add(new MajorDetailCategory(1,1,"zhexue"));

        categoryList.add(new MajorCategory(1,"zhexue",detailCategoryList1));
        categoryList.add(new MajorCategory(2,"jingjixue",detailCategoryList));
        adapter = new MajorCategoryAdapter(getActivity(),categoryList);
        expandableListView.setAdapter(adapter);
        expandableListView = new ExpandableListView(getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams
                (AbsListView.LayoutParams.WRAP_CONTENT,AbsListView.LayoutParams.WRAP_CONTENT);
        expandableListView.setLayoutParams(lp);
    }


}
