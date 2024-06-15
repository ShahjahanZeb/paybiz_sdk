package com.example.paybizsdk.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.ButtonType;
import com.example.paybizsdk.constants.UICustomizationType;
import com.example.paybizsdk.entity.ButtonCustomization;
import com.example.paybizsdk.entity.LabelCustomization;
import com.example.paybizsdk.entity.TextBoxCustomization;
import com.example.paybizsdk.entity.ToolbarCustomization;
import com.example.paybizsdk.entity.Warning;
import com.example.paybizsdk.interfaces.Controller;
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.ThreeDSService;
import com.example.paybizsdk.service.UiCustomization;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaybizController implements Controller {

    private static final String TAG = "PaybiController";

    private Activity activity;

    private Context context;

    private ThreeDSService threeDSService;

    public PaybizController(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        threeDSService = new ThreeDSService(this.activity, this.context);
//        FileLogger.initialize(context);

    }

    @Override
    public void initialize() {
        requestBluetoothConnectPermission();
    }

    public void init() {
        FileLogger.log("INFO", TAG, "--- In Initialize Method ---");
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
        threeDSService.initialize(this.context, configParameters, locale, uiCustomizationMap);
    }

    private void requestBluetoothConnectPermission() {
        FileLogger.log("INFO", TAG, "--- Request Bluetooth Connect Permission ---");
        Context context1 = this.context;
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(context1, Manifest.permission.BLUETOOTH_CONNECT)
                            == PackageManager.PERMISSION_GRANTED) {
                        requestBluetoothPermission();
                    }
                }
            }, 6000);
        } else {
            requestBluetoothPermission();
        }
    }

    private void requestBluetoothPermission() {
        FileLogger.log("INFO", TAG, "--- Request Bluetooth Permission ---");
        Context context1 = this.context;
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.BLUETOOTH},
                    1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(context1, Manifest.permission.BLUETOOTH)
                            == PackageManager.PERMISSION_GRANTED) {
                        requestLocationPermission();
                    }
                }
            }, 6000);

        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        FileLogger.log("INFO", TAG, "--- Request Location Permission ---");
        Context context1 = this.context;
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    3);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(context1, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        init();
                    }
                }
            }, 6000);
        } else {
            init();
        }
    }

    @Override
    public Transaction createTransaction(String directoryServerID, String messageVersion) {
        FileLogger.log("INFO", TAG, "--- In Create Transaction ---");
        return threeDSService.createTransaction(directoryServerID, messageVersion);
    }

    @Override
    public void cleanup(Context applicationContext) {
        FileLogger.log("INFO", TAG, "--- In Clean Up ---");
        threeDSService.cleanup(this.context);
    }

    @Override
    public String getSDKVersion() {
        FileLogger.log("INFO", TAG, "--- In Get SDK Version ---");
        return threeDSService.getSDKVersion();
    }

    @Override
    public List<Warning> getWarnings() {
        FileLogger.log("INFO", TAG, "--- In Get Warnings ---");
        return threeDSService.getWarnings();
    }

}
