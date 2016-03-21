package nl.sjtek.client.android.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.VolleyError;

import nl.sjtek.client.android.R;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragmentWeb extends BaseFragment {

    private WebView webView;
    private View loadingView;

    public BaseFragmentWeb() {
        // Required empty public constructor
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        loadingView = rootView.findViewById(R.id.progressBar);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
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

    @Override
    protected void onUpdate(ResponseCollection update) {
        webView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onError(VolleyError error) {
        webView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }
}
