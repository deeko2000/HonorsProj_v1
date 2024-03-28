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
    // Variables
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
        return "MeetingViewModelPrefs"; // Unique key for MeetingViewModel
    }

    void makeApiCallAndSaveToFile() {
        // Check if data has already been saved today
        if (isDataSavedToday()) {
            Log.d("MeetingViewModel", "Data has already been saved today");
            // If data has already been saved today return and don't do the following
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
                // Handle api call failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response
                    String responseData = response.body().string();
                    saveDataToFile(responseData); // Save data to file
                    printFileContents(); // Update horsesLiveData after saving data
                } else {
                    // Handle error response
                    Log.d("MeetingViewModel", "onResponse: Failure");
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

        // Get the last saved date from SharedPreferences using the unique key
        SharedPreferences preferences = applicationContext.getSharedPreferences(getSharedPreferencesKey(), Context.MODE_PRIVATE);
        int savedYear = preferences.getInt("Year", -1);
        int savedMonth = preferences.getInt("Month", -1);
        int savedDay = preferences.getInt("Day", -1);

        // Compare the dates
        return (todayYear == savedYear && todayMonth == savedMonth && todayDay == savedDay);
    }

    private void saveDataToFile(String data) {
        // Save the data to file

        // Save the current date to SharedPreferences using the unique key
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

        // Create the file if it doesn't exist
        File file = new File(applicationContext.getFilesDir(), "response_data.json");
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
            Log.d("MeetingViewModel", "saveDataToFile: Failure");
            // Handle file write error
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

                // Iterate through each race meeting
                for (int i = 0; i < racecardsArray.length(); i++) {
                    JSONObject racecardObject = racecardsArray.getJSONObject(i);
                    String offTime = racecardObject.getString("off_time");

                    // Convert offTime to 24-hour format
                    String convertedTime = convertTo24HourFormat(offTime);

                    JSONArray runnersArray = racecardObject.getJSONArray("runners");

                    List<String> horseNames = new ArrayList<>();

                    // Iterate through each runner in the race meeting
                    for (int j = 0; j < runnersArray.length(); j++) {
                        JSONObject runnerObject = runnersArray.getJSONObject(j);
                        String horseName = runnerObject.getString("horse");
                        horseNames.add(horseName);
                    }

                    // Add the list of horse names to the map with converted off time as key
                    horseMap.put(convertedTime, horseNames);
                }

                // Log the contents of the HashMap
                for (Map.Entry<String, List<String>> entry : horseMap.entrySet()) {
                    String time = entry.getKey();
                    List<String> horses = entry.getValue();
                    //Log.d("MeetingViewModel", "Key: " + time); // Log key
                    for (String horse : horses) {
                        //Log.d("MeetingViewModel", "Horse: " + horse); // Log each horse associated with the key
                    }
                }

                // Update LiveData with the new horseMap
                horsesLiveData.postValue(horseMap);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("MeetingViewModel", "printFileContents: Failure");
                // Handle error
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

    // Method to convert time to 24-hour format
    private String convertTo24HourFormat(String offTime) {
        try {
            String[] parts = offTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            // Convert hour to 24-hour format if it's less than 12
            if (hour < 12) {
                hour += 12;
            }

            // Return the time in 24-hour format
            return String.format("%02d:%02d", hour, minute);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
            return offTime; // Return original offTime if parsing fails
        }
    }


}
