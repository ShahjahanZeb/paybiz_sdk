package com.example.paybizsdk.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.OTPScreen;
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

    private static final String TAG = "TransactionService";

    public TransactionService(){}

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
        if (this.SDKEmpheralPublicKey == null) {
            throw new RuntimeException("SDK Empheral Public Key not found");
        }
        JSONObject areq = this.getAreqJson();
        String acsURL = areq.getString("acsURL");

        JSONObject creqJson = new JSONObject();
        creqJson.put("threeDSServerTransID", challengeParameters.getThreeDSServerTransactionId());
        creqJson.put("acsTransID", challengeParameters.getAcsTransactionId());
        creqJson.put("challengeNoEntry", "false");
        creqJson.put("challengeWindowSize", "05");
        creqJson.put("messageType", "CReq");
        creqJson.put("messageVersion", "2.2.0");
        creqJson.put("oobContinue", "false");
        creqJson.put("resendChallenge", "N");
        creqJson.put("sdkTransID", areq.getString("sdkTransID"));
        creqJson.put("sdkCounterStoA", "05");
        creqJson.put("whitelistingDataEntry", "SampleWhitelistEntry");
        System.out.println("Creq Json: "+creqJson);
        PostRequestTask postRequestCallable = new PostRequestTask(acsURL, "creq", new String(Base64.getEncoder().encodeToString(creqJson.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        FileLogger.log("INFO", TAG , "CREQ REC");
        try {
            String response = futureTask.get();
            System.out.println("Initial CReq Response: "+response);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(currentActivity, OTPScreen.class);
        intent.putExtra("header", "LOGIBIZ");
        intent.putExtra("label", "OTP Screen");
        intent.putExtra("text", "Enter OTP Screen");
        intent.putExtra("creq", creqJson.toString());
        intent.putExtra("acsUrl", acsURL);
        currentActivity.startActivity(intent);
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