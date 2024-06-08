package com.example.paybizsdk.utility;

import android.os.Build;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static String getIPAddress(boolean useIPv4) {
        try {
            ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String getSDKAppID() {
        UUID uuid = UUID.randomUUID();
        String sdkAppID = uuid.toString();
        return sdkAppID;
    }

    public static String getUTCDateTime(){
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    public static String currentVersion(){
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly Bean
        if(release >= 4.1 && release < 4.4) codeName = "Jelly Bean";
        else if(release == 5)   codeName="Lollipop";
        else if(release == 6)   codeName="M";
        else if(release == 7)   codeName="N";
        else if(release == 8)   codeName="O";
        else if(release == 9)   codeName="P";
        else if(release == 10)  codeName="Q";
        else if(release == 11) codeName="R";
        else if(release == 12) codeName="S";
        else if(release == 13) codeName="TIRAMISU";
        else if(release == 14) codeName="UPSIDE_DOWN_CAKE";
        return codeName;
    }

}
