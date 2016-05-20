package com.example.bruce.zhumeng.model.entities;

/**
 * Created by zhang on 2016/5/12.
 */
public class CommentInfo {
    private Comment comment;
    private User user;

    public CommentInfo(Comment comment,User user) {
        this.comment = comment;
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
