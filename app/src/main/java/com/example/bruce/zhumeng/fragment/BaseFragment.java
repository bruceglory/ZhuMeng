package com.example.bruce.zhumeng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.bruce.zhumeng.R;

import java.util.List;

/**
 * Created by bruce on 2015/12/29.
 */
public abstract class BaseFragment extends Fragment {

    LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(loadingPage == null) {
            loadingPage = new LoadingPage(getActivity()){
                @Override
                protected View createSuccessView() {
                   return BaseFragment.this.createSuccessView();
                }

                @Override
                public LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
        }
        return loadingPage;
    }


    public abstract LoadingPage.LoadResult load();


    protected abstract View createSuccessView();


    public void show(){
        if (loadingPage != null){
            loadingPage.show();
        }
    }

    public LoadingPage.LoadResult checkData(List datas) {
        if(datas == null) {
            return LoadingPage.LoadResult.error;
        } else {
            if(datas.size() == 0) {
                return LoadingPage.LoadResult.empty;
            } else {
                return LoadingPage.LoadResult.success;
            }

        }
    }

}


