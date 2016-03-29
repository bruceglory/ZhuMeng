package com.example.bruce.zhumeng.entities;

import java.util.List;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorDetailCategory {
    private int majorCategoryId;
    private int majorDetailCategoryId;
    private String majorDetailCategoryName;
    private List<Major> majorList ;

    public MajorDetailCategory(int mMajorCategoryId, int mMajorDetailCategoryId,
                               String mMajorDetailCategoryName) {
        this.majorCategoryId         = mMajorCategoryId;
        this.majorDetailCategoryId   = mMajorDetailCategoryId;
        this.majorDetailCategoryName = mMajorDetailCategoryName;
    }


    public int getMajorCategoryId() {
        return majorCategoryId;
    }

    public int getMajorDetailCategoryId() {
        return majorDetailCategoryId;
    }

    public String getMajorDetailCategoryName() {
        return majorDetailCategoryName;
    }

    public List<Major> getMajorList() {
        return majorList;
    }

    public void setMajorList(List<Major> majorList) {
        this.majorList = majorList;
    }
}
