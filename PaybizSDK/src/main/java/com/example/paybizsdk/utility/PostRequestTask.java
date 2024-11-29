package com.example.paybizsdk.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class PostRequestTask implements Callable<String> {

    private String urlString;
    private String value;
    private String key;

    public PostRequestTask(String urlString, String key, String value) {
        this.urlString = urlString;
        this.value = value;
        this.key = key;
    }

    @Override
    public String call() throws Exception {
        StringBuilder response = new StringBuilder();
        OutputStream os = null;
        BufferedReader br = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            if (key != null && !key.isEmpty()) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            } else {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            }
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String urlParameters;
            if (key != null && !key.isEmpty()) {
                urlParameters = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            } else {
                urlParameters = value;
            }

            os = conn.getOutputStream();
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            int responseCode = conn.getResponseCode();
            InputStreamReader streamReader;

            if (responseCode >= 200 && responseCode < 300) {
                streamReader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                streamReader = new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8);
            }

            br = new BufferedReader(streamReader);
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            conn.disconnect();

        } finally {
            if (os != null) os.close();
            if (br != null) br.close();
        }
        return response.toString();
    }
}
