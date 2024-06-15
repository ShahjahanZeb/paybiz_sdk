package com.example.paybizsdk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.ThreeDSService;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.service.UiCustomization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    PaybizController paybizController = new PaybizController(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileLogger.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("TOday Testing");
        paybizController.initialize();
        System.out.println("i am back");
        ProgressDialog progressDialog = paybizController.createTransaction("abc","1.2.2").getProgressView(this);
        progressDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                progressDialog.hide();
            }
        }, 6000);
        System.out.println(paybizController.getSDKVersion());
        System.out.println(paybizController.getWarnings());
        System.out.println(paybizController.createTransaction("abc","1.2.2").getAuthenticationRequestParameters());
    }
}