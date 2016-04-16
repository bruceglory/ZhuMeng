package com.example.bruce.zhumeng.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bruce.zhumeng.MainActivity;
import com.example.bruce.zhumeng.R;

/**
 * Created by bruce on 2016/1/4.
 */
public class PsysFragment extends BaseFragment {

    private WebView psyWebView;
    private ProgressBar mProgressbar;

    public static PsysFragment newInstance() {
        return new PsysFragment();
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
        psyWebView = (WebView) rootView.findViewById(R.id.psy_webview);
        mProgressbar = (ProgressBar) rootView.findViewById(R.id.psywebview_progress);
    }

    private void initialize() {
        psyWebView.getSettings().setJavaScriptEnabled(true);
        setUpWebView();
        psyWebView.loadUrl("http://www.apesk.com/mbti/dati.asp");
    }

    private void setUpWebView() {
        psyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressbar.setVisibility(View.VISIBLE);
            }
        });

        psyWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressbar.setProgress(newProgress);

                if(newProgress == 100) {
                    mProgressbar.setVisibility(View.GONE);
                }
            }
        });
//        psyWebView.setFocusableInTouchMode(true);
    }

    public boolean onBackPressed() {
        if(psyWebView.canGoBack()) {
            psyWebView.goBack();
            return true;
        }
        return false;
    }

}
