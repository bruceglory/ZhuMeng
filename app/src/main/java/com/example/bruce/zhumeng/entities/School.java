package com.example.bruce.zhumeng.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by bruce on 2016/1/2.
 */
public class School implements Serializable {

    private String objectId;
    private int ranking;
    private String iconUrl;
    private String type;
    private String belongto;
    private String address;
    private String tel;
    private String schoolName;
    private String pictureUrl;

    private int doctorNumber;
    private int masterNumber;
    private int importantMajorNumber;
    private int academicianNumber;

    private String description;

    private boolean schoolReady;

    public School(int ranking,String schoolName,String pictureUrl) {
        this.ranking = ranking;
        this.schoolName = schoolName;
        this.pictureUrl = pictureUrl;

    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String proVince) {
        this.type = proVince;
    }

    public String getBelongto() {
        return belongto;
    }

    public void setBelongto(String belongto) {
        this.belongto = belongto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(int doctorNumber) {
        this.doctorNumber = doctorNumber;
    }

    public int getMasterNumber() {
        return masterNumber;
    }

    public void setMasterNumber(int masterNumber) {
        this.masterNumber = masterNumber;
    }

    public int getImportantMajorNumber() {
        return importantMajorNumber;
    }

    public void setImportantMajorNumber(int importantMajorNumber) {
        this.importantMajorNumber = importantMajorNumber;
    }

    public int getAcademicianNumber() {
        return academicianNumber;
    }

    public void setAcademicianNumber(int academicianNumber) {
        this.academicianNumber = academicianNumber;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getRanking() {
        return ranking;
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
