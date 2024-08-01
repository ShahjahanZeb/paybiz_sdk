package com.example.paybizsdk;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
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
    private TextView challengeInfoHeader, challengeInfoLabel, challengeInfoText;
    private EditText otpInput;
    private Button submitButton;

    private String creqString = "";

    private String acsUrl = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);

        challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
        challengeInfoLabel = findViewById(R.id.challengeInfoLabel);
        challengeInfoText = findViewById(R.id.challengeInfoText);
        otpInput = findViewById(R.id.otpInput);
        submitButton = findViewById(R.id.submitButton);

        Intent intent = getIntent();
        if (intent != null) {
            String header = intent.getStringExtra("header");
            String label = intent.getStringExtra("label");
            String text = intent.getStringExtra("text");


            if (header != null) challengeInfoHeader.setText(header);
            if (label != null) challengeInfoLabel.setText(label);
            if (text != null) challengeInfoText.setText(text);
        }

        creqString = intent.getStringExtra("creq");
        acsUrl = intent.getStringExtra("acsUrl");

        System.out.println("Creq: "+ creqString);
        System.out.println("ACS Url: "+ acsUrl);
        JSONObject creqJson;
        try {
             creqJson = new JSONObject(creqString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpInput.getText().toString().trim();
                if (otp.isEmpty()) {
                    otpInput.setError("Please enter the OTP");
                    return;
                }

                System.out.println("OTP from Input Field: " + otp);

                try {
                    creqJson.put("challengeDataEntry", otp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("challengeDataEntry added in Creq: "+ creqJson);
                sendCreqRequest(creqJson, acsUrl);
//                finish();
            }
        });
    }

    public void sendCreqRequest(JSONObject creqJSON, String acsUrl){
        TransactionService transactionService = new TransactionService();
        progressDialog = transactionService.getProgressView(this);
        progressDialog.show();
        System.out.println("I am in Send Creq Request: "+ creqJSON+"\n"+acsUrl);
        PostRequestTask postRequestCallable = new PostRequestTask(acsUrl, "creq", new String(Base64.getEncoder().encodeToString(creqJSON.toString().getBytes())));
        FutureTask<String> futureTask = new FutureTask<>(postRequestCallable);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        FileLogger.log("INFO", TAG , "CREQ REC");
        try {
            String response = futureTask.get();
            System.out.println("Initial CReq Response: "+response);
            if(response != null || response != ""){
                JSONObject cres = new JSONObject(response.toString());
                String transactionResult = (cres.getString("transStatus") != null &&
                        cres.getString("transStatus") != "" && cres.getString("transStatus") == "Y")? "Payment Success":
                        "Payment UnSuccessful";
                Intent intent = new Intent(this, TransactionResult.class);
                intent.putExtra("transStatus", transactionResult);
                finish();
                progressDialog.hide();
                startActivity(intent);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
