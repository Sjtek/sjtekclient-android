package nl.sjtek.client.android.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.storage.Preferences;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragmentWeb extends BaseFragment {

    private WebView webView;

    public BaseFragmentWeb() {
        // Required empty public constructor
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler,
                                                  String host,
                                                  String realm) {
                Preferences preferences = Preferences.getInstance(getContext());
                handler.proceed(preferences.getUsername(), preferences.getPassword());
            }
        });
        webView.loadUrl(getUrl());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    protected abstract String getUrl();
}
