package com.example.paybizsdk.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.OTPScreen;
import com.example.paybizsdk.TransactionResult;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.exceptions.InvalidInputException;
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

public class TransactionService implements Transaction {

    private ThreeDS2Service threeDS2Service;
    private Activity activity;
    private Context context;

    private String SDKAppID;

    private String SDKEmpheralPublicKey;

    private String messageVersion;

    private JSONObject areqJson;
    DatabaseService databaseService;

    private static final String TAG = "TransactionService";

    public TransactionService() {
    }

    public TransactionService(Activity activity, Context context, String SDKAppID, String SDKEmpheralPublicKey, String messageVersion) {
        this.threeDS2Service = new ThreeDSService();
        this.activity = activity;
        this.context = context;
        this.SDKAppID = SDKAppID;
        this.SDKEmpheralPublicKey = SDKEmpheralPublicKey;
        this.messageVersion = messageVersion;
        this.databaseService = new DatabaseService(context);
    }

    @Override
    public AuthenticationRequestParameters getAuthenticationRequestParameters() {
        FileLogger.log("INFO", TAG, "--- In Get Authentication Request Parameters ---");
        return new AuthenticationRequestParameters(String.valueOf(ThreeDSService.deviceInformation), SDKConstants.SDK_TRANS_ID, this.SDKAppID, SDKConstants.SDK_REF_NUM, null, null);
    }

    @SuppressLint("Range")
    @Override
    public void doChallenge(Activity currentActivity, ChallengeParameters challengeParameters,
                            ChallengeStatusReceiver challengeStatusReceiver, int timeOut)
            throws InvalidInputException, JSONException {
        String acsURL = "";
        FileLogger.log("INFO", TAG, "--- In Do Challenge ---");
        if (this.SDKEmpheralPublicKey == null) {
            throw new RuntimeException("SDK Empheral Public Key not found");
        }
        JSONObject creqJson = new JSONObject();
        creqJson.put("threeDSServerTransID", challengeParameters.getThreeDSServerTransactionId());
        creqJson.put("acsTransID", challengeParameters.getAcsTransactionId());
        creqJson.put("challengeWindowSize", "05");
        creqJson.put("messageType", "CReq");
        creqJson.put("messageVersion", SDKConstants.Message_VERSION);
//        creqJson.put("oobContinue", "true");
        creqJson.put("resendChallenge", "N");
        creqJson.put("sdkTransID", challengeParameters.getSdkTransID());
        creqJson.put("sdkCounterStoA", "001");
        creqJson.put("whitelistingDataEntry", "Y");
        creqJson.put("oobAppURLInd", "01");
        creqJson.put("threeDSRequestorAppURL", "https://developer.android.com/training/app-links");
        System.out.println("CReq Json: " + creqJson);
        try {
            Cursor cursor = databaseService.getLastTransaction();
            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    acsURL = cursor.getString(cursor.getColumnIndex("acsURL"));
                }
            }
        } catch (Exception e) {
            FileLogger.log("ERROR", TAG, "Error in fetching ACS URL" + e.getMessage());
        }
        FileLogger.log("INFO", TAG, "CReq Prepared: " + creqJson.toString());
        FileLogger.log("INFO", TAG, "CReq Call To: " + acsURL);
        PostRequestTask postRequestCallable = new PostRequestTask(acsURL, "creq", new String(Base64.getEncoder().encodeToString(creqJson.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        FileLogger.log("INFO", TAG, "CRes Received");
        JSONObject cresObject = null;
        try {
            String response = futureTask.get();
            System.out.println("Initial CReq Response: " + response);
            if (response != null) {
                cresObject = new JSONObject(response);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        if (cresObject != null && (cresObject.has("acsUiType") &&
                cresObject.getString("acsUiType").equalsIgnoreCase("01"))) {
            Intent intent = new Intent(currentActivity, OTPScreen.class);
            intent.putExtra("header", cresObject.has("challengeInfoHeader") ? cresObject.getString("challengeInfoHeader") : "Alt Header");
            intent.putExtra("label", cresObject.has("challengeInfoLabel") ? cresObject.getString("challengeInfoLabel") : "Alt Label");
            intent.putExtra("text", cresObject.has("challengeInfoText") ? cresObject.getString("challengeInfoText") : "Alt Info Text");
            intent.putExtra("textIndicator", cresObject.has("challengeInfoTextIndicator") ? cresObject.getString("challengeInfoTextIndicator") : "N");
            intent.putExtra("creq", creqJson.toString());
            intent.putExtra("acsUrl", acsURL);
            currentActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(currentActivity, TransactionResult.class);
            intent.putExtra("transStatus", "Error while connecting ACS");
            currentActivity.startActivity(intent);
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

    public JSONObject getAreqJson() {
        return areqJson;
    }

    public void setAreqJson(JSONObject areqJson) {
        this.areqJson = areqJson;
    }
}