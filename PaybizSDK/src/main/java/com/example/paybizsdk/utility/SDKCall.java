package com.example.paybizsdk.utility;

import android.content.Context;
import android.content.Intent;
public class SDKCall {

    public static final String ACTION_NOTIFY = "com.example.paybizsdk.service.NOTIFY_EVENT";
    public static void triggerEvent(Context context, String threeDSServerTransId, String acsTransId,
                                    String sdkTransId, String transStatus) {
        System.out.println(" In Trigger Event");
        // Create an intent to send a broadcast with the event message
        Intent intent = new Intent(ACTION_NOTIFY);
        intent.putExtra("threeDSServerTransId", threeDSServerTransId);
        intent.putExtra("acsTransId", acsTransId);
        intent.putExtra("sdkTransId", sdkTransId);
        intent.putExtra("transStatus", transStatus);
        context.sendBroadcast(intent);
    }

}
