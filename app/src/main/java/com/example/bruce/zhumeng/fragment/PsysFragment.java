package com.example.bruce.zhumeng.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class PsysFragment extends Fragment {

    private WebView psyWebView;
    private TextView toolbarTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.psy_mainview,container,false);
        findView(rootView);
        initialize();
        return rootView;
    }

    private void findView(View rootView) {
        psyWebView = (WebView) rootView.findViewById(R.id.psy_webview);
        //toolbarTitle = (TextView)getActivity().findViewById(R.id.toolbar_title1);
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
