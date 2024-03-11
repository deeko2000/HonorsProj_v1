package com.example.honorsproj_v1;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<String> apiResponseLiveData = new MutableLiveData<>();
    private Context applicationContext;

    public MainViewModel(Application application) {
        super(application);
        this.applicationContext = application.getApplicationContext();
    }

    public LiveData<String> getApiResponseLiveData() {
        return apiResponseLiveData;
    }

    void makeApiCallAndSaveToFile() {
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
                    saveDataToFile(responseData); // Save data to file
                } else {
                    // Handle error response
                    // You might want to handle different HTTP error codes differently
                }
            }
        });
    }

    private void saveDataToFile(String data) {
        File file = new File(applicationContext.getFilesDir(), "response_data.txt");

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
            // Notify observers about file save completion or provide appropriate feedback
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file write error
        }
    }

    void printFileContents() {
        String fileContents = readFileContents();
        Log.d("MainViewModel", "File contentss: " + fileContents);
    }

    private String readFileContents() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(applicationContext.getFilesDir(), "response_data.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
