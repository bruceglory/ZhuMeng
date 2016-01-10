package com.example.bruce.zhumeng.entities;

import java.io.Serializable;

/**
 * Created by bruce on 2016/1/2.
 */
public class School implements Serializable {

    private int id;
    private String schoolName;
    private String pictureUrl;
    private boolean _985;
    private boolean _211;
    private boolean schoolReady;

    public School(int id,String schoolName,String pictureUrl,boolean _985,boolean _211) {
        this.id = id;
        this.schoolName = schoolName;
        this.pictureUrl = pictureUrl;
        this._985 = _985;
        this._211 = _211;
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

    public boolean is_985() {
        return _985;
    }

    public boolean is_211() {
        return _211;
    }

    public void setSchoolReady(boolean schoolReady) {

        this.schoolReady = schoolReady;
    }

    public boolean isSchoolReady() {

        return schoolReady;
    }
}
