package com.example.paybizsdk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.paybizsdk.constants.ButtonType;
import com.example.paybizsdk.constants.UICustomizationType;
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

    ThreeDSService threeDSService = new ThreeDSService(this, this);


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiCustomization uiCustomization = new UiCustomization();
        ButtonCustomization btnCustomization = new ButtonCustomization();
        btnCustomization.setTextColor("#FF00FF");
        ToolbarCustomization toolbarCustomization = new ToolbarCustomization();
        toolbarCustomization.setBackgroundColor("#FF00FF");
        LabelCustomization lblCustomization = new LabelCustomization();
        lblCustomization.setTextColor("#FF00FF");
        TextBoxCustomization txtboxCustomization = new TextBoxCustomization();
        txtboxCustomization.setTextColor("#FF00FF");
        uiCustomization.setButtonCustomization(btnCustomization, ButtonType.NEXT);
        uiCustomization.setToolBarCustomization(toolbarCustomization);
        uiCustomization.setLabelCustomization(lblCustomization);
        uiCustomization.setTextBoxCustomization(txtboxCustomization);
        Map<UICustomizationType, UiCustomization> uiCustomizationMap = new HashMap<>();
        uiCustomizationMap.put(UICustomizationType.DEFAULT, uiCustomization);
        String locale = Locale.getDefault().toString();
        ConfigParameters configParameters = new ConfigParameters();
        threeDSService.initialize(this, configParameters, locale, uiCustomizationMap);
        System.out.println(threeDSService.getWarnings());
        Transaction threeDSService1 = threeDSService.createTransaction("abc", "2.3.0");
        System.out.println(threeDSService1.getAuthenticationRequestParameters().getSDKAppID());
        progressDialog = threeDSService1.getProgressView(this);
        progressDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
            }
        }, 5000);
    }
}