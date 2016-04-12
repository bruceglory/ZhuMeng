package com.example.bruce.zhumeng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.MajorCategoryAdapter;
import com.example.bruce.zhumeng.entities.Major;
import com.example.bruce.zhumeng.entities.MajorCategory;
import com.example.bruce.zhumeng.entities.MajorDetailCategory;
import com.example.bruce.zhumeng.MajorDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorsFragment extends BaseFragment implements ExpandableListView.OnGroupClickListener,
        MajorCategoryAdapter.OnChildTreeViewClickListener {

    private static final int LOAD_MAJORCATEGORY_SUCCESS = 0;
    private static final int LOAD_MAJOR_DETAIL_CATTGORY_SUCCESS = 1;
    public static final int  LOAD_MAJOR_SUCCESS = 2;

    private Toolbar toolbar;
    private ExpandableListView expandableListView;
    private MajorCategoryAdapter adapter;

    private List<MajorCategory> categoryList = new ArrayList<>();
    private List<MajorDetailCategory> detailCategoryList = new ArrayList<>();
    private List<MajorDetailCategory> detailCategoryList2 = new ArrayList<>();

    private Handler majorsHandler;
    private int clickPosition = 0;

    public static MajorsFragment newInstance() {
        MajorsFragment majorsFragment = new MajorsFragment();
        return majorsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.major_main_view;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        findView(mRootView);
        initialize();
    }

    private void findView(View rootView) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.drawer_major);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.major_list);
    }

    private void initialize() {
        initializeData();
        List<Major> majorlist = new ArrayList<>();
        majorlist.add(new Major(1,1,"jkgeogeyio"));
        majorlist.add(new Major(1, 2, "jkfgejigejokgo"));
        majorlist.add(new Major(1, 3, "jgi3g"));
        MajorDetailCategory majorDetailCategory1 = new MajorDetailCategory(1,5,"jhifge");
        majorDetailCategory1.setMajorList(majorlist);
        detailCategoryList.add(new MajorDetailCategory(1, 1, "zhexue"));
        detailCategoryList.add(new MajorDetailCategory(1,2,"zhengzhixue"));
        detailCategoryList.add(new MajorDetailCategory(1,3,"gekjge"));
        detailCategoryList.add(new MajorDetailCategory(1,4,"jgoiejg"));

        detailCategoryList.add(majorDetailCategory1);

        detailCategoryList2.add(new MajorDetailCategory(2, 1, "gjek"));
        detailCategoryList2.add(new MajorDetailCategory(2, 2, "gjeigjh"));

        MajorCategory majorCategory1 = new MajorCategory(1,"zhexue");
        majorCategory1.setMajorDetailCategoryList(detailCategoryList);
        MajorCategory majorCategory2 = new MajorCategory(2,"caizhengxue");
        majorCategory2.setMajorDetailCategoryList(detailCategoryList2);
        //categoryList.add(majorCategory1);
        //categoryList.add(majorCategory2);
        adapter = new MajorCategoryAdapter(getActivity(),categoryList,majorsHandler);

        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupClickListener(this);
        adapter.setOnChildTreeViewClickListener(this);
    }

    private void initializeData() {
        majorsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(adapter != null) {
                    if (msg.what == LOAD_MAJORCATEGORY_SUCCESS) {
                        adapter.notifyDataSetChanged();
                    } else if (msg.what == LOAD_MAJOR_DETAIL_CATTGORY_SUCCESS) {
                        adapter.notifyDataSetChanged();
                        if(expandableListView != null) {
                            expandableListView.expandGroup(clickPosition,true);
                        }
                    } else if (msg.what == LOAD_MAJOR_SUCCESS) {
                        MajorCategoryAdapter majorCategoryAdapter = (MajorCategoryAdapter)
                                expandableListView.getExpandableListAdapter();
                        majorCategoryAdapter.getMajorDetailCategoryAdapter().notifyDataSetChanged();
                        majorCategoryAdapter.getChildExpandableListView().expandGroup(0);

                    }
                }
            }
        };

        loadMajorCategoryData();
    }

    private void loadMajorCategoryData() {
        Log.d("zhang","loadMajorCategoryData");
        AVQuery<AVObject> queryMajorCategory = new AVQuery<>("major_category");
        queryMajorCategory.whereGreaterThanOrEqualTo("major_category_id",1);
        queryMajorCategory.whereLessThanOrEqualTo("major_category_id",13);
        queryMajorCategory.orderByAscending("major_category_id");
        queryMajorCategory.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject majorCategory : list) {
                        categoryList.add(new MajorCategory(
                                majorCategory.getInt("major_category_id"),
                                majorCategory.getString("major_category_name")));
                    }
                    for (MajorCategory ma : categoryList) {
                        Log.d("zhang", "name=" + ma.getMajorCategoryName());
                    }
                    if (categoryList.size() > 0)
                        majorsHandler.sendEmptyMessage(LOAD_MAJORCATEGORY_SUCCESS);
                } else {
                    Log.d("zhang", "load majorcategory error");
                }
            }
        });
    }

    private void loadMajorDetailCategoryData(final int parent) {
         Log.d("zhang", "loadMajorDetailCategoryData");
        AVQuery<AVObject> queryMajorCategory = new AVQuery<>("major_detail_category");
        queryMajorCategory.whereEqualTo("major_category_id", parent+1);
        queryMajorCategory.orderByAscending("major_detail_category_id");
        queryMajorCategory.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null) {
                    detailCategoryList.clear();
                    for(AVObject majorCategory :list ) {
                         detailCategoryList.add(new MajorDetailCategory(
                                 majorCategory.getInt("major_category_id"),
                                 majorCategory.getInt("major_detail_category_id"),
                                 majorCategory.getString("major_detail_category_name")));
                    }
                    for(MajorDetailCategory ma : detailCategoryList) {
                        Log.d("zhang","name="+ma.getMajorDetailCategoryName());
                    }
                    if(detailCategoryList.size()>0)
                        if(categoryList.size()>parent) {
                            categoryList.get(parent).setMajorDetailCategoryList(detailCategoryList);
                            majorsHandler.sendEmptyMessage(LOAD_MAJOR_DETAIL_CATTGORY_SUCCESS);
                        }
                } else {
                    Log.d("zhang","load majorcategory error");
                }
            }
        });
    }
    @Override
    public boolean onGroupClick(ExpandableListView majorCategoryListView, View view,
                                int groupPosition, long id) {
        Log.d("zhang", "onGroupClick gp=" + groupPosition);
        clickPosition = groupPosition;

        for (int i = 0; i < categoryList.size(); i++) {
            if (i != groupPosition) {
                expandableListView.collapseGroup(i);
            }
        }

        boolean expanded = majorCategoryListView.isGroupExpanded(groupPosition);
        if(!expanded) {
            loadMajorDetailCategoryData(groupPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onClickPosition(int parentPosition, int groupPosition, int childPosition) {
        Log.d("zhang","onClickPosition parentPosition="+parentPosition+"gp="+groupPosition
        +"cp="+childPosition);
        Intent majorFragmentIntent = new Intent(getActivity(), MajorDetailActivity.class);
        getActivity().startActivity(majorFragmentIntent);
    }
}
