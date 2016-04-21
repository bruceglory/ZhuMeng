package com.example.bruce.zhumeng.entities;

import java.io.Serializable;

/**
 * Created by GKX100127 on 2016/1/7.
 */
public class Major implements Serializable {

    private String majorName;
    private int majorId;
    private int majorDetailCategoryId;
    private String majorCourse;
    private String majorOverview;

    public Major(int majorDetailCategoryId, int majorId, String majorName) {
        this.majorDetailCategoryId = majorDetailCategoryId;
        this.majorId = majorId;
        this.majorName = majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public void setMajorDetailCategoryId(int majorDetailCategoryId) {
        this.majorDetailCategoryId = majorDetailCategoryId;
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

    public String getMajorCourse() {
        return majorCourse;
    }

    public void setMajorCourse(String majorCourse) {
        this.majorCourse = majorCourse;
    }

    public String getMajorOverview() {
        return majorOverview;
    }

    public void setMajorOverview(String majorOverview) {
        this.majorOverview = majorOverview;
    }
}
