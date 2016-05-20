package com.example.bruce.zhumeng.model.entities;

import java.util.Date;

/**
 * Created by zhang on 2016/5/12.
 */
public class Comment {

    private String comment;
    private Date   updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
