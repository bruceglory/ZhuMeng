package com.example.bruce.zhumeng.entities;

import java.io.Serializable;

/**
 * Created by bruce on 2016/1/2.
 */
public class School implements Serializable {

    private int id;
    private String schoolName;
    private String pictureUrl;
    private boolean schoolReady;

    public School(int id,String schoolName,String pictureUrl) {
        this.id = id;
        this.schoolName = schoolName;
        this.pictureUrl = pictureUrl;

    }

    public int getId() {
        return id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }


    public void setSchoolReady(boolean schoolReady) {

        this.schoolReady = schoolReady;
    }

    public boolean isSchoolReady() {

        return schoolReady;
    }
}
