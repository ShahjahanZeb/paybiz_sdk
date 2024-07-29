package com.example.paybizsdk.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.entity.WebViewFragment;
import com.example.paybizsdk.exceptions.InvalidInputException;
import com.example.paybizsdk.exceptions.SDKRuntimeException;
import com.example.paybizsdk.interfaces.ThreeDS2Service;
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.utility.PostRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionService implements Transaction {

    private ThreeDS2Service threeDS2Service;
    private Activity activity;
    private Context context;

    private String SDKAppID;

    private String SDKEmpheralPublicKey;

    private String messageVersion;

    private JSONObject aresJson;

    private static final String TAG = "TransactionService";

    public TransactionService(Activity activity, Context context, String SDKAppID, String SDKEmpheralPublicKey, String messageVersion) {
        this.threeDS2Service = new ThreeDSService();
        this.activity = activity;
        this.context = context;
        this.SDKAppID = SDKAppID;
        this.SDKEmpheralPublicKey = SDKEmpheralPublicKey;
        this.messageVersion = messageVersion;
    }

    @Override
    public AuthenticationRequestParameters getAuthenticationRequestParameters() {
        FileLogger.log("INFO", TAG, "--- In Get Authentication Request Parameters ---");
        return new AuthenticationRequestParameters(String.valueOf(ThreeDSService.deviceInformation), SDKConstants.SDK_TRANS_ID, this.SDKAppID, SDKConstants.SDK_REF_NUM, null, null);
    }

    @Override
    public void doChallenge(Activity currentActivity, ChallengeParameters challengeParameters,
                            ChallengeStatusReceiver challengeStatusReceiver, int timeOut)
            throws InvalidInputException, JSONException {
        FileLogger.log("INFO", TAG, "--- In Do Challenge ---");
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Will call Timeout Method
//                System.out.println("Session Expired");
//            }
//        }, timeOut);
        if (this.SDKEmpheralPublicKey == null) {
            throw new RuntimeException("SDK Empheral Public Key not found");
        }
        String acsURL = aresJson.getString("acsURL");
        FileLogger.log("INFO", TAG , "ACS URL:\t"+acsURL);
        JSONObject creqJson = new JSONObject();
        creqJson.put("threeDSServerTransID", challengeParameters.getThreeDSServerTransactionId());
        creqJson.put("acsTransID", challengeParameters.getAcsTransactionId());
        creqJson.put("challengeWindowSize", "01");
        creqJson.put("messageType", "CReq");
        creqJson.put("messageVersion", "2.2.0");
        creqJson.put("oobContinue", false);
        String jsonString = creqJson.toString();
        FileLogger.log("INFO", TAG , "CREQ:\t"+jsonString);
        byte[] jsonBytes = jsonString.getBytes();
        FileLogger.log("INFO", TAG , "CREQ SENT");
        PostRequestTask postRequestCallable = new PostRequestTask(acsURL, "creq", new String(Base64.getEncoder().encodeToString(jsonBytes)));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        String updatedResponse = null;
        FileLogger.log("INFO", TAG , "CREQ REC");
        try {
            String response = futureTask.get();
            FileLogger.log("INFO", TAG , "RESPONSE:\t"+response);
            String baseUrl = "https://ddds.logibiztech.com:7000";
            String regex = "action=\"(/acs2x/1/api/v2/process/challenge/[a-zA-Z0-9-]+/final220)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response);
            if(matcher.find()){
                updatedResponse = matcher.replaceAll("action=\"" + baseUrl + "$1\"");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        FileLogger.log("INFO", TAG , "UPDATED RESPONSE:\t"+updatedResponse);
        if(updatedResponse != null && updatedResponse.startsWith("<!DOCTYPE")) {
            FileLogger.log("INFO", TAG , "IN WEB VIEW");
            WebViewFragment webViewWrapper = new WebViewFragment(this.context);
            webViewWrapper.loadHtml(updatedResponse);
            currentActivity.setContentView(webViewWrapper);
            final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Will call Timeout Method
                String htmlCode = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Transaction Result</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            background-color:white; /* Light gray background */\n" +
                        "            margin: 0;\n" +
                        "            padding: 20px;\n" +
                        "            font-family: sans-serif;\n" +
                        "        }\n" +
                        "        .heading {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: bold;\n" +
                        "            color: black; /* Dark gray text */\n" +
                        "            margin-bottom: 10px;\n" +
                        "        }\n" +
                        "        .subheading {\n" +
                        "            font-size: 18px;\n" +
                        "            color: black; /* Even darker gray text */            \n" +
                        "            font-weight: bold;\n" +
                        "        }\n" +
                        "        .data {\n" +
                        "            font-size: 14px;\n" +
                        "            color: #313030;\n" +
                        "        }\n" +
                        "        .button {\n" +
                        "            background-color: #005A7F; /* Dark gray button */\n" +
                        "            color: #fff; /* White text */\n" +
                        "            padding: 10px 20px;\n" +
                        "            width: 100%; \n" +
                        "            border: none;\n" +
                        "            border-radius: 5px;\n" +
                        "            cursor: pointer;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <h1 class=\"heading\">Transaction Result</h1>\n" +
                        "    <br>\n" +
                        "    <span class=\"subheading\">Payment Id:</span>\n" +
                        "    <br>\n" +
                        "    <span class=\"data\">824116179350001</span>\n" +
                        "    <br><br>\n" +
                        "    <span class=\"subheading\">Card Holder Name:</span>\n" +
                        "    <br>\n" +
                        "    <span class=\"data\">shah</span>\n" +
                        "    <br><br>\n" +
                        "    <span class=\"subheading\">Currency:</span>\n" +
                        "    <br>\n" +
                        "    <span class=\"data\">840</span>\n" +
                        "    <br><br>\n" +
                        "    <span class=\"subheading\">Amount:</span>\n" +
                        "    <br>\n" +
                        "    <span class=\"data\">555</span>\n" +
                        "    <br><br>\n" +
                        "    <span class=\"subheading\">Result:</span>\n" +
                        "    <br>\n" +
                        "    <span class=\"data\">Payment Successful!</span>\n" +
                        "    <br><br>\n" +
                        "    <button class=\"button\">Back</button>\n" +
                        "</body>\n" +
                        "</html>";
                webViewWrapper.loadHtml(htmlCode);
                currentActivity.setContentView(webViewWrapper);
            }
        }, 12000);

        }
    }

    @Override
    public ProgressDialog getProgressView(Activity currentActivity) {
        FileLogger.log("INFO", TAG, "--- In Get Progress View ---");
        return new ProgressDialog(currentActivity);
    }

    @Override
    public void close() {
        threeDS2Service.cleanup(this.context);
    }

    public JSONObject getAresJson() {
        return aresJson;
    }

    public void setAresJson(JSONObject aresJson) {
        this.aresJson = aresJson;
    }

}
