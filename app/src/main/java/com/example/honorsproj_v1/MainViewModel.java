package com.example.honorsproj_v1;

import static android.content.ContentValues.TAG;

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
import android.content.SharedPreferences;
import java.util.Calendar;


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
        // Check if data has already been saved today
        if (isDataSavedToday()) {
            Log.d("MainViewModel", "Data has already been saved today");
            return; // Exit method if data has already been saved today
        }

        // If data has not been saved today, make the API call
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://horse-racing.p.rapidapi.com/racecards?date=2024-03-13")
                .get()
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
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

    private boolean isDataSavedToday() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Get the last saved date from SharedPreferences
        SharedPreferences preferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedYear = preferences.getInt("Year", -1);
        int savedMonth = preferences.getInt("Month", -1);
        int savedDay = preferences.getInt("Day", -1);

        // Compare the dates
        return (todayYear == savedYear && todayMonth == savedMonth && todayDay == savedDay);
    }


    private void saveDataToFile(String data) {
        // Save the data to file

        // Save the current date to SharedPreferences
        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

        SharedPreferences preferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Year", todayYear);
        editor.putInt("Month", todayMonth);
        editor.putInt("Day", todayDay);
        editor.apply();

        // Create the file if it doesn't exist
        File file = new File(applicationContext.getFilesDir(), "response_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // Write data to the file
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file write error
        }
    }


    void printFileContents() {
        String fileContents = readFileContents();
        Log.d("MainViewModel", "File contents: " + fileContents);
        System.out.println("File contents: " + fileContents);
        // Assuming responseData is the string containing the API response data
        int length = fileContents.length();
        int startIndex = length * 2 / 3; // Start index for the last half of the data
        String lastThird  = fileContents.substring(startIndex);
        Log.d(TAG, "Last half of the data: " + lastThird);

    }

    private String readFileContents() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(applicationContext.getFilesDir(), "response_data.txt");

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                fis.close();
            } else {
                Log.d("MainViewModel", "File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    void clearSavedData() {
        SharedPreferences preferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Year");
        editor.remove("Month");
        editor.remove("Day");
        editor.apply();
        Log.d("MainViewModel", "SHITS GONE");
    }


}
