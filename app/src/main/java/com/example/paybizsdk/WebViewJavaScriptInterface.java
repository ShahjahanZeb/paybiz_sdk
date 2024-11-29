package com.example.paybizsdk;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebViewJavaScriptInterface {

    private Context context;

    public WebViewJavaScriptInterface(Context context) {
        this.context = context;
    }


    @JavascriptInterface
    public void showData(String message) {
        System.out.println("Data from WebView: " + message);
    }

}
