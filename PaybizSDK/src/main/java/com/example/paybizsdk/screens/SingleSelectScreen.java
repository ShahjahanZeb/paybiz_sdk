package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.utility.PostRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SingleSelectScreen extends AppCompatActivity {

    private static final String TAG = "SINGLE_SELECT";
    private TextView challengeInfoHeader, challengeInfoLabel, challengeInfoText, whyInfo, expandInfo, whyInfoLabelArrow, expandInfoLabelArrow;
    private Button submitButton, backButton;

    private ImageView issuerImage, paymentScheme, warningImage;

    private String acsUrl = "", challengeDataEntry = "";
    JSONObject creqJson = null;

    RadioButton emailButton, mobileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_select_screen);
        FileLogger.log("INFO", TAG, "Single Select Screen Opened");
        initializeUIComponents();
        Intent intent = getIntent();
        if (intent != null) {
            FileLogger.log("INFO", TAG, "Fetching Intent Values");
            handleIntentData(intent);
        }

//        ColorStateList colorStateList = ColorStateList(this, "");
//        radioButton.setButtonTintList(colorStateList);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLogger.log("INFO", TAG, "Email Radio Clicked");
                challengeDataEntry = "email";
                submitButton.setEnabled(true);
//                mobileButton.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
//                emailButton.setColor
//                        getBackground().setColorFilter(Color.parseColor("#064C70"), PorterDuff.Mode.SRC_IN);
            }
        });

        mobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLogger.log("INFO", TAG, "Mobile Radio Clicked");
                challengeDataEntry = "mobile";
                submitButton.setEnabled(true);
//                emailButton.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
//                mobileButton.getBackground().setColorFilter(Color.parseColor("#064C70"), PorterDuff.Mode.SRC_IN);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCreqRequest();
            }
        });
    }

    private void initializeUIComponents() {
        challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
        challengeInfoLabel = findViewById(R.id.challengeInfoLabel);
        challengeInfoText = findViewById(R.id.challengeInfoText);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        issuerImage = findViewById(R.id.yourBank);
        paymentScheme = findViewById(R.id.paymentScheme);
        whyInfo = findViewById(R.id.whyInfoLabel);
        expandInfo = findViewById(R.id.expandInfoLabel);
        emailButton = findViewById(R.id.radioEmail);
        mobileButton = findViewById(R.id.radioMobile);
        whyInfoLabelArrow = findViewById(R.id.whyInfoLabelArrow);
        expandInfoLabelArrow = findViewById(R.id.expandInfoLabelArrow);
    }

    private void handleIntentData(Intent intent) {
        FileLogger.log("INFO", TAG, "Fetching Intent Values");
        // Extract values from Intent
        String header = intent.getStringExtra("challengeInfoHeader");
        String label = intent.getStringExtra("challengeInfoLabel");
        String text = intent.getStringExtra("challengeInfoText");
        String textIndicator = intent.getStringExtra("challengeInfoTextIndicator");
        String issuerImageContent = intent.getStringExtra("issuerImage");
        String psImage = intent.getStringExtra("psImage");
        String submitAuthenticationLabel = intent.getStringExtra("submitAuthenticationLabel");
        String whyInfoLabel = intent.getStringExtra("whyInfoLabel");
        String expandInfoLabel = intent.getStringExtra("expandInfoLabel");
        String emailRadio = intent.getStringExtra("radioEmail");
        String mobileRadio = intent.getStringExtra("radioMobile");
        this.acsUrl = intent.getStringExtra("acsUrl");
        FileLogger.log("VERBOSE", TAG, "Values stored in Variables");
        // Set values to UI components
        setTextIfNotNull(challengeInfoHeader, header);
        setTextIfNotNull(challengeInfoLabel, label);
        System.out.println("Challenge Info Text:" + text + "\t " + textIndicator);
//        if (text != null && "Y".equalsIgnoreCase(textIndicator)) {
            String updatedText = text.replace("\\n\\n", "\n");
            challengeInfoText.setText(updatedText);
//            challengeInfoText.setLineSpacing(5, 1f);
//        }
        emailButton.setText(emailRadio);
        mobileButton.setText(mobileRadio);
        setTextIfNotNull(whyInfo, whyInfoLabel);
        setTextIfNotNull(expandInfo, expandInfoLabel);
        submitButton.setText(submitAuthenticationLabel);
        if (whyInfoLabel.isEmpty() || whyInfoLabel.equals("") || whyInfoLabel == null) {
            whyInfoLabelArrow.setText("");
        }
        if (expandInfoLabel.isEmpty() || expandInfoLabel.equals("") || expandInfoLabel == null) {
            expandInfoLabelArrow.setText("");
        }
        new SingleSelectScreen.DownloadImagesTask(issuerImage, paymentScheme).execute(issuerImageContent, psImage);
        FileLogger.log("VERBOSE", TAG, "Fetched Intent Values, Set Text Dynamically on Screen");
        try {
            FileLogger.log("VERBOSE", TAG, "Mapping CReq String to JSON Object");
            this.creqJson = new JSONObject(intent.getStringExtra("creq").toString());
            FileLogger.log("VERBOSE", TAG, "Mapping Done");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to set text on a TextView or Button if the text is not null.
     *
     * @param view The TextView or Button to set text on.
     * @param text The text to set.
     */
    private void setTextIfNotNull(TextView view, String text) {
        if (text != null) {
            view.setText(text);
        }
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

    public void sendCreqRequest() {
        try {
            creqJson.put("challengeDataEntry", challengeDataEntry);
            String response = this.callAcsForCreq();
            if (response != null || response != "") {
                JSONObject cres = new JSONObject(response.toString());
                FileLogger.log("VERBOSE", TAG, "CRes Mapped to JSON Object: " + response);
                renderOTPScreen(cres);
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

    private void renderOTPScreen(JSONObject cresObject) throws JSONException {
        if (cresObject != null) {
            String logMessage = "UI TYPE = '01', OTP Case";
            FileLogger.log("INFO", TAG, logMessage);
            Intent intent = new Intent(this, OTPScreen.class);
            intent.putExtra("challengeInfoHeader", cresObject.optString("challengeInfoHeader", ""));
            intent.putExtra("challengeInfoText", cresObject.optString("challengeInfoText", ""));
            intent.putExtra("whyInfoLabel", cresObject.optString("whyInfoLabel", ""));
            intent.putExtra("expandInfoLabel", cresObject.optString("expandInfoLabel", ""));
            intent.putExtra("challengeInfoLabel", cresObject.optString("challengeInfoLabel", ""));
            intent.putExtra("challengeInfoTextIndicator", cresObject.optString("challengeInfoTextIndicator", "N"));
            intent.putExtra("submitAuthenticationLabel", cresObject.optString("submitAuthenticationLabel", "Submit"));
            intent.putExtra("acsTransID", cresObject.optString("acsTransID", ""));
            // Handle issuer and psImage for both cases
            String defaultIssuerImage = "https://i.ibb.co/3k6GPqc/logibiz-logo-300x-1.png";
            String defaultPsImage = "https://i.ibb.co/Fw9Gtrd/Picture1.png";
            JSONObject issuerImageObject = cresObject.optJSONObject("issuerImage");
            JSONObject psImageObject = cresObject.optJSONObject("psImage");
            intent.putExtra("issuerImage", issuerImageObject != null && issuerImageObject.has("medium")
                    ? issuerImageObject.optString("medium", defaultIssuerImage) : defaultIssuerImage);
            intent.putExtra("psImage", psImageObject != null && psImageObject.has("medium")
                    ? psImageObject.optString("medium", defaultPsImage) : defaultPsImage);
            intent.putExtra("creq", creqJson.toString());
            intent.putExtra("acsUrl", acsUrl);
            finish();
            this.startActivity(intent);
        }
    }

}