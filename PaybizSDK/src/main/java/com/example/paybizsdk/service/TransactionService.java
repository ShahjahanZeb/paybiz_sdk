package com.example.paybizsdk.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.screens.OOBConfirmationScreen;
import com.example.paybizsdk.screens.OTPScreen;
import com.example.paybizsdk.screens.TransactionResult;
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

    private ChallengeParameters parameters;

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
        this.parameters = challengeParameters;
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
//        creqJson.put("oobAppURLInd", "01");
        creqJson.put("threeDSRequestorAppURL", "oobApp://localhost/path");
        System.out.println("CReq Json: " + creqJson);
        try {
            Cursor cursor = databaseService.getLastTransaction();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
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
        FileLogger.log("INFO", TAG, "CReq Called");
        JSONObject cresObject = null;
        try {
            String response = futureTask.get();
            if (response != null) {
                FileLogger.log("INFO", TAG, "CRes Received" + response);
                cresObject = new JSONObject(response);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        if (cresObject != null) {
            if (cresObject.has("acsUiType") &&
                    cresObject.getString("acsUiType").equalsIgnoreCase("01")) {
                FileLogger.log("INFO", TAG, "UI TYPE = '01', OTP Case");
                Intent intent = new Intent(currentActivity, OTPScreen.class);
                intent.putExtra("challengeInfoHeader", cresObject.has("challengeInfoHeader")
                        ? cresObject.getString("challengeInfoHeader") : "");
                intent.putExtra("challengeInfoLabel", cresObject.has("challengeInfoLabel")
                        ? cresObject.getString("challengeInfoLabel") : "");
                intent.putExtra("challengeInfoText", cresObject.has("challengeInfoText")
                        ? cresObject.getString("challengeInfoText") : "");
                intent.putExtra("challengeInfoTextIndicator", cresObject.has("challengeInfoTextIndicator")
                        ? cresObject.getString("challengeInfoTextIndicator") : "N");
                // New fields
                intent.putExtra("submitAuthenticationLabel", cresObject.has("submitAuthenticationLabel")
                        ? cresObject.getString("submitAuthenticationLabel") : "Submit");
                intent.putExtra("whyInfoLabel", cresObject.has("whyInfoLabel")
                        ? cresObject.getString("whyInfoLabel") : "");
                intent.putExtra("expandInfoLabel", cresObject.has("expandInfoLabel")
                        ? cresObject.getString("expandInfoLabel") : "");
                intent.putExtra("creq", creqJson.toString());
                intent.putExtra("acsUrl", acsURL);
                if (cresObject.has("issuerImage")) {
                    JSONObject issuerImageObject = cresObject.getJSONObject("issuerImage");
                    intent.putExtra("issuerImage", issuerImageObject.has("medium") && !issuerImageObject.getString("medium").isEmpty()
                            ? issuerImageObject.getString("medium") : "");
                } else {
                    intent.putExtra("issuerImage", "https://i.ibb.co/3k6GPqc/logibiz-logo-300x-1.png");
                }
                if (cresObject.has("psImage")) {
                    JSONObject issuerImageObject = cresObject.getJSONObject("psImage");
                    intent.putExtra("psImage", issuerImageObject.has("medium") && !issuerImageObject.getString("medium").isEmpty()
                            ? issuerImageObject.getString("medium") : "");
                } else {
                    intent.putExtra("psImage", "https://i.ibb.co/Fw9Gtrd/Picture1.png");
                }
                currentActivity.startActivity(intent);
            } else if (cresObject.has("acsUiType") &&
                    cresObject.getString("acsUiType").equalsIgnoreCase("04")) {
                FileLogger.log("INFO", TAG, "UI TYPE = '04', OOB Case");


                Intent intent = new Intent(currentActivity, OOBConfirmationScreen.class);
                intent.putExtra("challengeInfoHeader", cresObject.has("challengeInfoHeader")
                        ? cresObject.getString("challengeInfoHeader") : "");

                intent.putExtra("challengeInfoText", cresObject.has("challengeInfoText")
                        ? cresObject.getString("challengeInfoText") : "");

                intent.putExtra("oobAppLabel", cresObject.has("oobAppLabel")
                        ? cresObject.getString("oobAppLabel") : "OPEN YOUR BANK APP");

                intent.putExtra("oobAppUrl", "oobApp://localhost/path");

                intent.putExtra("whyInfoLabel", cresObject.has("whyInfoLabel")
                        ? cresObject.getString("whyInfoLabel") : "");

                intent.putExtra("expandInfoLabel", cresObject.has("expandInfoLabel")
                        ? cresObject.getString("expandInfoLabel") : "");

                intent.putExtra("threeDSServerTransID", !challengeParameters.getThreeDSServerTransactionId().isEmpty()
                        ? challengeParameters.getThreeDSServerTransactionId() : "N/A");

                if (cresObject.has("issuerImage")) {
                    JSONObject issuerImageObject = cresObject.getJSONObject("issuerImage");
                    System.out.println("Issuer Image: " + issuerImageObject.getString("medium"));
                    intent.putExtra("issuerImage", issuerImageObject.has("medium") && !issuerImageObject.getString("medium").isEmpty()
                            ? issuerImageObject.getString("medium") : "https://i.ibb.co/3k6GPqc/logibiz-logo-300x-1.png");
                } else {
                    intent.putExtra("issuerImage", "https://i.ibb.co/3k6GPqc/logibiz-logo-300x-1.png");
                }

                if (cresObject.has("psImage")) {
                    JSONObject psImageObject = cresObject.getJSONObject("psImage");
                    System.out.println("PS Image: " + psImageObject.getString("medium"));
                    intent.putExtra("psImage", psImageObject.has("medium") && !psImageObject.getString("medium").isEmpty()
                            ? psImageObject.getString("medium") : "https://i.ibb.co/Fw9Gtrd/Picture1.png");
                } else {
                    intent.putExtra("psImage", "https://i.ibb.co/Fw9Gtrd/Picture1.png");
                }
                currentActivity.startActivity(intent);
            }
        } else {
            FileLogger.log("ERROR", TAG, "Error Case, Redirecting to Transaction Result Screen with Error");
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

    @SuppressLint("Range")
    public void sendOOBFinalCReq(Activity currentActivity) {
        this.getProgressView(currentActivity).show();
        JSONObject creqJson = new JSONObject();
        try {
            creqJson.put("threeDSServerTransID", this.parameters.getThreeDSServerTransactionId());
            creqJson.put("acsTransID", this.parameters.getAcsTransactionId());
            creqJson.put("challengeWindowSize", "05");
            creqJson.put("messageType", "CReq");
            creqJson.put("messageVersion", SDKConstants.Message_VERSION);
            creqJson.put("oobContinue", "true");
            creqJson.put("resendChallenge", "N");
            creqJson.put("sdkTransID", this.parameters.getSdkTransID());
            creqJson.put("sdkCounterStoA", "001");
            creqJson.put("whitelistingDataEntry", "Y");
            creqJson.put("oobContinue", "true");
        } catch (JSONException e) {
            this.getProgressView(currentActivity).hide();
            throw new RuntimeException(e);
        }
        String acsURL = "";
        try {
            Cursor cursor = databaseService.getLastTransaction();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    acsURL = cursor.getString(cursor.getColumnIndex("acsURL"));
                }
            }
        } catch (Exception e) {
            this.getProgressView(currentActivity).hide();
            FileLogger.log("ERROR", TAG, "Error in fetching ACS URL" + e.getMessage());
        }
        FileLogger.log("INFO", TAG, "CReq Prepared: " + creqJson.toString());
        FileLogger.log("INFO", TAG, "CReq Call To: " + acsURL);
        PostRequestTask postRequestCallable = new PostRequestTask(acsURL, "creq", new String(Base64.getEncoder().encodeToString(creqJson.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        FileLogger.log("INFO", TAG, "CReq Called");
        JSONObject cresObject = null;
        try {
            String response = futureTask.get();
            if (response != null) {
                FileLogger.log("INFO", TAG, "CRes Received" + response);
                cresObject = new JSONObject(response);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            this.getProgressView(currentActivity).hide();
            e.printStackTrace();
        }
        this.getProgressView(currentActivity).hide();
        try {
            if (cresObject != null) {
                String transResult = cresObject.has("transStatus") ? String.valueOf(cresObject.get("transStatus")) : "Not Present";
                String sdkTransId = cresObject.has("sdkTransID") ? String.valueOf(cresObject.get("sdkTransID")) : "Not Present";
                if (transResult.equalsIgnoreCase("Y")) {
                    FileLogger.log("INFO", TAG, "Transaction Successful, Redirecting to Transaction Result Screen with status: " + cresObject.toString());
                    String transactionResult = "Payment Success";
                    Intent intent = new Intent(currentActivity, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    intent.putExtra("sdkTransID", sdkTransId);
                    currentActivity.startActivity(intent);
                    currentActivity.finish();
                } else {
                    FileLogger.log("INFO", TAG, "Transaction Unsuccessful, Redirecting to Transaction Result Screen with status: " + cresObject.toString());
                    String transactionResult = "Payment Unsuccessful";
                    Intent intent = new Intent(currentActivity, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    intent.putExtra("sdkTransID", sdkTransId);
                    currentActivity.startActivity(intent);
                    currentActivity.finish();
                }
            } else {
                FileLogger.log("ERROR", TAG, "Error Case, Redirecting to Transaction Result Screen with Error");
                Intent intent = new Intent(currentActivity, TransactionResult.class);
                intent.putExtra("transStatus", "Error while connecting ACS");
                currentActivity.startActivity(intent);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}