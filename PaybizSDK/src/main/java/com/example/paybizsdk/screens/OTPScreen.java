package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.utility.ApiClient;
import com.example.paybizsdk.utility.PostRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class OTPScreen extends AppCompatActivity {

    private static final String TAG = "OTPScreen";
    private TextView challengeInfoHeader, challengeInfoLabel, challengeInfoText, whyInfo, whyInfoLabelArrow, resendButton;
    private EditText otpInput;
    private Button submitButton, backButton;

    private ImageView issuerImage, paymentScheme, warningImage;

    private String acsUrl = "";
    JSONObject creqJson = null;

    int resendCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        FileLogger.log("INFO", TAG, "OTP Screen Opened");
        challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
        challengeInfoLabel = findViewById(R.id.challengeInfoLabel);
        challengeInfoText = findViewById(R.id.challengeInfoText);
        otpInput = findViewById(R.id.otpInput);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        challengeInfoText.setTextColor(Color.BLACK);
//        challengeInfoText.setTextSize(12f);
        issuerImage = findViewById(R.id.paymentLogo);
        paymentScheme = findViewById(R.id.imageView);
        whyInfo = findViewById(R.id.whyInfoLabel);
        warningImage = findViewById(R.id.warning);
        whyInfoLabelArrow = findViewById(R.id.whyInfoLabelArrow);
        resendButton = findViewById(R.id.resendButton);
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
            new DownloadImagesTask(issuerImage, paymentScheme).execute(issuerImageContent, psImage);
            FileLogger.log("VERBOSE", TAG, "Fetched Intent Values, Set Text Dynamically on Screen");
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
                String counter = cres.has("acsCounterAtoS") ? cres.optString("acsCounterAtoS") : "001";
                FileLogger.log("VERBOSE", TAG, "Transaction Status: " + transResult + " ,Counter Value: " + counter);
                if (transResult.equalsIgnoreCase("Y")) {
                    FileLogger.log("INFO", TAG, "Transaction Successful, Redirecting to Transaction Result Screen with status: " + response.toString());
                    String transactionResult = "Success";
                    Intent intent = new Intent(this, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    intent.putExtra("sdkTransID", SdkTransId);
                    startActivity(intent);
                    finish();
                } else if (!transResult.equalsIgnoreCase("Y") && Integer.parseInt(counter) < 010) {
                    challengeInfoText.setText("The code you entered is incorrect please try again.");
                    warningImage.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_warning_round_foreground));
                    FileLogger.log("INFO", TAG, "Invalid OTP Entered by User");
                    int count = Integer.parseInt(counter);
                    count++;
                    if (count < 10) {
                        this.creqJson.put("sdkCounterStoA", "00" + count);
                    } else {
                        this.creqJson.put("sdkCounterStoA", "0" + count);
                    }
                    FileLogger.log("VERBOSE", TAG, "Transaction Counter After Increment: " + this.creqJson);
                } else {
                    FileLogger.log("ERROR", TAG, "Transaction Unsuccessful, Redirecting to Payment Result Screen with status: " + response.toString());
                    String transactionResult = "Unsuccessful";
                    Intent intent = new Intent(this, TransactionResult.class);
                    intent.putExtra("transStatus", transactionResult);
                    startActivity(intent);
                    finish();
                }
            } else {
                FileLogger.log("ERROR", TAG, "No Response from ACS");
                String transactionResult = "Error Connecting to ACS";
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

    private static class DownloadImagesTask extends AsyncTask<String, Void, Bitmap[]> {

        private ImageView imageView1, imageView2;

        public DownloadImagesTask(ImageView imageView1, ImageView imageView2) {
            this.imageView1 = imageView1;
            this.imageView2 = imageView2;
        }

        @Override
        protected Bitmap[] doInBackground(String... urls) {
            Bitmap[] bitmaps = new Bitmap[2];
            try {
                // Validate URLs and prepend http:// if necessary
                String imageUrl1 = validateUrl(urls[0]);
                String imageUrl2 = validateUrl(urls[1]);

                // Download the first image
                URL url1 = new URL(imageUrl1);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                InputStream input1 = connection1.getInputStream();
                bitmaps[0] = BitmapFactory.decodeStream(input1);

                // Download the second image
                URL url2 = new URL(imageUrl2);
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setDoInput(true);
                connection2.connect();
                InputStream input2 = connection2.getInputStream();
                bitmaps[1] = BitmapFactory.decodeStream(input2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap[] result) {
            if (result[0] != null) {
                imageView1.setImageBitmap(result[0]);
            }
            if (result[1] != null) {
                imageView2.setImageBitmap(result[1]);
            }
        }

        // Validate the URL and add the protocol if it's missing
        private String validateUrl(String url) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url; // Default to https if protocol is missing
            }
            return url;
        }
    }
}
