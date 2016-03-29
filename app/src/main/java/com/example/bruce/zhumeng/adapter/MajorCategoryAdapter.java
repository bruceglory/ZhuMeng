package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.entities.Major;
import com.example.bruce.zhumeng.entities.MajorCategory;
import com.example.bruce.zhumeng.entities.MajorDetailCategory;
import com.example.bruce.zhumeng.fragment.MajorsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorCategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MajorCategory> majorCategoryList = null;
    private OnChildTreeViewClickListener childTreeViewClickListener;
    private Handler majorHandler;
    private MajorDetailCategoryAdapter childAdapter;
    private ExpandableListView childExpandableListView;

    public MajorCategoryAdapter(Context mContext, List<MajorCategory> mMajorCategoryList, Handler
            majorHandler) {
        this.context = mContext;
        this.majorCategoryList = mMajorCategoryList;
        this.majorHandler = majorHandler;
    }

    @Override
    public int getGroupCount() {
        return majorCategoryList == null ? 0 : majorCategoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return majorCategoryList == null ? 0 :
                (majorCategoryList.get(groupPosition).getMajorDetailCategoryList() == null ? 0 :
                majorCategoryList.get(groupPosition).getMajorDetailCategoryList().size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return majorCategoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return majorCategoryList.get(groupPosition).getMajorDetailCategoryList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpand, View convertView, ViewGroup parent) {
        MajorCategoryHolder majorCategoryHolder ;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.major_first_list,null);
            majorCategoryHolder = new MajorCategoryHolder(convertView);
            convertView.setTag(majorCategoryHolder);
        } else {
            majorCategoryHolder = (MajorCategoryHolder)convertView.getTag();
        }

        majorCategoryHolder.categoryNameTV
                .setText(majorCategoryList.get(groupPosition).getMajorCategoryName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListView detailCategoryExpandableListView = getExpandableListView();
        final MajorDetailCategoryAdapter  majorDetailCategoryAdapter  =
                new MajorDetailCategoryAdapter(context,
                        majorCategoryList.get(groupPosition).getMajorDetailCategoryList()
                        .get(childPosition));

        final MajorDetailCategory majorDetailCategory = majorCategoryList.get(groupPosition)
                .getMajorDetailCategoryList().get(childPosition);
        detailCategoryExpandableListView.setAdapter(majorDetailCategoryAdapter);

        /**
         * called when childExpandableListView childView is clicked
         *@author zxr
         *created at 2016/1/18 14:15
         */
        detailCategoryExpandableListView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view,
                                                int groupIndex, int childIndex, long l) {
                        if (childTreeViewClickListener != null) {
                            childTreeViewClickListener.onClickPosition(groupPosition,childPosition,
                                    childIndex);
                        }
                        return false;
                    }
                });

//        detailCategoryExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
//                Log.d("zhang","childexpand groupclick");
//                if(!detailCategoryExpandableListView.isGroupExpanded(position)) {
//                    detailCategoryExpandableListView.expandGroup(position);
//                    return true;
//                }
//                return false;
//            }
//        });
        detailCategoryExpandableListView.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int childGroupPosition) {
                        childExpandableListView = detailCategoryExpandableListView;
                        childAdapter = majorDetailCategoryAdapter;
                        if(majorDetailCategory.getMajorList() == null) {
                                Log.d("zhang","load major data") ;
                                AVQuery<AVObject> majorQuery = new AVQuery<>("Major");
                                majorQuery.whereEqualTo("major_category_id",groupPosition+1);
                                majorQuery.whereEqualTo("major_detail_category_id",childPosition+1);
                                majorQuery.orderByAscending("major_id");
                                majorQuery.findInBackground(new FindCallback<AVObject>() {
                                    @Override
                                    public void done(List<AVObject> list, AVException e) {
                                        List<Major> majorList = new ArrayList<Major>();
                                        if(e == null) {
                                            for(AVObject major : list) {
                                                majorList.add(new Major(major.getInt
                                                        ("major_detail_category_id"),major.getInt
                                                        ("major_id"),major.getString
                                                        ("major_name")));
                                            }
                                            for(Major ma : majorList) {
                                                Log.d("zhang","major name = "+ma.getMajorName());
                                            }
                                            if(majorList.size()>0) {
                                                majorDetailCategory.setMajorList(majorList);
                                                majorHandler.sendEmptyMessage(MajorsFragment
                                                        .LOAD_MAJOR_SUCCESS);
                                            }
                                        } else {
                                           Log.d("zhang","load major error");
                                        }
                                    }

                                });
                        }
                        Log.d("zhang","childexpand ongroupexpand");
                        if (majorCategoryList.get(groupPosition).getMajorDetailCategoryList()
                                .get(childPosition).getMajorList() != null) {
                            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                                    (majorCategoryList.get(groupPosition).getMajorDetailCategoryList()
                                            .get(childPosition).getMajorList().size() + 1) * (int)
                                            context.getResources().getDimension(
                                                    R.dimen.major_category_expandable_list_height));
                            detailCategoryExpandableListView.setLayoutParams(lp);
                        }
                    }
                });

        detailCategoryExpandableListView.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int i) {
                        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                                (int) context.getResources().getDimension(
                                        R.dimen.major_category_expandable_list_height));
                        detailCategoryExpandableListView.setLayoutParams(lp);
                    }
                });

        detailCategoryExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                return false;
            }
        });


        return detailCategoryExpandableListView;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    /**
     * create childExpandableListView
     *@author zxr
     *created at 2016/1/18 13:02
     */
    public ExpandableListView getExpandableListView() {
        ExpandableListView expandableListView = new ExpandableListView(context);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,(int)context.getResources()
                .getDimension(R.dimen.major_category_expandable_list_height));
        expandableListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));


        expandableListView.setLayoutParams(lp);
        //expandableListView.setGroupIndicator(null);
        //expandableListView.setDividerHeight(0);
        expandableListView.setPadding((int)context.getResources().
                getDimension(R.dimen.major_child_expandable_padding_left),0,0,0);
        return expandableListView;
    }

    class MajorCategoryHolder{

        private TextView categoryNameTV;

        public MajorCategoryHolder(View view) {
            categoryNameTV = (TextView) view.findViewById(R.id.major_category_name);
        }
    }

    public void setOnChildTreeViewClickListener(OnChildTreeViewClickListener onChildTreeViewClickListener) {
        this.childTreeViewClickListener = onChildTreeViewClickListener;
    }
    /**
     * called when ExpandListview child is cliked
     *@author zxr
     *created at 2016/1/18 10:30
     */
    public interface OnChildTreeViewClickListener {

        void onClickPosition(int parentPosition, int groupPosition, int childPosition);
    }


    public ExpandableListView getChildExpandableListView() {
        return childExpandableListView;
    }

    public MajorDetailCategoryAdapter getMajorDetailCategoryAdapter() {
        return childAdapter;
    }
}
