package com.example.paybizsdk.utility;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiCall {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void postRequestWithExecutorService(String url, String jsonBody) {
        executorService.execute(() -> {
            ApiClient apiClient = new ApiClient();
            try {
                // Execute the POST request and get the response
                String response = apiClient.post(url, jsonBody);

                // Post the response to the main thread to handle it in the UI or log it
                new Handler(Looper.getMainLooper()).post(() -> {
                    // Print the response on the main thread
                    System.out.println("Response from API: " + response);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
