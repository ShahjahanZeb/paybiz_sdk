package com.example.paybizsdk.entity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.paybizsdk.R;

public class WebViewFragment extends FrameLayout {

    private WebView webView;


    public WebViewFragment(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        webView = new WebView(context);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Ensure links open within the WebView and not in a browser
        webView.setWebViewClient(new WebViewClient());

        // Add WebView to the FrameLayout (WebViewWrapper)
        addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void loadHtml(String htmlContent) {
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
    }

    public void destroyWebView() {
        if (webView != null) {
            webView.destroy();
        }
    }
}
