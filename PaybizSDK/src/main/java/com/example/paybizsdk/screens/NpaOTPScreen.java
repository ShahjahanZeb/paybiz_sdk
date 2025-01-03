package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.utility.ApiClient;
import com.example.paybizsdk.utility.PostRequestTask;
import com.example.paybizsdk.utility.SDKCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class NpaOTPScreen extends AppCompatActivity {

    private static final String TAG = "NPAOTPScreen";
    private TextView challengeInfoHeader, challengeInfoLabel, challengeInfoText,
            whyInfo, whyInfoLabelArrow, resendButton, expandInfoLabel, expandInfoLabelArrow;
    private EditText otpInput;
    private Button submitButton, backButton;

    private ImageView warningImage;

    private String acsUrl = "";
    JSONObject creqJson = null;

    RadioButton radioYes, radioNo;
    RadioGroup radioButtons;
    int resendCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npa_otpscreen);

        FileLogger.log("INFO", TAG, "NPA OTP Screen Opened");
        challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
        challengeInfoLabel = findViewById(R.id.challengeInfoLabel);
        challengeInfoText = findViewById(R.id.challengeInfoText);
        otpInput = findViewById(R.id.otpInput);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        challengeInfoText.setTextColor(Color.BLACK);
        whyInfo = findViewById(R.id.whyInfoLabel);
        warningImage = findViewById(R.id.warning);
        whyInfoLabelArrow = findViewById(R.id.whyInfoLabelArrow);
        resendButton = findViewById(R.id.resendButton);
        expandInfoLabel = findViewById(R.id.expandInfoLabel);
        expandInfoLabelArrow = findViewById(R.id.expandInfoLabelArrow);
        Intent intent = getIntent();
        String acsTransId = "";
        if (intent != null) {
            FileLogger.log("INFO", TAG, "Fetching Intent Values");
            String header = intent.getStringExtra("challengeInfoHeader");
            String label = intent.getStringExtra("challengeInfoLabel");
            String text = intent.getStringExtra("challengeInfoText");
            String textIndicator = intent.getStringExtra("challengeInfoTextIndicator");
            String issuerImageContent = intent.getStringExtra("issuerImage");
            String psImage = intent.getStringExtra("psImage");
            String submitAuthenticationLabel = intent.getStringExtra("submitAuthenticationLabel");
            String whyInfoLabel = intent.getStringExtra("whyInfoLabel");
            acsUrl = intent.getStringExtra("acsUrl");
            acsTransId = intent.getStringExtra("acsTransID");
            FileLogger.log("VERBOSE", TAG, "Values stored in Variables");
            if (header != null) challengeInfoHeader.setText(header);
            if (label != null) challengeInfoLabel.setText(label);
            if (text != null && textIndicator.equalsIgnoreCase("Y")) {
                String updatedText = text.replace("\\n\\n", "\n");
                challengeInfoText.setText(updatedText);
                challengeInfoText.setLineSpacing(5, 1f);
            }
            whyInfo.setText(whyInfoLabel);
            if (whyInfoLabel.isEmpty() || whyInfoLabel.equals("") || whyInfoLabel == null) {
                whyInfoLabelArrow.setText("");
            }
            submitButton.setText(submitAuthenticationLabel);
            System.out.println("\n\nImages URL: " + issuerImageContent + "\n" + psImage);
            FileLogger.log("VERBOSE", TAG, "Fetched Intent Values, Set Text Dynamically on Screen");
            expandInfoLabel.setVisibility(View.INVISIBLE);
            expandInfoLabelArrow.setVisibility(View.INVISIBLE);
        }
        try {
            FileLogger.log("VERBOSE", TAG, "Mapping CReq String to JSON Object");
            this.creqJson = new JSONObject(intent.getStringExtra("creq").toString());
            FileLogger.log("VERBOSE", TAG, "Mapping Done");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        backButton.setOnClickListener(v -> finish());

        submitButton.setOnClickListener(v -> {
            String otp = otpInput.getText().toString().trim();
            if (otp.isEmpty()) {
                otpInput.setError("Please enter the OTP");
                return;
            }
            FileLogger.log("VERBOSE", TAG, "OTP Entered By USER: " + otp);
            try {
                creqJson.put("challengeDataEntry", otp);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            sendCreqRequest();
        });

        String finalAcsTransId = acsTransId;
        resendButton.setOnClickListener(v -> {
            resendCount++;
            if (resendCount >= 5) {
                resendButton.setVisibility(View.INVISIBLE);
            }
            System.out.println("Resend Button Clicked: " + acsUrl + SDKConstants.RESEND_OTP_URL + finalAcsTransId);
            ApiClient apiClient = new ApiClient();
            new Thread(() -> {
                try {
                    FileLogger.log("VERBOSE", TAG, "Sending Request to ACS on URL: " + acsUrl + SDKConstants.RESEND_OTP_URL + finalAcsTransId);
                    apiClient.get(acsUrl, SDKConstants.RESEND_OTP_URL + finalAcsTransId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });

    }

    public void sendCreqRequest() {
        try {
            String response = this.callAcsForCreq();
            if (response != null || response != "") {
                JSONObject cres = new JSONObject(response.toString());
                FileLogger.log("VERBOSE", TAG, "CRes Mapped to JSON Object: " + response);
                String transResult = cres.has("transStatus") ? String.valueOf(cres.get("transStatus")) : "Not Present";
                String SdkTransId = cres.has("sdkTransID") ? String.valueOf(cres.get("sdkTransID")) : "Not Present";
                String threeDSServerTransID = cres.has("threeDSServerTransID") ? String.valueOf(cres.get("threeDSServerTransID")) : "";
                String acsTransID = cres.has("acsTransID") ? String.valueOf(cres.get("acsTransID")) : "";
                String counter = cres.has("acsCounterAtoS") ? cres.optString("acsCounterAtoS") : "001";
                FileLogger.log("VERBOSE", TAG, "Transaction Status: " + transResult + " ,Counter Value: " + counter);
                if (transResult.equalsIgnoreCase("Y") && cres.has("transStatus")) {
                    FileLogger.log("INFO", TAG, "Transaction Successful, Redirecting to Transaction Result Screen with status: " + response.toString());
                    SDKCall.triggerEvent(this, threeDSServerTransID, acsTransID, SdkTransId, transResult);
                    finish();
                } else if (transResult.equalsIgnoreCase("N") && cres.has("transStatus")) {
                    FileLogger.log("INFO", TAG, "Transaction Not Successful, Redirecting to Transaction Result Screen with status: " + response.toString());
                    SDKCall.triggerEvent(this, threeDSServerTransID, acsTransID, SdkTransId, transResult);
                    finish();
                } else if (cres.has("challengeInfoText")) {
                    FileLogger.log("INFO", TAG, "Invalid OTP Entered by User");
                    challengeInfoText.setText(cres.optString("challengeInfoText", ""));
                    warningImage.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_warning_round_foreground));
                } else {
                    FileLogger.log("ERROR", TAG, "Transaction Unsuccessful, Redirecting to Payment Result Screen with status: " + response.toString());
                    SDKCall.triggerEvent(this, threeDSServerTransID, acsTransID, SdkTransId, transResult);
                    finish();
                }
            } else {
                FileLogger.log("ERROR", TAG, "No Response from ACS");
                String transactionResult = "Error Connecting to ACS";
                SDKCall.triggerEvent(this, "", "", "", "Not able to connect to ACS");
                finish();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String callAcsForCreq() {
        FileLogger.log("VERBOSE", TAG, "CReq Call TO: " + this.acsUrl + " With CReq Body: " + this.creqJson);
        PostRequestTask postRequestCallable = new PostRequestTask(this.acsUrl, "creq", new String(Base64.getEncoder().encodeToString(this.creqJson.toString().getBytes())));
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