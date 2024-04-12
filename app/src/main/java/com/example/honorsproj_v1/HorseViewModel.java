package com.example.honorsproj_v1;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HorseViewModel extends ViewModel {
    private OkHttpClient client;

    public HorseViewModel() {
        client = new OkHttpClient();
    }

    // Method to perform API call to get horseId
    public void getHorseId(String horseName, final OnHorseIdReceivedListener listener) {
        // Create a request with your API endpoint to get horseId
        Request request = new Request.Builder()
                .url("https://horse-racing.p.rapidapi.com/query-horses?name=" + horseName)
                .get()
                .addHeader("X-RapidAPI-Key", "d70a23cef9mshfe1d96275f11100p155f05jsna9862e3a7d1c")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure (e.g., network issues)
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the API response
                if (response.isSuccessful()) {
                    // Parse the response body to get horseId
                    String responseBody = response.body().string();
                    String horseId = parseHorseId(responseBody); // Implement this method to parse horseId from response
                    listener.onHorseIdReceived(horseId);
                } else {
                    // Handle unsuccessful response
                    listener.onFailure();
                }
            }
        });
    }

    // Method to query a different API using horseId
    void queryApiWithHorseId(String horseId, final OnApiDataReceivedListener listener) {
        // Build the request with the horseId
        Request request = new Request.Builder()
                .url("https://horse-racing.p.rapidapi.com/horse-stats/" + horseId) // Use the horseId from the first API call
                .get()
                .addHeader("X-RapidAPI-Key", "d70a23cef9mshfe1d96275f11100p155f05jsna9862e3a7d1c")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure of the second API call
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response of the second API call
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    listener.onDataReceived(responseData);
                } else {
                    listener.onFailure();
                }
            }
        });
    }

    // Interface to listen for horseId retrieval events
    public interface OnHorseIdReceivedListener {
        void onHorseIdReceived(String horseId);
        void onFailure();
    }

    // Interface to listen for API data retrieval events
    public interface OnApiDataReceivedListener {
        void onDataReceived(String data);
        void onFailure();
    }

    // Method to parse horseId from API response
    private String parseHorseId(String responseBody) {
        try {
            // Parse JSON array response
            JSONArray jsonArray = new JSONArray(responseBody);

            // Iterate through each JSON object in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extract horse ID from each JSON object
                String horseId = jsonObject.getString("id_horse");
                System.out.println("Horse ID: " + horseId); // Print the horse ID
                return horseId; // Return the horse ID
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Return null if parsing fails
    }
}


