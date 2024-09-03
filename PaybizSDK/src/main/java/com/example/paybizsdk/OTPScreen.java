package com.example.paybizsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.utility.PostRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class OTPScreen extends AppCompatActivity {

    private static final String TAG = "OTPScreen";
    private TextView challengeInfoHeader, challengeInfoLabel, challengeInfoText, fieldError;
    private EditText otpInput;
    private Button submitButton, backButton;

    private String acsUrl = "";
    JSONObject creqJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
        challengeInfoLabel = findViewById(R.id.challengeInfoLabel);
        challengeInfoText = findViewById(R.id.challengeInfoText);
        otpInput = findViewById(R.id.otpInput);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        fieldError = findViewById(R.id.fieldError);
        fieldError.setTextColor(Color.WHITE);
        Intent intent = getIntent();
        if (intent != null) {
            String header = intent.getStringExtra("header");
            String label = intent.getStringExtra("label");
            String text = intent.getStringExtra("text");
            String textIndicator = intent.getStringExtra("text");
            if (header != null) challengeInfoHeader.setText(header);
            if (label != null) challengeInfoLabel.setText(label);
            if (text != null && textIndicator.equalsIgnoreCase("Y")) challengeInfoText.setText(text);
        }
        acsUrl = intent.getStringExtra("acsUrl");
        try {
            this.creqJson = new JSONObject(intent.getStringExtra("creq").toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpInput.getText().toString().trim();
                if (otp.isEmpty()) {
                    otpInput.setError("Please enter the OTP");
                    return;
                }
                FileLogger.log("INFO", TAG, "OTP Entered By USER: " + otp);
                try {
                    creqJson.put("challengeDataEntry", otp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                sendCreqRequest();
            }
        });
    }

    public void sendCreqRequest() {
        FileLogger.log("INFO", TAG, "CREQ Received: " + this.creqJson);
        try {
            String response = this.callAcsForCreq();
            System.out.println("CRes Response from Method: " + response);
            if (response != null || response != "") {
                JSONObject cres = new JSONObject(response.toString());
                FileLogger.log("INFO", TAG, "CRes Received and is Not Null: " + response);
                String transResult = cres.has("transStatus") ? String.valueOf(cres.get("transStatus")) : null;
                String SdkTransId = cres.has("sdkTransID") ? String.valueOf(cres.get("sdkTransID")) : null;
                String counter = cres.has("acsCounterAtoS") ? cres.optString("acsCounterAtoS") : "001";
                FileLogger.log("INFO", TAG, "Transaction Status: " + transResult + " ,Counter Value: " + counter);
                if (transResult.equalsIgnoreCase("Y")) {
                    FileLogger.log("INFO", TAG, "Transaction Successfull: " + response.toString());
                    String transactionResult = "Payment Success";
                    Intent intent = new Intent(this, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    intent.putExtra("sdkTransID", SdkTransId);
                    startActivity(intent);
                    finish();
                } else if (!transResult.equalsIgnoreCase("Y") && Integer.parseInt(counter) < 010) {
                    fieldError.setTextColor(Color.RED);
                    FileLogger.log("INFO", TAG, "Transaction Counter: " + this.creqJson);
                    int count = Integer.parseInt(counter);
                    count++;
                    System.out.println("\n\nCount after Increment: " + count);
                    if (count < 10) {
                        this.creqJson.put("sdkCounterStoA", "00" + count);
                    } else {
                        this.creqJson.put("sdkCounterStoA", "0" + count);
                    }
                    FileLogger.log("INFO", TAG, "Transaction Counter After Increment: " + this.creqJson);
                } else {
                    FileLogger.log("INFO", TAG, "Transaction Unsuccessful: " + response.toString());
                    String transactionResult = "Payment Unsuccessful";
                    Intent intent = new Intent(this, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    startActivity(intent);
                    finish();
                }
            } else {
                FileLogger.log("ERROR", TAG, "No Response");
                String transactionResult = "Error in Connection ACS";
                Intent intent = new Intent(this, TransactionResult.class);
                intent.putExtra("transStatus", transactionResult);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String callAcsForCreq() {
        FileLogger.log("INFO", TAG, "CReq Call TO: " + this.acsUrl + " With Body: " + this.creqJson);
        PostRequestTask postRequestCallable = new PostRequestTask(this.acsUrl, "creq", new String(Base64.getEncoder().encodeToString(this.creqJson.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        String response = null;
        try {
            response = futureTask.get();
            System.out.println("Final CRes Response: " + response);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }
}
