package com.example.bruce.zhumeng.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.bruce.zhumeng.R;

/**
 * Created by bruce on 2015/12/29.
 */
public abstract class LoadingPage extends FrameLayout{

    //five states
    static final int STATE_UNKNOW = 0;
    static final int STATE_LOADING = 1;
    static final int STATE_ERROR = 2;
    static final int STATE_EMPTY = 3;
    static final int STATE_SUCCESS = 4;

    int state = STATE_UNKNOW;
    private Context mContext;
    //four views
    //loading
    private View loadingView;
    private View errorView;
    private View successView;
    private View EmptyView;

    public LoadingPage(Context context) {
        this(context, null);
        mContext = context;
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    //add fourViews.success,error,success but no data,loading
    private void init() {
        Log.d("zhang","loading init");
        loadingView = createLoadingView();
        if(loadingView != null){
            this.addView(loadingView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }

        errorView = createErrorView();
        if(errorView != null) {
            this.addView(errorView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }

        showPage();
    }

    //loading layout
    private View createLoadingView() {
        return View.inflate(mContext, R.layout.loading_view_loading,null);
    }

    //error layout
    private View createErrorView() {
        return View.inflate(mContext,R.layout.loading_view_error,null);
    }

    /**
     * success view
     *
     * @return
     */
    protected abstract View createSuccessView();


    private void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility(
                    state == STATE_UNKNOW || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        }



        if (state == STATE_SUCCESS) {
            successView = createSuccessView();
            if (successView != null) {
                this.addView(successView, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                successView.setVisibility(View.VISIBLE);
            }
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public enum LoadResult {
        error(2), empty(3), success(4);
        int value;


        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public void show() {

        if (state == STATE_ERROR || state == STATE_EMPTY) {
            state = STATE_LOADING;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                final LoadResult result = load();
                state = result.getValue();

            }
        }).start();
        showPage();
    }


    public abstract LoadResult load();
}
