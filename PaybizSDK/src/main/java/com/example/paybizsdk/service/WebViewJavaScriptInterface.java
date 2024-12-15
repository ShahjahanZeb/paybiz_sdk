package com.example.paybizsdk.service;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.screens.HTMLRender;
import com.example.paybizsdk.screens.TransactionResult;
import com.example.paybizsdk.utility.ApiClient;
import com.example.paybizsdk.utility.PostRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class WebViewJavaScriptInterface {

    private Context context;
    private JSONObject creqObject;
    private String acsURL, oobAppURL;

    WebView webView;
    Activity activity;

    private final String TAG = "WEB_VIEW_JS_INTERFACE";

    public WebViewJavaScriptInterface(Activity activity, Context context, JSONObject creqObject,
                                      String acsURL, WebView webView, String oobAppURL) {
        this.activity = activity;
        this.context = context;
        this.creqObject = creqObject;
        this.acsURL = acsURL;
        this.webView = webView;
        this.oobAppURL = oobAppURL;
    }

    @JavascriptInterface
    public void openOobApp() {
        Uri appUrl = Uri.parse(oobAppURL);
        Intent appIntent = new Intent(Intent.ACTION_VIEW);
        appIntent.setData(appUrl);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            Toast toast = new Toast(this.activity.getBaseContext());
            toast.setText("Not able to open OOB App" + e.getMessage());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            FileLogger.log("ERROR", TAG, "Not able to open OOB App: " + e.getMessage());
//                            (this, "ABc", Toast.LENGTH_LONG).show();
        }

    }


    @JavascriptInterface
    public void showData(String message) {
        System.out.println("Data from WebView: " + message);
        System.out.println("CREQ Object: " + this.creqObject);
        System.out.println("ACS URL: " + this.acsURL);
        try {
            creqObject.put("challengeDataEntry", message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        sendCreqRequest();
    }

    @JavascriptInterface
    public void submitHtmlOther(String answer) {
        System.out.println("Answer from Multi Select Input Field: " + answer);
        FileLogger.log("VERBOSE", TAG, "Security Questions: User Entered: : " + answer);
        try {
            creqObject.put("challengeDataEntry", answer);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        sendCreqRequest();
    }


    @JavascriptInterface
    public void resendOTP(String baseUrl, String acsTransId) {
        System.out.println("Resend Button Clicked: " + baseUrl + SDKConstants.RESEND_OTP_URL + acsTransId);
        ApiClient apiClient = new ApiClient();
        new Thread(() -> {
            try {
                FileLogger.log("VERBOSE", TAG, "Sending Request to ACS on URL: " + baseUrl + SDKConstants.RESEND_OTP_URL + acsTransId);
                apiClient.get(baseUrl, SDKConstants.RESEND_OTP_URL + acsTransId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void sendCreqRequest() {
        try {
            String response = this.callAcsForCreq();
            if (response != null || response != "") {
                JSONObject cres = new JSONObject(response.toString());
                FileLogger.log("VERBOSE", TAG, "CRes Mapped to JSON Object: " + response);
                String acsHTML = cres.optString("acsHTML", null);
                String transResult = cres.has("transStatus") ? String.valueOf(cres.get("transStatus")) : "Not Present";
                String SdkTransId = cres.has("sdkTransID") ? String.valueOf(cres.get("sdkTransID")) : "Not Present";
                if (acsHTML != null) {
                    FileLogger.log("INFO", TAG, "HTML Render");
                    byte[] decodedBytes = Base64.getUrlDecoder().decode(acsHTML);
                    String decodedString = new String(decodedBytes);
                    Intent intent = new Intent(this.activity, HTMLRender.class);
                    intent.putExtra("htmlContent", decodedString);
                    intent.putExtra("acsURL", acsURL);
                    intent.putExtra("creqObject", creqObject.toString());
                    FileLogger.log("INFO", TAG, "HTML Rehnder: Intent Set Successfully");
                    this.activity.startActivity(intent);
                    this.activity.finish();
                } else {
                    FileLogger.log("INFO", TAG, "Not HTML Render, Will be redirected to Result Screen");
                    String counter = cres.has("acsCounterAtoS") ? cres.optString("acsCounterAtoS") : "001";
                    FileLogger.log("VERBOSE", TAG, "Transaction Status: " + transResult + " ,Counter Value: " + counter);
                    if (transResult.equalsIgnoreCase("Y")) {
                        FileLogger.log("INFO", TAG, "Transaction Successful, Redirecting to Transaction Result Screen with status: " + response.toString());
                        String transactionResult = "Success";
                        Intent intent = new Intent(this.context, TransactionResult.class);
                        intent.putExtra("transStatus", transactionResult);
                        intent.putExtra("sdkTransID", SdkTransId);
                        this.context.startActivity(intent);
                        this.activity.finish();
                    } else {
                        FileLogger.log("ERROR", TAG, "Transaction Unsuccessful, Redirecting to Payment Result Screen with status: " + response.toString());
                        String transactionResult = "Unsuccessful";
                        Intent intent = new Intent(this.context, TransactionResult.class);
                        intent.putExtra("transStatus", transactionResult);
                        intent.putExtra("sdkTransID", SdkTransId);
                        this.context.startActivity(intent);
                        this.activity.finish();
                    }
                }
            } else {
                FileLogger.log("ERROR", TAG, "No Response from ACS");
                String transactionResult = "Error Connecting to ACS";
                Intent intent = new Intent(this.context, TransactionResult.class);
                intent.putExtra("transStatus", transactionResult);
                intent.putExtra("sdkTransID", "");
                this.context.startActivity(intent);
                this.activity.finish();
            }
        } catch (JSONException ex) {
            throw new RuntimeException(ex);

        }
    }

    private String callAcsForCreq() {
        FileLogger.log("VERBOSE", TAG, "CReq Call TO: " + this.acsURL + " With CReq Body: " + this.creqObject);
        PostRequestTask postRequestCallable = new PostRequestTask(this.acsURL, "creq", new String(Base64.getEncoder().encodeToString(this.creqObject.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        String response = "";
        try {
            response = futureTask.get();
            FileLogger.log("VERBOSE", TAG, "CRes Received From ACS: " + response);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

}
