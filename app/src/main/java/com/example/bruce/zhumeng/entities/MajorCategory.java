package com.example.bruce.zhumeng.entities;

import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorCategory {

    private int majorCategoryId;
    private String majorCategoryName;
    private List<MajorDetailCategory> majorDetailCategoryList ;

    public MajorCategory(int id, String name) {
        this.majorCategoryId         = id;
        this.majorCategoryName       = name;
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

    public void setMajorDetailCategoryList(List<MajorDetailCategory> majorDetailCategoryList) {
        this.majorDetailCategoryList = majorDetailCategoryList;
    }
}
