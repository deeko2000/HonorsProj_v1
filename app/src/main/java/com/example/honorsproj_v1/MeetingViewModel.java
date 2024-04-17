package com.example.honorsproj_v1;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeetingViewModel extends AndroidViewModel {
    private MutableLiveData<String> apiResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, List<String>>> horsesLiveData = new MutableLiveData<>();
    private Context applicationContext;

    public MeetingViewModel(Application application) {
        super(application);
        this.applicationContext = application.getApplicationContext();
    }

    public LiveData<String> getApiResponseLiveData() {
        return apiResponseLiveData;
    }

    public LiveData<Map<String, List<String>>> getHorsesLiveData() {
        return horsesLiveData;
    }


    private String getSharedPreferencesKey() {
        return "MeetingViewModelPrefs";
    }

    void makeApiCallAndSaveToFile() {

        if (isDataSavedToday()) {
            Log.d("MeetingViewModel", "Data has already been saved today");

            return;
        }


        OkHttpClient client = new OkHttpClient();


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        String url = "https://the-racing-api1.p.rapidapi.com/v1/racecards/free?day=today&region_codes%5B0%5D=gb&region_codes%5B1%5D=ire";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "14592a19b7msha506b13166c4e3fp16bc30jsn1ca5f22e6ec7")
                .addHeader("X-RapidAPI-Host", "the-racing-api1.p.rapidapi.com")
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
                    Log.d("MeetingViewModel", "onResponse: Failure");
                }
            }
        });
    }


    private boolean isDataSavedToday() {

        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);


        SharedPreferences preferences = applicationContext.getSharedPreferences(getSharedPreferencesKey(), Context.MODE_PRIVATE);
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

        SharedPreferences preferences = applicationContext.getSharedPreferences(getSharedPreferencesKey(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Year", todayYear);
        editor.putInt("Month", todayMonth);
        editor.putInt("Day", todayDay);
        editor.apply();


        File file = new File(applicationContext.getFilesDir(), "response_data.json");
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
            Log.d("MeetingViewModel", "saveDataToFile: Failure");

        }
    }

    void printFileContents() {
        String fileContents = readFileContents();
        Log.d("MeetingViewModel", "File contents: " + fileContents);

        if (!fileContents.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(fileContents);
                JSONArray racecardsArray = jsonObject.getJSONArray("racecards");

                Map<String, List<String>> horseMap = new HashMap<>();


                for (int i = 0; i < racecardsArray.length(); i++) {
                    JSONObject racecardObject = racecardsArray.getJSONObject(i);
                    String offTime = racecardObject.getString("off_time");


                    String convertedTime = convertTo24HourFormat(offTime);

                    JSONArray runnersArray = racecardObject.getJSONArray("runners");

                    List<String> horseNames = new ArrayList<>();


                    for (int j = 0; j < runnersArray.length(); j++) {
                        JSONObject runnerObject = runnersArray.getJSONObject(j);
                        String horseName = runnerObject.getString("horse");
                        horseNames.add(horseName);
                    }


                    horseMap.put(convertedTime, horseNames);
                }


                for (Map.Entry<String, List<String>> entry : horseMap.entrySet()) {
                    String time = entry.getKey();
                    List<String> horses = entry.getValue();

                    for (String horse : horses) {
                    }
                }


                horsesLiveData.postValue(horseMap);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("MeetingViewModel", "printFileContents: Failure");

            }
        }
    }







    private String readFileContents() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(applicationContext.getFilesDir(), "response_data.json");

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
                Log.d("MeetingViewModel", "File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    void clearSavedData() {
        SharedPreferences preferences = applicationContext.getSharedPreferences(getSharedPreferencesKey(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Year");
        editor.remove("Month");
        editor.remove("Day");
        editor.apply();
        Log.d("MeetingViewModel", "Saved data cleared");
    }


    private String convertTo24HourFormat(String offTime) {
        try {
            String[] parts = offTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);


            if (hour < 12) {
                hour += 12;
            }


            return String.format("%02d:%02d", hour, minute);
        } catch (Exception e) {
            e.printStackTrace();

            return offTime;
        }
    }


}
