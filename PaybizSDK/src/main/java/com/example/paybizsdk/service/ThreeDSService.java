package com.example.paybizsdk.service;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.constants.SDKConstants;
import com.example.paybizsdk.constants.UICustomizationType;
import com.example.paybizsdk.constants.WarningEnum;
import com.example.paybizsdk.constants.WarningSeverity;
import com.example.paybizsdk.encryption.AESEncryption;
import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.DeviceInfo;
import com.example.paybizsdk.entity.ProgressDialog;
import com.example.paybizsdk.entity.Warning;
import com.example.paybizsdk.exceptions.InvalidInputException;
import com.example.paybizsdk.exceptions.SDKAlreadyInitializedException;
import com.example.paybizsdk.exceptions.SDKNotInitializedException;
import com.example.paybizsdk.exceptions.SDKRuntimeException;
import com.example.paybizsdk.interfaces.*;
import com.example.paybizsdk.utility.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ThreeDSService implements ThreeDS2Service {

    private ConfigParameters configParameters;
    private List<Warning> warnings = new ArrayList<Warning>();

    private static final int REQUEST_LOCATION = 2;
    private Activity activity;

    private Context context;


    public ThreeDSService() {
    }

    public ThreeDSService(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public static JSONObject deviceInformation;

    private static final String TAG = "ThreeDSService";

    @Override
    public void initialize(Context applicationContext, ConfigParameters configParameters, String locale, Map<UICustomizationType, UiCustomization> uiCustomizationMap) throws InvalidInputException,
            SDKAlreadyInitializedException, SDKRuntimeException {
        FileLogger.log("INFO", TAG, "--- Getting Device Info ---");
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setC001("Android");
        deviceInfo.setC002(Build.MANUFACTURER + "||" + Build.MODEL);
        deviceInfo.setC003("Android " + Utils.currentVersion() + " " + Build.VERSION.RELEASE + " API " + Build.VERSION.SDK_INT);
        deviceInfo.setC004(Build.VERSION.RELEASE);
        deviceInfo.setC005(locale);
        deviceInfo.setC006(String.valueOf((TimeZone.getDefault().getRawOffset() / 60000) * -1));
        deviceInfo.setC008(Resources.getSystem().getDisplayMetrics().heightPixels + "x" + Resources.getSystem().getDisplayMetrics().widthPixels);
        deviceInfo.setC009(getBluetoothDeviceName(applicationContext));
        deviceInfo.setC010(Utils.getIPAddress(true));
        Location location = getLocation(applicationContext);
        if (location != null) {
            deviceInfo.setC011(String.valueOf(location.getLatitude()));
            deviceInfo.setC012(String.valueOf(location.getLongitude()));
        }
        deviceInfo.setC013(applicationContext.getPackageName());
        deviceInfo.setC014(Utils.getSDKAppID());
        deviceInfo.setC015(SDKConstants.SDK_VERSION);
        deviceInfo.setC016(SDKConstants.SDK_REF_NUM);
        deviceInfo.setC017(Utils.getUTCDateTime());
        deviceInfo.setC018(SDKConstants.SDK_TRANS_ID);

        FileLogger.log("INFO", TAG, "--- Performing Security Checks ---");
        String isRoot = AndroidSecurityService.isRooted();
        if (isRoot != null || isRoot != "") {
            warnings.add(new Warning(WarningEnum.SW01, isRoot, WarningSeverity.HIGH));
        }
        String isTampered = AndroidSecurityService.isTampered();
        if (isTampered.equalsIgnoreCase("tampered")) {
            warnings.add(new Warning(WarningEnum.SW02, "The integrity of the SDK has been tampered", WarningSeverity.HIGH));
        }
        if (!AndroidSecurityService.isRunningInEmulator()) {
            warnings.add(new Warning(WarningEnum.SW03, "An emulator is being used to run the App.", WarningSeverity.HIGH));
        }
        if (AndroidSecurityService.isDebuggerAttached()) {
            warnings.add(new Warning(WarningEnum.SW04, "A debugger is attached to the App.", WarningSeverity.MEDIUM));
        }
        if (!AndroidSecurityService.isAndroid_5_0_orNewer()) {
            warnings.add(new Warning(WarningEnum.SW05, "The OS or the OS version is not supported.", WarningSeverity.HIGH));
        }
        this.deviceInformation = AndroidSecurityService.setDeviceInformationData(deviceInfo, this.warnings);
        FileLogger.log("INFO", TAG, String.valueOf(this.deviceInformation));
    }

    private Location getLocation(Context context) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
            // user defines the criteria
            criteria.setCostAllowed(false);
            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                return location;
            }
        } catch (Exception e) {
            FileLogger.log("ERROR", TAG, e.getMessage());
        }
        return null;
    }


    private String getBluetoothDeviceName(Context context) {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                }
                return bluetoothAdapter.getName();
            }
        } catch (Exception e) {
            FileLogger.log("ERROR", TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public Transaction createTransaction(String directoryServerID, String messageVersion) throws
            InvalidInputException, SDKNotInitializedException,
            SDKRuntimeException {
        TransactionService transactionService = null;
        try {
            transactionService = new TransactionService(this.activity, this.context, Utils.getSDKAppID(), AESEncryption.encrypt(SDKConstants.SECRET_KEY), messageVersion);
            return transactionService;
        } catch (Exception e) {
            FileLogger.log("ERROR", TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public void cleanup(Context applicationContext) throws SDKNotInitializedException {
        applicationContext.getResources().flushLayoutCache();
    }

    @Override
    public String getSDKVersion() throws SDKNotInitializedException,
            SDKRuntimeException {
        return SDKConstants.SDK_VERSION;
    }

    @Override
    public List<Warning> getWarnings() {
        return this.warnings;
    }

}