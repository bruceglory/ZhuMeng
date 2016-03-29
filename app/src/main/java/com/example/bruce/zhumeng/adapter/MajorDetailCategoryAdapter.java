package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.entities.MajorDetailCategory;

/**
 * Created by GKX100127 on 2016/1/7.
 */
public class MajorDetailCategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private MajorDetailCategory majorDetailCategory;

    public MajorDetailCategoryAdapter(
            Context context, MajorDetailCategory majorDetailCategory) {
        this.context             = context;
        this.majorDetailCategory = majorDetailCategory;
    }

    @Override
    public int getGroupCount() {
        return majorDetailCategory == null ? 0 : 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return majorDetailCategory.getMajorList() == null ? 0 :
                majorDetailCategory.getMajorList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return majorDetailCategory;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return majorDetailCategory.getMajorList().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MajorDetailCategoryHolder majorDetailCategoryHolder ;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.major_categoy_detail,null);
            majorDetailCategoryHolder = new MajorDetailCategoryHolder(convertView);
            convertView.setTag(majorDetailCategoryHolder);
        } else {
            majorDetailCategoryHolder = (MajorDetailCategoryHolder)convertView.getTag();
        }

        majorDetailCategoryHolder.categoryName
                .setText(majorDetailCategory.getMajorDetailCategoryName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MajorHolder majorHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.major_child_list,null);
            majorHolder = new MajorHolder(convertView);
            convertView.setTag(majorHolder);
        } else {
            majorHolder = (MajorHolder) convertView.getTag();
        }
        majorHolder.majorName.setText(majorDetailCategory.getMajorList().get(childPosition)
                .getMajorName());
        return  convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class MajorHolder {
        TextView majorName;

        public MajorHolder(View view) {
            majorName = (TextView) view.findViewById(R.id.major_name);
        }
    }

    class MajorDetailCategoryHolder{
        TextView categoryName;

        public MajorDetailCategoryHolder(View view) {
            categoryName = (TextView) view.findViewById(R.id.major_category_detail_name_tv);
        }
    }

}
