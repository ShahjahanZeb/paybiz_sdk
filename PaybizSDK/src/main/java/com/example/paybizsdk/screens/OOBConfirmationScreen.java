package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.service.DatabaseService;
import com.example.paybizsdk.utility.Utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class OOBConfirmationScreen extends AppCompatActivity {

    TextView challengeInfoHeader, challengeInfoText, whyInfo, whyInfoLabelArrow;
    Button button, backButton;

    private final String TAG = "OOB Confirmation Screen";

    private String oobURL = "";

    private ImageView issuerImage, paymentScheme;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oobconfirmation_screen);
        FileLogger.log("INFO", TAG, "OOB Confirmation Screen Opened");
        String url = "", threeDSServerTransID = "", amount = "", currency = "";
        try {
            FileLogger.log("VERBOSE", TAG, "Fetching Data from Intent, Database and setting dynamically on Screen");
            challengeInfoHeader = findViewById(R.id.challengeInfoHeader);
            challengeInfoText = findViewById(R.id.challengeInfoText);
            button = findViewById(R.id.button);
            backButton = findViewById(R.id.backButton);
            issuerImage = findViewById(R.id.yourBank);
            paymentScheme = findViewById(R.id.paymentScheme);
            whyInfo = findViewById(R.id.whyInfoLabel);
            whyInfoLabelArrow = findViewById(R.id.whyInfoLabelArrow);
            Intent intent = getIntent();
            String infoHeader = intent.getStringExtra("challengeInfoHeader");
            String infoText = intent.getStringExtra("challengeInfoText"), updatedInfoText = "";
            if (infoText != null && !infoText.isEmpty()) {
                updatedInfoText = infoText.replace("\\n\\n", "\n");
            }
            String appLabel = intent.getStringExtra("oobAppLabel");
            url = intent.getStringExtra("oobAppUrl");
            threeDSServerTransID = intent.getStringExtra("threeDSServerTransID");
            String issuerImageContent = intent.getStringExtra("issuerImage");
            String psImage = intent.getStringExtra("psImage");
            String whyInfoLabel = intent.getStringExtra("whyInfoLabel");
            String expandInfoLabel = intent.getStringExtra("expandInfoLabel");
            FileLogger.log("VERBOSE", TAG, "Values stored in Variables");
            challengeInfoHeader.setText(infoHeader);
            challengeInfoText.setText(updatedInfoText);
            challengeInfoText.setLineSpacing(5, 1f);
            button.setText(appLabel);
            whyInfo.setText(whyInfoLabel);
            if (whyInfoLabel.isEmpty() || whyInfoLabel.equals("") || whyInfoLabel == null) {
                whyInfoLabelArrow.setText("");
            }
            DatabaseService databaseService = new DatabaseService(this);
            Cursor cursor = databaseService.getLastTransaction();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    FileLogger.log("VERBOSE", TAG, "Fetching values from Cursor");
                    amount = !cursor.getString(cursor.getColumnIndex("amount")).isEmpty() ? cursor.getString(cursor.getColumnIndex("amount")) : "840";
                    currency = !cursor.getString(cursor.getColumnIndex("currency")).isEmpty() ? cursor.getString(cursor.getColumnIndex("currency")) : "111";
                    this.oobURL = url + "?amount=" + amount + "&currency=" + currency + "&threeDsRquestorAppUrl=myapp://localhost/path" + "&threeDSServerTransID=" + threeDSServerTransID + "&timestamp=" + Utils.getUTCDateTime();
                }
            }
            System.out.println("\n\nImages URL: " + issuerImageContent + "\n" + psImage);
            new OOBConfirmationScreen.DownloadImagesTask(issuerImage, paymentScheme).execute(issuerImageContent, psImage);
        } catch (Exception e) {
            FileLogger.log("ERROR", TAG, "Error: " + e.getMessage());
        }

        FileLogger.log("VERBOSE", TAG, "OOB App URL: " + this.oobURL);

        String finalUrl = url;
        String finalAmount = amount;
        String finalCurrency = currency;
        String finalThreeDSServerTransID = threeDSServerTransID;
        button.setOnClickListener(v -> {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(oobURL));
//                startActivity(browserIntent);
//                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(oobURL);
//                startActivity(launchIntent);
            Uri appUrl = Uri.parse(oobURL);
            Intent appIntent = new Intent(Intent.ACTION_VIEW);
            appIntent.setData(appUrl);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                finish();
                startActivity(appIntent);
            } catch (ActivityNotFoundException e) {
                Toast toast = new Toast(getBaseContext());
                toast.setText("OOB App not found, redirecting to Web");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                FileLogger.log("ERROR", TAG, "Not able to open OOB App: " + e.getMessage());
                String webURL = "https://staging.logibiztech.com:8777/oob/openoobApp?amount=" + finalAmount + "&currency=" + finalCurrency + "&threeDsRquestorAppUrl=myapp://localhost/path" + "&threeDSServerTransID=" + finalThreeDSServerTransID + "&timestamp=" + Utils.getUTCDateTime();
                FileLogger.log("VERBOSE", TAG, "Redirecting User to Web" + e.getMessage());
                Uri webURI = Uri.parse(webURL);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(webURI);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    finish();
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException ex) {
                    FileLogger.log("ERROR", TAG, "Not able to open OOB Website: " + ex.getMessage());
                    Toast webToaster = new Toast(getBaseContext());
                    webToaster.setText("Not able to open OOB Web");
                    webToaster.setDuration(Toast.LENGTH_LONG);
                    webToaster.show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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