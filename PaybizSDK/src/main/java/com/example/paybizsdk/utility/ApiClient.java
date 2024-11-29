package com.example.paybizsdk.utility;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

//    private final OkHttpClient client;
//
//    // MediaType for JSON
//    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
//
//    public ApiClient() {
//        this.client = new OkHttpClient();
//    }
//
//    /**
//     * Makes a POST request with a JSON body and returns the response as a String.
//     * @param url The API URL.
//     * @param jsonBody The JSON object containing the request body.
//     * @return The response body as a String.
//     * @throws IOException if an I/O error occurs during the request.
//     */
//    public String post(String url, String body) throws IOException {
//        // Create the request body from the JSON object
//        RequestBody requestBody = RequestBody.create(body, JSON);
//
//        // Build the request
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        // Execute the request and retrieve the response
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                return response.body().string();  // Return response as String
//            } else {
//                throw new IOException("Unexpected code " + response.code() + ": " + response.message());
//            }
//        }
//    }


    // new


    private final OkHttpClient client;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ApiClient() {
        this.client = new OkHttpClient();
    }

    /**
     * Makes a POST request with a JSON body and returns the response as a String.
     * This method is synchronous and should be called from a background thread only.
     *
     * @param url The API URL.
     * @param jsonBody The JSON body as a String.
     * @return The response body as a String.
     * @throws IOException if an I/O error occurs during the request.
     */
    public String post(String url, String jsonBody) throws IOException {
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response.code() + ": " + response.message());
            }
        }
    }


    public String get(String baseUrl, String pathVariable) throws IOException {
        // Construct the URL with the path variable
        String url = baseUrl + "/" + pathVariable;

        // Build the GET request
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string(); // Return the response body
            } else {
                throw new IOException("Unexpected code " + response.code() + ": " + response.message());
            }
        }
    }

}
