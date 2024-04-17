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

    private MutableLiveData<String> apiResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, List<String>>> coursesLiveData = new MutableLiveData<>();

    private Context applicationContext;

    public MainViewModel(Application application) {
        super(application);
        this.applicationContext = application.getApplicationContext();
    }

    public LiveData<String> getApiResponseLiveData() {
        return apiResponseLiveData;
    }

    public LiveData<Map<String, List<String>>> getCoursesLiveData() {
        return coursesLiveData;
    }


    void makeApiCallAndSaveToFile() {

        if (isDataSavedToday()) {
            Log.d("MainViewModel", "Data has already been saved today");

            return;
        }


        OkHttpClient client = new OkHttpClient();


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        String url = "https://horse-racing.p.rapidapi.com/racecards?date=" + year + "-" + month + "-" + day;


        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "horse-racing.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseData = response.body().string();
                    saveDataToFile(responseData);
                    printFileContents();
                } else {

                    Log.d(TAG, "onResponse: Failure");
                }
            }
        });
    }


    private boolean isDataSavedToday() {

        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);


        SharedPreferences preferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedYear = preferences.getInt("Year", -1);
        int savedMonth = preferences.getInt("Month", -1);
        int savedDay = preferences.getInt("Day", -1);


        return (todayYear == savedYear && todayMonth == savedMonth && todayDay == savedDay);
    }


    private void saveDataToFile(String data) {

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


        File file = new File(applicationContext.getFilesDir(), "response_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }


            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "saveDataToFile: Failure");

        }
    }


    void printFileContents() {
        String fileContents = readFileContents();
        Log.d("MainViewModel", "File contents: " + fileContents);

        try {
            JSONArray jsonArray = new JSONArray(fileContents);
            Map<String, List<String>> coursesMap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String course = jsonObject.getString("course");
                String dateTime = jsonObject.getString("date");
                String time = dateTime.substring(dateTime.indexOf(' ') + 1, dateTime.lastIndexOf(':'));
                if (!coursesMap.containsKey(course)) {
                    List<String> timesList = new ArrayList<>();
                    timesList.add(time);
                    coursesMap.put(course, timesList);
                } else {
                    coursesMap.get(course).add(time);
                }
            }


            for (Map.Entry<String, List<String>> entry : coursesMap.entrySet()) {
                String course = entry.getKey();
                List<String> times = entry.getValue();
                Log.d("MainViewModel", "Course: " + course + ", Times: " + times.toString());
            }


            coursesLiveData.postValue(coursesMap);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "printFileContents: Failure");

        }
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