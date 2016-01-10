package com.example.bruce.zhumeng.mvp.presenters;

import com.example.bruce.zhumeng.mvp.views.SchoolsView;

/**
 * Created by bruce on 2016/1/2.
 */
public class SchoolsPresenter extends Presenter {

    private SchoolsView schoolsView;

    public void attachView(SchoolsView schoolsView) {

        this.schoolsView = schoolsView;
    }

    @Override
    public void start() {
        if(schoolsView.isTheListEmpty()) {
            schoolsView.showLoading();
        }
    }

    @Override
    public void stop() {

    }
}
