package com.example.paybizsdk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.ButtonType;
import com.example.paybizsdk.constants.UICustomizationType;
import com.example.paybizsdk.controller.PaybizController;
import com.example.paybizsdk.encryption.AESEncryption;
import com.example.paybizsdk.entity.ButtonCustomization;
import com.example.paybizsdk.entity.LabelCustomization;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.entity.TextBoxCustomization;
import com.example.paybizsdk.entity.ToolbarCustomization;
import com.example.paybizsdk.entity.WebViewFragment;
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.ThreeDSService;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.service.UiCustomization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WebView webView;

//    String htmlContent = "<html>" +
//            "<body>" +
//            "<h1>Welcome to WebView</h1>" +
//            "<img src='https://via.placeholder.com/150' alt='Sample Image' />" +
//            "<p>Please enter your name:</p>" +
//            "<input type='text' id='nameInput' />" +
//            "<button type='button' onclick='sendData()'>Submit</button>" +
//            "<script type='text/javascript'>" +
//            "function sendData() {" +
//            "   var name = document.getElementById('nameInput').value;" +
//            "   Android.showData(name);" +  // Call Android function
//            "}" +
//            "</script>" +
//            "</body>" +
//            "</html>";

    String htmlContent = "<html>" +
            "<head>" +
            "    <meta charset=\"UTF-8\">" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "    <title>Payment Security</title>" +
            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">" +
            "    <style>" +
            "        body {" +
            "            margin: 0;" +
            "            height: 100vh;" +
            "            background-color: #f8f9fa;" +
            "        }" +
            "        .payment-security-container {" +
            "            background-color: white;" +
            "            padding: 20px;" +
            "            border-radius: 10px;" +
            "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
            "            text-align: center;" +
            "            width: 100%;" +
            "            height: 100vh;" +
            "        }" +
            "        .logos {" +
            "            display: flex;" +
            "            justify-content: space-between;" +
            "            align-items: center;" +
            "            margin-bottom: 20px;" +
            "        }" +
            "        .logos img {" +
            "            max-width: 50px;" +
            "        }" +
            "        .security-heading {" +
            "            font-size: 20px;" +
            "            font-weight: bold;" +
            "            margin-bottom: 10px;" +
            "        }" +
            "        .step-text {" +
            "            text-align: left;" +
            "            margin-bottom: 20px;" +
            "        }" +
            "        .btn-continue {" +
            "            background-color: #28a745;" +
            "            color: white;" +
            "            border: none;" +
            "            padding: 10px 20px;" +
            "            border-radius: 5px;" +
            "            font-size: 16px;" +
            "            cursor: pointer;" +
            "            width: 100%;" +
            "        }" +
            "        .btn-continue:hover {" +
            "            background-color: #218838;" +
            "        }" +
            "        .help-links {" +
            "            margin-top: 20px;" +
            "        }" +
            "        .help-links a {" +
            "            display: block;" +
            "            color: #007bff;" +
            "            text-decoration: none;" +
            "            margin-bottom: 10px;" +
            "        }" +
            "        .help-links a:hover {" +
            "            text-decoration: underline;" +
            "        }" +
            "        .arrow {" +
            "            float: right;" +
            "        }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class=\"payment-security-container\">" +
            "        <div class=\"logos\">" +
            "            <img src=\"https://via.placeholder.com/50x50?text=YourBank\" alt=\"YourBank Logo\">" +
            "            <img src=\"https://via.placeholder.com/50x50?text=Card\" alt=\"Card Network Logo\">" +
            "        </div>" +
            "        <div class=\"security-heading\">Payment Security</div>" +
            "        <p>For added security, you will be authenticated with YourBank application.</p>" +
            "        <div class=\"step-text\">" +
            "            <p><strong>Step 1</strong> – Open your YourBank application directly from your phone and verify this payment.</p>" +
            "            <p><strong>Step 2</strong> – Tap continue after you have completed authentication with your YourBank application.</p>" +
            "        </div>" +
            "        <button class=\"btn-continue\">CONTINUE</button>" +
            "        <div class=\"help-links\">" +
            "            <a href=\"#\">Learn more about authentication <span class=\"arrow\">&#x25BC;</span></a>" +
            "            <a href=\"#\">Need some help? <span class=\"arrow\">&#x25BC;</span></a>" +
            "        </div>" +
            "    </div>" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js\"></script>" +
            "</body>" +
            "</html>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
//        webView.loadData(htmlContent, "text/html", "UTF-8");
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this),"app");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);

        // Add a JavaScript interface to interact with the WebView
        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "Android");

        webView.setWebViewClient(new WebViewClient());


    }

}