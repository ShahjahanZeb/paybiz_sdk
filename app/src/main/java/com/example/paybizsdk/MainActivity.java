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
import com.example.paybizsdk.entity.WebViewFragment;
import com.example.paybizsdk.interfaces.Transaction;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.ThreeDSService;
import com.example.paybizsdk.service.TransactionService;
import com.example.paybizsdk.service.UiCustomization;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private PaybizController paybizController;

    private String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <title>Additional Protection</title>\n" +
            "    <style>\n" +
            "        html {\n" +
            "            /*width: 390px;\n" +
            "            height: 400px;*/\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "            /* padding: 0px;\n" +
            "            margin: 0px; */\n" +
            "        }\n" +
            "\n" +
            "        body {\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "            /*\n" +
            "            width: 350px;\n" +
            "            height: 360px;\n" +
            "\n" +
            "            padding: 0px;\n" +
            "            margin-top: 20px;\n" +
            "            margin-left: 20px;\n" +
            "            margin-right: 20px;\n" +
            "            margin-bottom: 20px;\n" +
            "            */\n" +
            "        }\n" +
            "\n" +
            "        #container {\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "\n" +
            "            text-align: left;\n" +
            "\n" +
            "            border: 1px solid white;\n" +
            "        }\n" +
            "\n" +
            "        #sub-container {\n" +
            "            width: 350px;\n" +
            "            height: 360px;\n" +
            "            margin: auto;\n" +
            "            padding: 0;\n" +
            "\n" +
            "            text-align: left;\n" +
            "\n" +
            "            border: 1px solid white;\n" +
            "        }\n" +
            "\n" +
            "        #brandlogo {\n" +
            "            height: 51px;\n" +
            "            width: 89px;\n" +
            "            display: block;\n" +
            "            float: left;\n" +
            "\n" +
            "            text-align: center;\n" +
            "            word-wrap: break-word;\n" +
            "\n" +
            "            border: 1px solid blue;\n" +
            "        }\n" +
            "\n" +
            "        #banklogo {\n" +
            "            height: 47px;\n" +
            "            width: 140px;\n" +
            "            display: block;\n" +
            "            float: right;\n" +
            "\n" +
            "            text-align: center;\n" +
            "            word-wrap: break-word;\n" +
            "\n" +
            "            border: 1px solid blue;\n" +
            "        }\n" +
            "\n" +
            "        #ap {\n" +
            "            position: relative;\n" +
            "            width: 100%;\n" +
            "            color: rgb(52, 52, 102);\n" +
            "            margin-top: 100px;\n" +
            "\n" +
            "            font: bold 16px Arial;\n" +
            "        }\n" +
            "\n" +
            "        #otp-desc {\n" +
            "            position: relative;\n" +
            "            color: rgb(102, 102, 102);\n" +
            "\n" +
            "            font: normal 12px Arial;\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "        #otp-desc span {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "\n" +
            "        #details-container {\n" +
            "            display: block;\n" +
            "            position: relative;\n" +
            "\n" +
            "            margin-top: 10px;\n" +
            "\n" +
            "            height: 190px;\n" +
            "\n" +
            "            color: rgb(102, 102, 102);\n" +
            "\n" +
            "            font: normal 12px Arial;\n" +
            "        }\n" +
            "\n" +
            "        #details-container table td label {\n" +
            "            text-align: right;\n" +
            "        }\n" +
            "\n" +
            "        .column1 {\n" +
            "            vertical-align: top;\n" +
            "            padding-top: 5px;\n" +
            "            text-align: right;\n" +
            "        }\n" +
            "\n" +
            "        .column2 {\n" +
            "            width: 8px;\n" +
            "        }\n" +
            "\n" +
            "        .column3 {\n" +
            "            vertical-align: middle;\n" +
            "            text-align: left;\n" +
            "        }\n" +
            "\n" +
            "        .amount {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "    <div id=\"container\">\n" +
            "        <div id=\"sub-container\">\n" +
            "            <div id=\"brandlogo\">Brand Logo</div>\n" +
            "            <div id=\"banklogo\">Bank Logo</div>\n" +
            "            <div id=\"ap\">Added Protection</div>\n" +
            "            <div id=\"otp-desc\">Please submit your <span>One Time Password</span></div>\n" +
            "            <div id=\"details-container\">\n" +
            "                <form action=\"https://ddds.logibiztech.com:7000/acs2x/1/api/v2/process/challenge/7e8f9d57-507c-4bd5-b7ca-62ead1234e75/final210\"\n" +
            "                    method=\"POST\">\n" +
            "                    <input type=\"hidden\" id=\"threeDSSessionData\" name=\"threeDSSessionData\"\n" +
            "                        value=\"org.apache.catalina.session.StandardSessionFacade@6dc254c1\">\n" +
            "                    <table style=\"width:100%\">\n" +
            "                        <caption></caption>\n" +
            "                        <th class=\"column1\" scope=\"col\"></th>\n" +
            "                        <th class=\"column2\" scope=\"col\"></th>\n" +
            "                        <th class=\"column3\" scope=\"col\"></th>\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\">Merchant</td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\"><label id=\"merchant\" name=\"merchant\" >Some Merchant</label></td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\">Amount</td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3 amount\"><label id=\"amount\" name=\"amount\" >SAR 8.37</label>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\">Date</td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\"><label id=\"date\" name=\"date\" >20240625055108</label></td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\">Card number</td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\"><label id=\"pan\" name=\"pan\" >**** **** **** 4444</label></td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\"> </td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\"> </td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\">One Time Password</td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\">\n" +
            "                                <input title=\"otp\" placeholder=\"otp\" type=\"password\" id=\"otp\" name=\"otp\"\n" +
            "                                    value=\"\">\n" +
            "                                <br />\n" +
            "                                <a href=\"#\">Resend OTP</a>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\" style=\"height:10px;\"> </td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\"> </td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                        <tr>\n" +
            "                            <td class=\"column1\"></td>\n" +
            "                            <td class=\"column2\"></td>\n" +
            "                            <td class=\"column3\">\n" +
            "                                <input type=\"submit\" id=\"submit-btn\" name=\"submit-btn\" value=\"submit\" />\n" +
            "                                <a href=\"#\" style=\"float: right\">Exit</a>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "                </form>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        paybizController = new PaybizController(this, this);
//        FileLogger.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paybizController.initialize();
        paybizController.createTransaction("abc", "1.2.2");
        System.out.println(paybizController.getAuthParams());
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
}