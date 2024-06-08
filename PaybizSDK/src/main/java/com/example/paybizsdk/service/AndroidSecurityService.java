package com.example.paybizsdk.service;

import android.os.Build;
import android.text.TextUtils;

import com.example.paybizsdk.entity.DeviceInfo;
import com.example.paybizsdk.entity.Warning;
import com.example.paybizsdk.utility.FileUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AndroidSecurityService {

    static JSONObject deviceInformation;

    public static final List<String> BINARY_PATHS = Arrays.asList("/sbin/",
            "/system/bin/", "/system/xbin/", "/data/local/xbin/",
            "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/",
            "/data/local/");
    public static final List<String> APK_PATHS = Arrays.asList("/system/app/");

    public static String isRooted() {
        return findSuBinary() + findSuperuserAPK();
    }

    public static String isTampered() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            return "Not Tampered";
        }
        return "Tampered";
    }

    public static boolean isRunningInEmulator() {
        return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.contains("google_sdk")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static boolean isDebuggerAttached(){
        return android.os.Debug.isDebuggerConnected();
    }


    public static boolean isAndroid_5_0_orNewer() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private static String findSuBinary() {
        String s = FileUtility.findFileInPaths("su", BINARY_PATHS);
        if (s != null && s.length() > 0) {
            return "su binary was found on paths: " + s;
        }
        return "";
    }

    private static String findSuperuserAPK() {
        String s = FileUtility.findAPK("Superuser.apk", APK_PATHS.get(0));
        if (s != null && s.length() > 0) {
            return "Superuser apk was found on paths: " + s;
        }
        return "";
    }


    public static JSONObject setDeviceInformationData(DeviceInfo deviceInfo, List<Warning> warnings) {
        deviceInformation = new JSONObject();
        try {
            deviceInformation.put("DV", "1.6");
            JSONObject ddObject = new JSONObject();
            JSONObject dpnaObject = new JSONObject();
            if (!TextUtils.isEmpty(deviceInfo.getC001())) {
                ddObject.put("C001", deviceInfo.getC001());
            } else {
                dpnaObject.put("C001", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC002())) {
                ddObject.put("C002", deviceInfo.getC002());
            } else {
                dpnaObject.put("C002", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC003())) {
                ddObject.put("C003", deviceInfo.getC003());
            } else {
                dpnaObject.put("C003", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC004())) {
                ddObject.put("C004", deviceInfo.getC004());
            } else {
                dpnaObject.put("C004", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC005())) {
                ddObject.put("C005", deviceInfo.getC005());
            } else {
                dpnaObject.put("C005", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC006())) {
                ddObject.put("C006", deviceInfo.getC006());
            } else {
                dpnaObject.put("C006", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC008())) {
                ddObject.put("C008", deviceInfo.getC008());
            } else {
                dpnaObject.put("C008", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC009())) {
                ddObject.put("C009", deviceInfo.getC009());
            } else {
                dpnaObject.put("C009", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC010())) {
                ddObject.put("C010", deviceInfo.getC010());
            } else {
                dpnaObject.put("C010", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC011())) {
                ddObject.put("C011", deviceInfo.getC011());
            } else {
                dpnaObject.put("C011", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC012())) {
                ddObject.put("C012", deviceInfo.getC012());
            } else {
                dpnaObject.put("C012", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC013())) {
                ddObject.put("C013", deviceInfo.getC013());
            } else {
                dpnaObject.put("C013", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC014())) {
                ddObject.put("C014", deviceInfo.getC014());
            } else {
                dpnaObject.put("C014", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC015())) {
                ddObject.put("C015", deviceInfo.getC015());
            } else {
                dpnaObject.put("C015", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC016())) {
                ddObject.put("C016", deviceInfo.getC016());
            } else {
                dpnaObject.put("C016", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC017())) {
                ddObject.put("C017", deviceInfo.getC017());
            } else {
                dpnaObject.put("C017", "RE04");
            }

            if (!TextUtils.isEmpty(deviceInfo.getC018())) {
                ddObject.put("C018", deviceInfo.getC018());
            } else {
                dpnaObject.put("C018", "RE04");
            }
            deviceInformation.put("DD", ddObject);
            deviceInformation.put("DPNA", dpnaObject);

            JSONArray swArray = new JSONArray();
            for (Warning warn : warnings) {
                swArray.put(warn.getWarningEnum());
            }
            deviceInformation.put("SW", swArray);
            return deviceInformation;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
