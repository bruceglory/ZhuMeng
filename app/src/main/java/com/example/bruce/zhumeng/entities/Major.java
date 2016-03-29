package com.example.bruce.zhumeng.entities;

/**
 * Created by GKX100127 on 2016/1/7.
 */
public class Major {

    private String majorName;
    private int majorId;
    private int majorDetailCategoryId;

    public Major(int majorDetailCategoryId, int majorId, String majorName) {
        this.majorDetailCategoryId = majorDetailCategoryId;
        this.majorId = majorId;
        this.majorName = majorName;
    }

    public String getMajorName() {
        return majorName;
    }

    public int getMajorId() {
        return majorId;
    }

    public int getMajorDetailCategoryId() {
        return majorDetailCategoryId;
    }
}
