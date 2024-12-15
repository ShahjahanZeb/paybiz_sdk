package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.constants.MadaConstant;
import com.example.paybizsdk.models.MadaRequest;
import com.example.paybizsdk.models.SchemeSpecificData;
import com.example.paybizsdk.models.ThreeDsData;
import com.example.paybizsdk.service.DatabaseService;
import com.example.paybizsdk.utility.ApiClient;
import com.example.paybizsdk.utility.PostRequestTask;
import com.example.paybizsdk.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TransactionResult extends AppCompatActivity {

    private TextView resultText, paymentIdText, sdkTransIdText, ccNameText, currencyText, amountText,
            messageId, rrn, status, messageIdHeader, rrnHeader, statusHeader, errorDescHeader, errorDesc;
    private Button button;

    private DatabaseService databaseService;

    private final String TAG = "Transaction Result Screen";

    private final String madaURL = "https://staging.logibiztech.com:8777/mada/mada/transaction";

    private static final int TIMEOUT_DURATION = 30; // 30 seconds timeout


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);
        FileLogger.log("INFO", TAG, "Transaction Result Screen Opened");
        databaseService = new DatabaseService(this);
        resultText = findViewById(R.id.authStatus);
        button = findViewById(R.id.backButton);
        paymentIdText = findViewById(R.id.paymentId);
        sdkTransIdText = findViewById(R.id.sdkTransId);
        ccNameText = findViewById(R.id.ccName);
        currencyText = findViewById(R.id.currency);
        amountText = findViewById(R.id.amount);
        messageId = findViewById(R.id.messageId);
        rrn = findViewById(R.id.rrn);
        status = findViewById(R.id.status);
        messageIdHeader = findViewById(R.id.messageIdHeader);
        rrnHeader = findViewById(R.id.rrnHeader);
        statusHeader = findViewById(R.id.statusHeader);
        errorDescHeader = findViewById(R.id.errorDescHeader);
        errorDesc = findViewById(R.id.errorDesc);

        try {
            FileLogger.log("VERBOSE", TAG, "Getting Values from Intent, Database Saved Record and setting text dynamically on Screen");
            Intent intent = getIntent();
            String tranxResult = !intent.getStringExtra("transStatus").isEmpty() ? intent.getStringExtra("transStatus") : "Error in fetching Details";
            String sdkTransId = !intent.getStringExtra("sdkTransID").isEmpty() ? intent.getStringExtra("sdkTransID") : "N/A";
            String pId = "", amount = "", cardHolderName = "", pan = "", cvv = "", expiry = "", acsRefNum = "", dsRefNum = "",
                    acsTransID = "", merchantId = "", merchantName = "";
            Cursor cursor = databaseService.getLastTransaction();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    pId = !cursor.getString(cursor.getColumnIndex("paymentId")).isEmpty() ? cursor.getString(cursor.getColumnIndex("paymentId")) : "N/A";
                    amount = !cursor.getString(cursor.getColumnIndex("amount")).isEmpty() ? cursor.getString(cursor.getColumnIndex("amount")) : "N/A";
                    cardHolderName = !cursor.getString(cursor.getColumnIndex("cardHolderName")).isEmpty() ? cursor.getString(cursor.getColumnIndex("cardHolderName")) : "N/A";
                    pan = !cursor.getString(cursor.getColumnIndex("pan")).isEmpty() ? cursor.getString(cursor.getColumnIndex("pan")) : "";
                    cvv = !cursor.getString(cursor.getColumnIndex("cvv")).isEmpty() ? cursor.getString(cursor.getColumnIndex("cvv")) : "";
                    expiry = !cursor.getString(cursor.getColumnIndex("expiry")).isEmpty() ? cursor.getString(cursor.getColumnIndex("expiry")) : "";
                    acsRefNum = !cursor.getString(cursor.getColumnIndex("acsRefNum")).isEmpty() ? cursor.getString(cursor.getColumnIndex("acsRefNum")) : "";
                    dsRefNum = !cursor.getString(cursor.getColumnIndex("dsRefNum")).isEmpty() ? cursor.getString(cursor.getColumnIndex("dsRefNum")) : "";
                    acsTransID = !cursor.getString(cursor.getColumnIndex("acsTransID")).isEmpty() ? cursor.getString(cursor.getColumnIndex("acsTransID")) : "";
                    merchantId = !cursor.getString(cursor.getColumnIndex("merchantId")).isEmpty() ? cursor.getString(cursor.getColumnIndex("merchantId")) : "";
                    merchantName = !cursor.getString(cursor.getColumnIndex("merchantName")).isEmpty() ? cursor.getString(cursor.getColumnIndex("merchantName")) : "";
                }
            }
            System.out.println("Data in Transaction Result Screen:" + tranxResult + " , " + pId + " , " + sdkTransId);
            resultText.setText(tranxResult);
            paymentIdText.setText(pId);
            sdkTransIdText.setText(sdkTransId);
            ccNameText.setText(cardHolderName);
            currencyText.setText("USD");
            amountText.setText(amount);
            databaseService.deleteLastTransaction();
//            MadaRequest madaReq = new MadaRequest();
            if ("Success".equals(tranxResult)) {
                FileLogger.log("VERBOSE", TAG, "Transaction Successful, Calling to MADA");
//                madaReq.setTransactionType(MadaConstant.TRANSACTION_TYPE);
//                madaReq.setClientId(merchantId);
//                madaReq.setAcquiringInstitutionId(MadaConstant.ACQUIRING_INST_ID);
//                madaReq.setTerminalType(MadaConstant.TERMINAL_TYPE);
//                madaReq.setInitiatedBy(MadaConstant.INITIATED_BY);
//                madaReq.setTokenType(MadaConstant.TOKEN_TYPE);
//                madaReq.setAmount(amount);
//                madaReq.setMerchantId(merchantName);
//                madaReq.setTerminalId(MadaConstant.TERMINAL_ID);
//                madaReq.setMerchantName(merchantName);
//                madaReq.setMerchantArabicName(merchantName);
//                madaReq.setAddress(MadaConstant.ADDRESS);
//                madaReq.setCity(MadaConstant.CITY);
//                madaReq.setZipCode(MadaConstant.ZIP_CODE);
//                madaReq.setRegionCode(MadaConstant.REGION_CODE);
//                madaReq.setMcc(MadaConstant.MCC);
//                madaReq.setCountryCode(MadaConstant.COUNTRY_CODE);
//                madaReq.setCurrency(MadaConstant.CURRENCY);
//                madaReq.setMerchantReference(MadaConstant.MERCHANT_REF);
//                madaReq.setPan(pan);
//                madaReq.setCvv2(cvv);
//                madaReq.setPanExpDate(Utils.convertMadaDate(expiry));
//                madaReq.setCardBrand(Utils.getCardBrand(pan));
//                madaReq.setLocalDateTime(Utils.getMadaDate());
//                madaReq.setThreedsFlag(true);
//                // Set "udf" field with a single value
//                madaReq.setUdf(Collections.EMPTY_LIST);
//                // Create and set values for nested ThreeDsData object
//                ThreeDsData threeDsData = new ThreeDsData();
//                threeDsData.setEci("05");
//                threeDsData.setTransStatus("Y");
//                threeDsData.setAuthenticationValue("0DUzMjM1MDY50TE4NTMyMzUWNjk=");
//                threeDsData.setSli("05");
//                threeDsData.setDsTransId("e1653e9a-eb9e-4fc4-af89-ac9ca670b970");
//                threeDsData.setAcsReferenceNumber(acsRefNum);
//                threeDsData.setDsReferenceNumber(dsRefNum);
//                threeDsData.setAcsTransId(acsTransID);
//                threeDsData.setMessageVersion("2.2.0");
//                threeDsData.setThreedsTransDate(Utils.getUTCDateTime());
//                madaReq.setThreeDsData(threeDsData);
//                // Create and set values for nested SchemeSpecificData and VisaSpecificData objects
//                SchemeSpecificData schemeSpecificData = new SchemeSpecificData();
//                SchemeSpecificData.VisaSpecificData visaSpecificData = new SchemeSpecificData.VisaSpecificData();
//                visaSpecificData.setMerchantVerificationValue("MERVAL");
//                visaSpecificData.setMarketSpecificIndicator("");
//                visaSpecificData.setMerchantIdentifier("merc7771");
//                visaSpecificData.setCommercialCardTypeRequest("V");
//                visaSpecificData.setAuthorizationCharacteristicsIndicator("Y");
//                schemeSpecificData.setVisaSpecificData(visaSpecificData);
//                madaReq.setSchemeSpecificData(schemeSpecificData);
//                JSONObject jsonObject = new JSONObject(madaReq.toString());

                JSONObject madaReq = new JSONObject();

                // Set main fields
                madaReq.put("transactionType", MadaConstant.TRANSACTION_TYPE);
                madaReq.put("clientId", merchantId);
                madaReq.put("acquiringInstitutionId", MadaConstant.ACQUIRING_INST_ID);
                madaReq.put("terminalType", MadaConstant.TERMINAL_TYPE);
                madaReq.put("initiatedBy", MadaConstant.INITIATED_BY);
                madaReq.put("tokenType", MadaConstant.TOKEN_TYPE);
                madaReq.put("amount", amount);
                madaReq.put("merchantId", merchantName);
                madaReq.put("terminalId", MadaConstant.TERMINAL_ID);
                madaReq.put("merchantName", merchantName);
                madaReq.put("merchantArabicName", merchantName);
                madaReq.put("address", MadaConstant.ADDRESS);
                madaReq.put("city", MadaConstant.CITY);
                madaReq.put("zipCode", MadaConstant.ZIP_CODE);
                madaReq.put("regionCode", MadaConstant.REGION_CODE);
                madaReq.put("mcc", MadaConstant.MCC);
                madaReq.put("countryCode", MadaConstant.COUNTRY_CODE);
                madaReq.put("currency", MadaConstant.CURRENCY);
                madaReq.put("merchantReference", MadaConstant.MERCHANT_REF);
                madaReq.put("pan", pan);
                madaReq.put("cvv2", cvv);
                madaReq.put("panExpDate", Utils.convertMadaDate(expiry));
                madaReq.put("cardBrand", Utils.getCardBrand(pan));
                madaReq.put("localDateTime", Utils.getMadaDate());
                madaReq.put("threedsFlag", true);

                // Set UDF field with an empty list
                JSONArray udfArray = new JSONArray();
                udfArray.put(""); // Add an empty string to the array
                madaReq.put("udf", udfArray);

                // Create and set nested ThreeDsData object
                JSONObject threeDsData = new JSONObject();
                threeDsData.put("eci", "05");
                threeDsData.put("transStatus", "Y");
                threeDsData.put("authenticationValue", "0DUzMjM1MDY50TE4NTMyMzUWNjk=");
                threeDsData.put("sli", "05");
                threeDsData.put("dsTransId", "e1653e9a-eb9e-4fc4-af89-ac9ca670b970");
                threeDsData.put("acsReferenceNumber", acsRefNum);
                threeDsData.put("dsReferenceNumber", dsRefNum);
                threeDsData.put("acsTransId", acsTransID);
                threeDsData.put("messageVersion", "2.2.0");
                threeDsData.put("threedsTransDate", Utils.getUTCDateTime());
                madaReq.put("threeDsData", threeDsData);

                // Create and set nested SchemeSpecificData and VisaSpecificData objects
                JSONObject visaSpecificData = new JSONObject();
                visaSpecificData.put("merchantVerificationValue", "MERVAL");
                visaSpecificData.put("marketSpecificIndicator", "");
                visaSpecificData.put("merchantIdentifier", "merc7771");
                visaSpecificData.put("commercialCardTypeRequest", "V");
                visaSpecificData.put("authorizationCharacteristicsIndicator", "Y");

                JSONObject schemeSpecificData = new JSONObject();
                schemeSpecificData.put("visaSpecificData", visaSpecificData);
                madaReq.put("schemeSpecificData", schemeSpecificData);


                FileLogger.log("VERBOSE", TAG, "MADA Data Set Successfully" + madaReq.toString(4));
//                ApiClient apiClient = new ApiClient();
//                new Thread(() -> {
//                    try {
//                        FileLogger.log("VERBOSE", TAG, "Sending Request to MADA on URL: " + this.madaURL + "\n Body: " + new String(madaReq.toString()));
//                        String response = apiClient.post(this.madaURL, new String(madaReq.toString()));
//                        FileLogger.log("VERBOSE", TAG, "Response from Mada Auth: " + response);
//                        setData(response);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
                /* Correct Code
                PostRequestTask postRequestCallable = new PostRequestTask(this.madaURL, null, new String(madaReq.toString()));
                FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(futureTask);
                String response = "";
                try {
                    response = futureTask.get();
                    FileLogger.log("VERBOSE", TAG, "Response Received From MADA AUTH: " + response);
                    setData(response);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                 */

                PostRequestTask postRequestCallable = new PostRequestTask(this.madaURL, null, new String(madaReq.toString()));
                FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(futureTask);

                // Set a timeout for the network request
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!futureTask.isDone()) {
                        futureTask.cancel(true);  // Cancel the task if it hasn't completed in time
                        FileLogger.log("ERROR", TAG, "Request timed out.");
                        // Update the UI to show timeout message
                        runOnUiThread(() -> status.setText("Request timed out. Please try again."));
                    }
                }, TimeUnit.SECONDS.toMillis(TIMEOUT_DURATION));

                try {
                    // Wait for response, or timeout
                    String response = futureTask.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
                    FileLogger.log("VERBOSE", TAG, "Response Received From MADA AUTH: " + response);
                    setData(response);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    // Handle error gracefully
                    runOnUiThread(() -> status.setText("An error occurred while processing the request."));
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> status.setText("Request timed out. Please try again."));
                }
            } else {
                messageIdHeader.setVisibility(View.GONE);
                rrnHeader.setVisibility(View.GONE);
                statusHeader.setVisibility(View.GONE);
                messageId.setVisibility(View.GONE);
                rrn.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
            }

            button.setOnClickListener(v -> finish());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
        public void setData(String response) {
        FileLogger.log("VERBOSE", TAG, "Setting Mada Response on Screen");

        try {
            // Check if the response contains HTML, indicating an error page
            if (response.toLowerCase().contains("<!doctype html>") || response.toLowerCase().contains("<html>")) {
                FileLogger.log("ERROR", TAG, "Invalid response received: HTML error page detected.");
                statusHeader.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                status.setText("Error: Unable to connect to MADA service. Please try again.");
            } else {
                // Parse the response JSON
                FileLogger.log("ERROR", TAG, "Parsing MADA Response.");
                JSONObject madaResponse = new JSONObject(response);
                // Get response code type
                String madaStatus = madaResponse.optString("responseCodeType", "");

                if (!madaStatus.isEmpty()) {
                    if (madaStatus.equalsIgnoreCase("APPROVED")) {
                        // Show fields for approved status
                        messageId.setVisibility(View.VISIBLE);
                        messageIdHeader.setVisibility(View.VISIBLE);
                        rrnHeader.setVisibility(View.VISIBLE);
                        rrn.setVisibility(View.VISIBLE);
                        messageId.setText(madaResponse.optString("messageId", "N/A"));
                        rrn.setText(madaResponse.optString("retrievalRef", "N/A"));
                    } else {
                        // Show fields for error
                        errorDescHeader.setVisibility(View.VISIBLE);
                        errorDesc.setVisibility(View.VISIBLE);

                        // Extract error description
                        JSONObject errorObject = madaResponse.optJSONObject("error");
                        String description = errorObject != null
                                ? errorObject.optString("description", "Please correct your request.")
                                : "Please correct your request.";
                        errorDesc.setText(description);
                    }
                }
                statusHeader.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                status.setText(madaStatus.isEmpty() ? "Error Connection MADA Auth" : madaStatus);
            }

        } catch (JSONException e) {
            FileLogger.log("ERROR", TAG, "JSON Parsing Error: " + e.getMessage());
            status.setText("Error: Invalid response format received. Please try again.");
        }
    }

    // Utility method to show multiple views
    private void showView(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

}