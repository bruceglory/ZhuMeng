package com.example.bruce.zhumeng.entities;

/**
 * Created by bruce on 2016/1/6.
 */
public class MajorDetailCategory {
    private int majorCategoryId;
    private int majorDetailCategoryId;
    private String majorDetailCategoryName;

    public MajorDetailCategory(int mMajorCategoryId,int mMajorDeatailCategoryId,
                               String mMajorDetailCategoryName) {
        this.majorCategoryId         = mMajorCategoryId;
        this.majorDetailCategoryId   = mMajorDeatailCategoryId;
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
}
