package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.entities.MajorCategory;

import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorCategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MajorCategory> majorCategoryList = null;

    public MajorCategoryAdapter(Context mContext,List<MajorCategory> mMajorCategoryList) {
        this.context = mContext;
        this.majorCategoryList = mMajorCategoryList;
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
    public Object getChild(int groupPosition, int childPostion) {
        return majorCategoryList.get(groupPosition).getMajorDetailCategoryList().get(childPostion);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return majorCategoryList.get(groupPosition).getMajorCategoryId();
    }

    @Override
    public long getChildId(int groupPosition, int childPostion) {
        return majorCategoryList.get(groupPosition).getMajorDetailCategoryList()
                .get(childPostion).getMajorDetailCategoryId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpand, View convertView, ViewGroup parent) {
        MajorFirstListHolder majorFirstListHolder = null;
        if(convertView == null) {
            majorFirstListHolder = new MajorFirstListHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.major_first_list,null);
            majorFirstListHolder.categoryName =
                    (TextView) convertView.findViewById(R.id.major_category_name);
            convertView.setTag(majorFirstListHolder);
        } else {
            majorFirstListHolder = (MajorFirstListHolder)convertView.getTag();
        }

        majorFirstListHolder.categoryName
                .setText(majorCategoryList.get(groupPosition).getMajorCategoryName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        MajorFirstListHolder majorFirstListHolder = null;
        if(convertView == null) {
            majorFirstListHolder = new MajorFirstListHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.major_first_list,null);
            majorFirstListHolder.categoryName =
                    (TextView) convertView.findViewById(R.id.major_category_name);
            convertView.setTag(majorFirstListHolder);
        } else {
            majorFirstListHolder = (MajorFirstListHolder)convertView.getTag();
        }

        majorFirstListHolder.categoryName
                .setText(majorCategoryList.get(groupPosition).getMajorDetailCategoryList()
                        .get(childPosition).getMajorDetailCategoryName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class MajorFirstListHolder{
        TextView categoryName;
    }
}
