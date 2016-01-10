package com.example.bruce.zhumeng.mvp.views;

import com.example.bruce.zhumeng.entities.School;

import java.util.List;

/**
 * Created by bruce on 2016/1/1.
 */
public interface SchoolsView  {
    void showSchools(List<School> movieList);

    void showLoading ();

    void hideLoading ();

    void showLoadingLabel();

    void hideActionLabel ();

    boolean isTheListEmpty ();

    void appendSchools (List<School> schoolList);
}
