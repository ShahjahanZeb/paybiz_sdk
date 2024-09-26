package com.example.paybizsdk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.ButtonType;
import com.example.paybizsdk.constants.UICustomizationType;
import com.example.paybizsdk.controller.PaybizController;
import com.example.paybizsdk.encryption.AESEncryption;
import com.example.paybizsdk.entity.ButtonCustomization;
import com.example.paybizsdk.entity.LabelCustomization;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.entity.TextBoxCustomization;
import com.example.paybizsdk.entity.ToolbarCustomization;
import com.example.paybizsdk.entity.WebViewFragment;
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.ThreeDSService;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.service.UiCustomization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    private PaybizController paybizController;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        new DownloadImageTask(imageView).execute("https://i.ibb.co/mB7hkKJ/mastercard-26161.png");
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://i.postimg.cc/rwpLHJRn/visa-logo-png-2020.png").getContent());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        imageView.setImageBitmap(bitmap);
//        paybizController.initialize();
//        paybizController.createTransaction("abc", "1.2.2");
//        System.out.println(paybizController.getAuthParams());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("abc", "abc");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        paybizController.doChallenge(this, jsonObject);
//        progressDialog.show();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                progressDialog.hide();
//            }
//        }, 6000);
//        try {
//            paybizController.doChallenge(this, null, null);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

//        WebViewFragment webViewWrapper = new WebViewFragment(this);
//
//                final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                String htmlContent = "<html><body><h1>Hello, WebViewWrapper!</h1><p>This is some HTML content.</p></body></html>";
//                webViewWrapper.loadHtml(html);
//
//                // Add WebViewWrapper to your activity's content view
//                setContentView(webViewWrapper);
//            }
//        }, 6000);

    }
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlDisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}