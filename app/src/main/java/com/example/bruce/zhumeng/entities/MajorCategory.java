package com.example.bruce.zhumeng.entities;

import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorCategory {

    private int majorCategoryId;
    private String majorCategoryName;
    private List<MajorDetailCategory> majorDetailCategoryList;

    public MajorCategory(int id,String name,List<MajorDetailCategory> list) {
        this.majorCategoryId         = id;
        this.majorCategoryName       = name;
        this.majorDetailCategoryList = list;
    }

    public int getMajorCategoryId() {
        return majorCategoryId;
    }

    public String getMajorCategoryName() {
        return majorCategoryName;
    }

    public List<MajorDetailCategory> getMajorDetailCategoryList() {
        return majorDetailCategoryList;
    }
}
