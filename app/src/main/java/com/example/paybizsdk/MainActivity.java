package com.example.paybizsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RadioButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebView webView;

//    String htmlContent = "<html>" +
//            "<body>" +
//            "<h1>Welcome to WebView</h1>" +
//            "<img src='https://via.placeholder.com/150' alt='Sample Image' />" +
//            "<p>Please enter your name:</p>" +
//            "<input type='text' id='nameInput' />" +
//            "<button type='button' onclick='sendData()'>Submit</button>" +
//            "<script type='text/javascript'>" +
//            "function sendData() {" +
//            "   var name = document.getElementById('nameInput').value;" +
//            "   Android.showData(name);" +  // Call Android function
//            "}" +
//            "</script>" +
//            "</body>" +
//            "</html>";

//    String htmlContent = "<html>" +
//            "<head>" +
//            "    <meta charset=\"UTF-8\">" +
//            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
//            "    <title>Payment Security</title>" +
//            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">" +
//            "    <style>" +
//            "        body {" +
//            "            margin: 0;" +
//            "            height: 100vh;" +
//            "            background-color: #f8f9fa;" +
//            "        }" +
//            "        .payment-security-container {" +
//            "            background-color: white;" +
//            "            padding: 20px;" +
//            "            border-radius: 10px;" +
//            "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
//            "            text-align: center;" +
//            "            width: 100%;" +
//            "            height: 100vh;" +
//            "        }" +
//            "        .logos {" +
//            "            display: flex;" +
//            "            justify-content: space-between;" +
//            "            align-items: center;" +
//            "            margin-bottom: 20px;" +
//            "        }" +
//            "        .logos img {" +
//            "            max-width: 50px;" +
//            "        }" +
//            "        .security-heading {" +
//            "            font-size: 20px;" +
//            "            font-weight: bold;" +
//            "            margin-bottom: 10px;" +
//            "        }" +
//            "        .step-text {" +
//            "            text-align: left;" +
//            "            margin-bottom: 20px;" +
//            "        }" +
//            "        .btn-continue {" +
//            "            background-color: #28a745;" +
//            "            color: white;" +
//            "            border: none;" +
//            "            padding: 10px 20px;" +
//            "            border-radius: 5px;" +
//            "            font-size: 16px;" +
//            "            cursor: pointer;" +
//            "            width: 100%;" +
//            "        }" +
//            "        .btn-continue:hover {" +
//            "            background-color: #218838;" +
//            "        }" +
//            "        .help-links {" +
//            "            margin-top: 20px;" +
//            "        }" +
//            "        .help-links a {" +
//            "            display: block;" +
//            "            color: #007bff;" +
//            "            text-decoration: none;" +
//            "            margin-bottom: 10px;" +
//            "        }" +
//            "        .help-links a:hover {" +
//            "            text-decoration: underline;" +
//            "        }" +
//            "        .arrow {" +
//            "            float: right;" +
//            "        }" +
//            "    </style>" +
//            "</head>" +
//            "<body>" +
//            "    <div class=\"payment-security-container\">" +
//            "        <div class=\"logos\">" +
//            "            <img src=\"https://via.placeholder.com/50x50?text=YourBank\" alt=\"YourBank Logo\">" +
//            "            <img src=\"https://via.placeholder.com/50x50?text=Card\" alt=\"Card Network Logo\">" +
//            "        </div>" +
//            "        <div class=\"security-heading\">Payment Security</div>" +
//            "        <p>For added security, you will be authenticated with YourBank application.</p>" +
//            "        <div class=\"step-text\">" +
//            "            <p><strong>Step 1</strong> – Open your YourBank application directly from your phone and verify this payment.</p>" +
//            "            <p><strong>Step 2</strong> – Tap continue after you have completed authentication with your YourBank application.</p>" +
//            "        </div>" +
//            "        <button class=\"btn-continue\">CONTINUE</button>" +
//            "        <div class=\"help-links\">" +
//            "            <a href=\"#\">Learn more about authentication <span class=\"arrow\">&#x25BC;</span></a>" +
//            "            <a href=\"#\">Need some help? <span class=\"arrow\">&#x25BC;</span></a>" +
//            "        </div>" +
//            "    </div>" +
//            "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js\"></script>" +
//            "</body>" +
//            "</html>";


    RadioButton emailButton, mobileButton;

    String htmlContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Security Question Verification</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "            font-family: Arial, sans-serif;\n" +
            "        }\n" +
            "\n" +
            "        body,\n" +
            "        html {\n" +
            "            height: auto;\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "        body {\n" +
            "            display: flex;\n" +
            "            flex-direction: column;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "            background-color: white;\n" +
            "        }\n" +
            "\n" +
            "        .container{ \n" +
            "            padding: 2% 5%;\n" +
            "            height: 100vh;\n" +
            "        }\n" +
            "        .header {\n" +
            "            display: flex;\n" +
            "            justify-content: space-between;\n" +
            "            align-items: center;\n" +
            "            width: 100%;\n" +
            "            max-width: 600px;\n" +
            "        }\n" +
            "\n" +
            "        .bank-logo,\n" +
            "        .card-logo {\n" +
            "            max-width: 60px;\n" +
            "        }\n" +
            "\n" +
            "        .content {\n" +
            "            text-align: left;\n" +
            "            width: auto;\n" +
            "            max-width: auto;\n" +
            "        }\n" +
            "\n" +
            "        h2 {\n" +
            "            color: #272727;\n" +
            "            font-size: 22px;\n" +
            "            margin-bottom: 25px;\n" +
            "            margin-top: 35px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        p {\n" +
            "            color: #232222;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .question-text {\n" +
            "            text-align: left;\n" +
            "            font-weight: bold;\n" +
            "            color: #333;\n" +
            "            margin: 15px 5 10px;\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "\n" +
            "        .verification-section {\n" +
            "            display: flex;\n" +
            "            justify-content: space-between;\n" +
            "            align-items: center;\n" +
            "            margin-bottom: 15px;\n" +
            "            text-align: left;\n" +
            "        }\n" +
            "\n" +
            "        .verification-text {\n" +
            "            width: 60%;\n" +
            "            color: #272727;\n" +
            "        }\n" +
            "\n" +
            "        .verification-question-text {\n" +
            "            width: 90%;\n" +
            "            color: #272727;\n" +
            "        }\n" +
            "\n" +
            "        .image-container {\n" +
            "            width: 100px;\n" +
            "            margin-left: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .image-container img {\n" +
            "            width: 100%;\n" +
            "            height: auto;\n" +
            "            border-radius: 5px;\n" +
            "        }\n" +
            "\n" +
            "        /* Input field styling */\n" +
            "        .answer-input {\n" +
            "            width: 100%;\n" +
            "            padding: 12px;\n" +
            "            margin-top: 10px;\n" +
            "            margin-bottom: 20px;\n" +
            "            border-radius: 5px;\n" +
            "            border: 1px solid #ccc;\n" +
            "            max-width: 900px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        .submit-btn {\n" +
            "            width: 100%;\n" +
            "            padding: 12px;\n" +
            "            margin-bottom: 15px;\n" +
            "            border: none;\n" +
            "            border-radius: 5px;\n" +
            "            font-size: 16px;\n" +
            "            cursor: pointer;\n" +
            "            background-color: #28a745;\n" +
            "            color: white;\n" +
            "            max-width: 900px;\n" +
            "        }\n" +
            "\n" +
            "        .help-link-container {\n" +
            "            display: flex;\n" +
            "            justify-content: space-between;\n" +
            "            align-items: center;\n" +
            "            margin-top: 35px;\n" +
            "            cursor: pointer;\n" +
            "            max-width: 400px;\n" +
            "        }\n" +
            "\n" +
            "        .help-link {\n" +
            "            color: #666;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "\n" +
            "        .arrow {\n" +
            "            font-size: 12px;\n" +
            "        }\n" +
            "\n" +
            "        .help-info {\n" +
            "            display: none;\n" +
            "            margin-top: 20px;\n" +
            "            color: #666;\n" +
            "            font-size: 14px;\n" +
            "            text-align: left;\n" +
            "            max-width: 400px;\n" +
            "        }\n" +
            "\n" +
            "        .error-message {\n" +
            "            align-items: center;\n" +
            "            justify-content: center;\n" +
            "            margin-top: 10px;\n" +
            "            color: #333;\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "        .error-icon img {\n" +
            "            width: 35px;\n" +
            "            height: 35px;\n" +
            "        }\n" +
            "\n" +
            "        .error-text {\n" +
            "            margin-left: 5px;\n" +
            "            font-size: 13px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <div class=\"logo\">\n" +
            "                <img src=\"https://i.ibb.co/3k6GPqc/logibiz-logo-300x-1.png\" alt=\"YourBank Logo\" class=\"bank-logo\">\n" +
            "            </div>\n" +
            "            <div class=\"network-logo\">\n" +
            "                <img src=\"https://i.ibb.co/Fw9Gtrd/Picture1.png\" alt=\"Card Network Logo\" class=\"card-logo\">\n" +
            "            </div>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"content\">\n" +
            "            <h2>Verify Your Payment</h2>\n" +
            "\n" +
            "            <div class=\"error-message\" style=\"display: display-text\">\n" +
            "                <div class=\"error-icon\">\n" +
            "                    <img src=\"https://i.ibb.co/FqhchJk/8256069.png\" alt=\"Result Image\">\n" +
            "\n" +
            "                </div>\n" +
            "                <div class=\"error-text\">\n" +
            "                    <p>The answer you have provided is incorrect, please try again.</p>\n" +
            "                </div>\n" +
            "\n" +
            "            </div>\n" +
            "            <p>Please answer 3 security questions from YourBank to complete your payment.</p>\n" +
            "\n" +
            "            <!-- Image Verification Section with Text on Left and Image on Right -->\n" +
            "            <div class=\"verification-section\">\n" +
            "                <div class=\"verification-text\">\n" +
            "                    <p>If the image to the right is correct, please answer the question below.</p>\n" +
            "                </div>\n" +
            "                <div class=\"image-container\">\n" +
            "                    <img src=\"{{question-image}}\" alt=\"Verification Image\">\n" +
            "                </div>\n" +
            "            </div>\n" +
            "\n" +
            "            <!-- Dynamic Question Section -->\n" +
            "            <label class=\"question-text\" for=\"answer-input\">Question {{index}}:</label><br>\n" +
            "            <div class=\"verification-question-text\">\n" +
            "                <p>{{question}}</p>\n" +
            "            </div>\n" +
            "            <!-- Answer Input -->\n" +
            "            <input type=\"text\" id=\"answer-input\" class=\"answer-input\" placeholder=\"Enter Answer Here\">\n" +
            "\n" +
            "            <!-- Submit Button -->\n" +
            "            <button class=\"submit-btn\">NEXT</button>\n" +
            "\n" +
            "            <!-- Help Link -->\n" +
            "            <div class=\"help-link-container\" onclick=\"toggleHelp()\">\n" +
            "                <span class=\"help-link\">Need some help?</span>\n" +
            "                <span class=\"arrow\">&#9660;</span>\n" +
            "            </div>\n" +
            "            <div class=\"help-info\">\n" +
            "                <p></p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "\n" +
            "    <script>\n" +
            "        function toggleHelp() {\n" +
            "            const helpInfo = document.querySelector('.help-info');\n" +
            "            const arrow = document.querySelector('.arrow');\n" +
            "            if (helpInfo.style.display === \"none\" || helpInfo.style.display === \"\") {\n" +
            "                helpInfo.style.display = \"block\";\n" +
            "                arrow.innerHTML = \"&#9650;\"; // Up arrow\n" +
            "            } else {\n" +
            "                helpInfo.style.display = \"none\";\n" +
            "                arrow.innerHTML = \"&#9660;\"; // Down arrow\n" +
            "            }\n" +
            "        }\n" +
            "    </script>\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        webView = findViewById(R.id.webViews);
//        webView.loadData(htmlContent, "text/html", "UTF-8");
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this),"app");
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
////
////        // Add a JavaScript interface to interact with the WebView
//        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "Android");
////
//        webView.setWebViewClient(new WebViewClient());


        System.out.println("SENDING MADA REQUEST");
        String registerURL = "https://staging.logibiztech.com:8777/mada/mada/register";
        String madaRequest = "{\n" +
                "    \"transactionType\": \"PURCHASE\",\n" +
                "    \"clientId\":\"345267811\",\n" +
                "    \"acquiringInstitutionId\": \"5454879\",\n" +
                "    \"terminalType\": \"INTERNET\",\n" +
                "    \"initiatedBy\": \"CARDHOLDER_OR_ECOMMERCE\",\n" +
                "    \"tokenType\": \"PAN\",\n" +
                "    \"amount\": \"200\",\n" +
                "    \"merchantId\": \"merchant1\",\n" +
                "    \"terminalId\": \"t111111111111\",\n" +
                "    \"merchantName\": \"Ahmad\",\n" +
                "    \"merchantArabicName\": \"Hassan\",\n" +
                "    \"address\": \"House34\",\n" +
                "    \"city\": \"Riyadh\",\n" +
                "    \"zipCode\": \"60000\",\n" +
                "    \"regionCode\": \"001\",\n" +
                "    \"mcc\": \"5469\",\n" +
                "    \"countryCode\": \"586\",\n" +
                "    \"currency\": \"SAR\",\n" +
                "    \"merchantReference\": \"123900\",\n" +
                "    \"pan\": \"5588480000000003\",\n" +
                "    \"cvv2\": \"123\",\n" +
                "    \"panExpDate\": \"1225\",\n" +
                "    \"cardBrand\": \"MADA\",\n" +
                "    \"localDateTime\": \"240726134158\",\n" +
                "    \"udf\": [\"123333\"],\n" +
                "    \"threedsFlag\": true,\n" +
                "    \"threeDsData\": {\n" +
                "        \"eci\": \"05\",\n" +
                "        \"transStatus\": \"Y\",\n" +
                "        \"authenticationValue\": \"0DUzMjM1MDY50TE4NTMyMzUWNjk=\",\n" +
                "        \"sli\": \"05\",\n" +
                "        \"dsTransId\": \"e1653e9a-eb9e-4fc4-af89-ac9ca670b970\",\n" +
                "        \"acsReferenceNumber\": \"REF-PANA-210-ACS1\",\n" +
                "        \"dsReferenceNumber\": \"DS-PANA-2XX-REFERENCE\",\n" +
                "        \"acsTransId\": \"68abee6a-2cb9-4bb0-bf15-19e46153fd0f\",\n" +
                "        \"messageVersion\": \"2.1.0\",\n" +
                "        \"threedsTransDate\": \"20240726134158\"\n" +
                "    },\n" +
                "     \"schemeSpecificData\": {\n" +
                "        \"visaSpecificData\": {\n" +
                "            \"merchantVerificationValue\": \"MERVAL\",\n" +
                "            \"marketSpecificIndicator\": \"\",\n" +
                "            \"merchantIdentifier\": \"merc7771\",\n" +
                "            \"commercialCardTypeRequest\": \"V\",\n" +
                "            \"authorizationCharacteristicsIndicator\": \"Y\"\n" +
                "        }\n" +
                "    }\n" +
                "   \n" +
                "}";


//        ApiClient apiClient = new ApiClient();
//
//
//        new Thread(() -> {
//            try {
//                String response = apiClient.post("https://staging.logibiztech.com:8777/mada/mada/transaction", madaRequest);
//                System.out.println("Response from API: " + response);
//                setData(response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

        System.out.println("URL Openeded");
        Uri appUrl = Uri.parse("https://www.google.com");
        Intent appIntent = new Intent(Intent.ACTION_VIEW);
        appIntent.setData(appUrl);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(appIntent);
        System.out.println("Brand: "+ getCardBrand("4223123"));


//        new ApiCall().postRequestWithExecutorService("https://staging.logibiztech.com:8777/mada/mada/transaction", madaRequest);
//        com.example.paybizsdk.ApiClient apiClient = new com.example.paybizsdk.ApiClient();
//        try {
//            String response = apiClient.post(registerURL, request);
//            System.out.println("response: "+response);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    public static String getCardBrand(String pan) {
        String cardFirstDigit = pan.substring(0, 1);

        switch (cardFirstDigit){
            case "4":
                return "VISA";
            case "5":
                return "MASTERCARD";
            default:
                return "";
        }
    }
    public void setData(String response){
        System.out.println("Response outside: "+ response);
    }
}