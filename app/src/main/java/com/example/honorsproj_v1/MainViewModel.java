package com.example.honorsproj_v1;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewModel extends AndroidViewModel {
    //variables
    private MutableLiveData<String> apiResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, List<String>>> coursesLiveData = new MutableLiveData<>();

    private Context applicationContext;

    public MainViewModel(Application application) {
        super(application);
        this.applicationContext = application.getApplicationContext();
    }

    public LiveData<String> getApiResponseLiveData() {//get api response
        return apiResponseLiveData;
    }

    public LiveData<Map<String, List<String>>> getCoursesLiveData() {//gets course data
        return coursesLiveData;
    }


    void makeApiCallAndSaveToFile() {
        // Check if data has already been saved today
        if (isDataSavedToday()) {
            Log.d("MainViewModel", "Data has already been saved today");
            // If data has already been saved today return and dont do following
            return;
        }

        // If data has not been saved today, make the API call
        OkHttpClient client = new OkHttpClient();

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // January is 0 so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Build the URL with the current date
        String url = "https://horse-racing.p.rapidapi.com/racecards?date=" + year + "-" + month + "-" + day; //api call with todays date
        String testurl = "https://horse-racing.p.rapidapi.com/racecards?date=2024-03-15"; //testing url

        Request request = new Request.Builder() //Api call using okhttp
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle api call failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response
                    String responseData = response.body().string();
                    saveDataToFile(responseData); // Save data to file
                    printFileContents(); // Update coursesLiveData after saving data
                } else {
                    // Handle error response
                    Log.d(TAG, "onResponse: Failure");
                }
            }
        });
    }


    private boolean isDataSavedToday() { //Checks if data has been saved today
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
            Log.d(TAG, "saveDataToFile: Failure");
            // Handle file write error
        }
    }


    void printFileContents() { //Prints file contents and extracts courses
        String fileContents = readFileContents();
        Log.d("MainViewModel", "File contents: " + fileContents);

        try {
            JSONArray jsonArray = new JSONArray(fileContents);
            Map<String, List<String>> coursesMap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String course = jsonObject.getString("course");
                String dateTime = jsonObject.getString("date");

                // Extracting only the time part from the datetime string
                String time = dateTime.substring(dateTime.indexOf(' ') + 1, dateTime.lastIndexOf(':'));
                // Add course to map if it doesn't exist, otherwise add time to its list
                if (!coursesMap.containsKey(course)) {
                    List<String> timesList = new ArrayList<>();
                    timesList.add(time);
                    coursesMap.put(course, timesList);
                } else {
                    coursesMap.get(course).add(time);
                }
            }

            // Log the contents of the HashMap
            for (Map.Entry<String, List<String>> entry : coursesMap.entrySet()) {
                String course = entry.getKey();
                List<String> times = entry.getValue();
                Log.d("MainViewModel", "Course: " + course + ", Times: " + times.toString());
            }

            // Update LiveData with the new coursesMap
            coursesLiveData.postValue(coursesMap);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "printFileContents: Failure");
            // Handle error
        }
    }



    private String readFileContents() { //Reads file contents and returns value for printFileContents
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

    void clearSavedData() { //clears saved data for testing
        SharedPreferences preferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Year");
        editor.remove("Month");
        editor.remove("Day");
        editor.apply();
        Log.d("MainViewModel", "SHITS GONE");
    }


}