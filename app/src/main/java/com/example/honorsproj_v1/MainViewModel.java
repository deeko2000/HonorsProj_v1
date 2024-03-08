package com.example.honorsproj_v1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> apiResponseLiveData = new MutableLiveData<>();

    public LiveData<String> getApiResponseLiveData() {
        return apiResponseLiveData;
    }

    void makeApiCall() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://andruxnet-random-famous-quotes.p.rapidapi.com/?cat=movies&count=10")
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "andruxnet-random-famous-quotes.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response
                    String responseData = response.body().string();
                    System.out.println("A" + responseData);
                    // Process the response data here
                } else {
                    // Handle error response
                    // You might want to handle different HTTP error codes differently
                }
            }
        });
    }
}

