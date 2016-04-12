package com.example.bruce.zhumeng.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.bruce.zhumeng.MainActivity;
import com.example.bruce.zhumeng.R;

/**
 * Created by bruce on 2016/1/4.
 */
public class PsysFragment extends BaseFragment {

    private WebView psyWebView;
    private Toolbar toolbar;

    public static PsysFragment newInstance() {
        PsysFragment psysFragment = new PsysFragment();
        return psysFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.psy_mainview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        findView(mRootView);
        initialize();
    }

    private void findView(View rootView) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.drawer_psy);
        psyWebView = (WebView) rootView.findViewById(R.id.psy_webview);
    }

    private void initialize() {
        setUpWebView();
        psyWebView.getSettings().setJavaScriptEnabled(true);
        psyWebView.loadUrl("http://www.apesk.com/mbti/dati.asp");
    }

    private void setUpWebView() {
        psyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });


//        psyWebView.setFocusableInTouchMode(true);
    }
}
