package com.example.paybizsdk.service;

import android.app.Activity;
import android.content.Context;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.interfaces.ThreeDS2Service;
import com.example.paybizsdk.interfaces.Transaction;

public class TransactionService implements Transaction {

    private ThreeDS2Service threeDS2Service;
    private Activity activity;
    private Context context;

    private String SDKAppID;

    private String SDKEmpheralPublicKey;

    private String messageVersion;

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
        return new AuthenticationRequestParameters(String.valueOf(ThreeDSService.deviceInformation), SDKConstants.SDK_TRANS_ID, this.SDKAppID,  SDKConstants.SDK_REF_NUM, null, null);
    }

    @Override
    public void doChallenge(Activity currentActivity, ChallengeParameters challengeParameters, ChallengeStatusReceiver challengeStatusReceiver, int timeOut) {

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

}
