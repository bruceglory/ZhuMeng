package com.example.bruce.zhumeng.entities;

/**
 * Created by bruce on 2015/12/30.
 */
public class SchoolInfo {

    private int id;
    private String schoolName;
    private String pictureUrl;
    private boolean _985;
    private boolean _211;

    public SchoolInfo(int id,String schoolName,String pictureUrl,boolean _985,boolean _211) {
        this.id = id;
        this.schoolName = schoolName;
        this.pictureUrl = pictureUrl;
        this._985 = _985;
        this._211 = _211;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean is_985() {
        return _985;
    }

    public void set_985(boolean _985) {
        this._985 = _985;
    }

    public boolean is_211() {
        return _211;
    }

    public void set_211(boolean _211) {
        this._211 = _211;
    }
}
