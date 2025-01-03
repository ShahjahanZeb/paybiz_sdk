package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.service.WebViewJavaScriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class HTMLRender extends AppCompatActivity {

    WebView webView;

    JSONObject creqObject;

    String acsURL, oobAppURL;

    Button backButton;

    private final String TAG = "HTML_RENDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmlrender);
        webView = findViewById(R.id.webView);
        backButton = findViewById(R.id.backButton);
        FileLogger.log("INFO", TAG, "In HTML Render Screen");

        Intent intent = getIntent();
        String htmlContent = intent.getStringExtra("htmlContent");
        this.acsURL = intent.getStringExtra("acsURL");
        this.oobAppURL = intent.hasExtra("oobAppUrl") && !intent.getStringExtra("oobAppUrl").isEmpty() ? intent.getStringExtra("oobAppUrl")
                : "";
        try {
            this.creqObject = new JSONObject(intent.getStringExtra("creqObject").toString());
            if (this.creqObject.has("acsHTML")) {
                this.creqObject.remove("acsHTML");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        FileLogger.log("INFO", TAG, "Fetched all data from Intent");


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        FileLogger.log("INFO", TAG, "Load Data in Web View");
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
        // Add a JavaScript interface to interact with the WebView
        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this, this, this.creqObject, this.acsURL, webView, this.oobAppURL), "Android");
        webView.setWebViewClient(new WebViewClient());
        FileLogger.log("INFO", TAG, "Web View Setting Done");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}